package dps.simplemailing.manage;

import dps.simplemailing.entities.*;
import dps.simplemailing.mailqueue.MailQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.*;

@SuppressWarnings("unchecked")
@ApplicationScoped
public class SeriesManager extends ManagerBase<Series,Long> {

    @Inject
    SeriesManager mailSeries;

    @Inject
    MailManager mailManager;

    @Inject
    UserManager userManager;

    @Inject
    SeriesSubscriptionManager subscriptionManager;

    @Inject
    SeriesManager seriesManager;

    @Inject
    MailQueue mailQueue;

    private int unit = Calendar.MINUTE;

    public void setUnit(int unit) {
        this.unit = unit;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createSubscription(Series series, User user, SeriesSubscription seriesSubscription) {
        seriesSubscription.setSeries(series);
        seriesSubscription.setUser(user);
        em.persist(seriesSubscription);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createSubscription(Long seriesId, Long userId, SeriesSubscription seriesSubscription) {
        Series series = this.getById(seriesId);
        User user = userManager.getById(userId);
        this.createSubscription(series, user, seriesSubscription);
    }

    //TODO: Test subscribe to series
    public void subscribeSeries(String seriesName, String userEmail, SeriesSubscription seriesSubscription)
    {
        User user = userManager.getByEmail(userEmail);
        Series series = seriesManager.getByName(seriesName);

        seriesSubscription.setSeries(series);
        seriesSubscription.setUser(user);

        SeriesSubscription subscription = seriesManager.getSubscription(user,series);
        if (subscription == null) {
            seriesSubscription.setSeries(series);
            seriesSubscription.setUser(user);
            subscriptionManager.create(seriesSubscription);
        } else {
            if (!series.getUpdateSubscribeTime()) {
                seriesSubscription.setSubscribeTime(subscription.getSubscribeTime());
            }
            subscriptionManager.modify(subscription.getId(),seriesSubscription);
        }

    }

    //TODO: Test unsubscribe series
    public void unsubscribeSeries(String seriesName, String userEmail)
    {
        User user = userManager.getByEmail(userEmail);
        Series series = seriesManager.getByName(seriesName);
        SeriesSubscription subscription = seriesManager.getSubscription(user,series);
        if (subscription != null) {
            subscriptionManager.remove(subscription);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createItem(Series series, Mail mail, SeriesItem item) {
        item.setMail(mail);
        item.setSeries(series);

        Set<ConstraintViolation<SeriesItem>> constraintViolations = validator.validate(item);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed");
        }

        em.persist(item);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createItem(Long seriesId, Long mailId, SeriesItem item) {
        Series series = this.getById(seriesId);
        Mail mail = mailManager.getById(mailId);
        this.createItem(series, mail, item);
    }

    public Series getByName(String name) {
        TypedQuery<Series> query = em.createNamedQuery(queryName("getByName"), entityClass);
        query.setParameter("name", name);
        List<Series> series = query.getResultList();
        if (series.isEmpty()) return null;
        else return series.get(0);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public SeriesSubscription getSubscription(User user, Series series) {
        return this.reload(series, Series_.seriesSubscriptions).getSeriesSubscriptions().get(user);
        /*
        Query query = em.createNamedQuery("SeriesSubscription.getSubscription");
        query.setParameter("user", user);
        query.setParameter("series", series);
        List<SeriesSubscription> subscriptions = query.getResultList();
        if (subscriptions.isEmpty()) return null;
        else return subscriptions.get(0);
        */
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void processAllSeries() {
        List<Series> allSeries = this.getAll();
        if (allSeries.isEmpty()) return;
        for (Series series : allSeries) {
            mailSeries.processSeries(series);
        }
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void processSeries(Series series) {

        series = this.reload(series,Series_.seriesSubscriptions);

        for (SeriesSubscription subscription : series.getSeriesSubscriptions().values()) {
            mailSeries.updateSeriesMails(subscription);
        }

        for (SeriesSubscription subscription : series.getSeriesSubscriptions().values()) {
            mailSeries.processSeriesMails(subscription);
        }

    }



    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void updateSeriesMails(SeriesSubscription subscription)
    {
        Series series = mailSeries.reload(subscription.getSeries(),Series_.seriesItems);
        for (SeriesItem item : series.getSeriesItems()) {
            SeriesMail seriesMail = mailSeries.getSeriesMail(subscription,item);
            if (seriesMail == null) {
                seriesMail = mailSeries.createSeriesMail(subscription, item);
            }
        }
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void processSeriesMails(SeriesSubscription subscription) {
        subscription = subscriptionManager.reload(subscription,SeriesSubscription_.seriesMails);
        for (SeriesMail seriesMail: subscription.getSeriesMails().values()) {
            if (seriesMail.getStatus().equals(SeriesMail.Status.unsent))
                mailSeries.processSeriesMail(seriesMail);
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void processSeriesMail(SeriesMail seriesMail) {

        seriesMail = em.find(SeriesMail.class,new SeriesMailId(seriesMail.getSeriesSubscription(),seriesMail.getSeriesItem()));

        SeriesSubscription subscription = seriesMail.getSeriesSubscription();
        SeriesItem item = seriesMail.getSeriesItem();
        User user = subscription.getUser();
        Calendar cal = Calendar.getInstance();


        System.out.println("delay: "+item.getSendDelay()+" subscribeTime: "+subscription.getSubscribeTime());

        int delayAfterSubscribe = item.getSendDelay();
        int delayAfterItem = item.getSendDelayLastItem();
        int delayAfterMail = item.getSendDelayLastMail();

        Date subscribeTime = subscription.getSubscribeTime();
        Date lastItemSendTime = subscription.getLastItemSendTime();
        Date lastMailSendTime = user.getLastSeriesMailSendTime();

        long now = this.getCurrentTime().getTime();
        long sendTime = 0;

        System.out.println("now: "+now);

        long delayTime = 0;

        if (delayAfterSubscribe > 0 && subscribeTime != null) {
            cal.setTime(subscribeTime);
            cal.add(unit, delayAfterSubscribe);
            delayTime = cal.getTime().getTime();
            System.out.println("delay after subscribe time: "+delayTime);
            sendTime = Math.max(sendTime,delayTime);
        }

        if (delayAfterItem > 0 && lastItemSendTime != null && item.getSendOrder() == subscription.getLastItemOrder()+1) {
            cal.setTime(lastItemSendTime);
            cal.add(unit, delayAfterItem);
            delayTime = cal.getTime().getTime();
            System.out.println("delay after item time: "+delayTime);
            sendTime = Math.max(sendTime,delayTime);
        }

        if (delayAfterMail > 0 && lastMailSendTime != null)
        {
            cal.setTime(lastMailSendTime);
            cal.add(unit, delayAfterMail);
            delayTime = cal.getTime().getTime();
            System.out.println("delay after mail time: "+delayTime);
            sendTime = Math.max(sendTime,delayTime);
        }

        System.out.println("send time: "+sendTime+" now: "+now);

        if (sendTime <= now) {

            System.out.println("send time elapsed");

            if (seriesMail.getStatus() == SeriesMail.Status.unsent) {
                System.out.println("Queuing mail series: " + seriesMail);
                mailQueue.createQueuedMail(seriesMail.getSeriesSubscription().getUser(), seriesMail.getSeriesItem().getMail(), new Date(now));
                seriesMail.setStatus(SeriesMail.Status.sent);
                seriesMail.setSentTime(new Date(now));
                em.merge(seriesMail);
                subscription.setLastItemSendTime(new Date(now));
                em.merge(subscription);
                user.setLastSeriesMailSendTime(new Date(now));
                em.merge(user);
            }

        }

    }

    private Date currentTime = null;

    public Date getCurrentTime()
    {
        return currentTime!=null ? currentTime : new Date();
    }

    public void setCurrentTime(Date date)
    {
        currentTime = date;
    }


    /*
    @Transactional(Transactional.TxType.REQUIRED)
    public void processSeries_old(Series series) {
        Calendar cal = Calendar.getInstance();

        series = this.reload(series,Series_.seriesSubscriptions);

        for (SeriesSubscription subscription : series.getSeriesSubscriptions().values()) {
            if (subscription.getUser().getStatus() != User.Status.subscribed) continue;
            for (SeriesItem item : series.getSeriesItems()) {
                System.out.println("processing item "+item.getId()+" on subscription "+subscription.getId());

                SeriesMail seriesMail = this.getSeriesMail(subscription,item);
                if (seriesMail == null) {
                    seriesMail = this.createSeriesMail(subscription,item);
                }

                if (seriesMail.getStatus().equals(SeriesMail.Status.unsent)) {


                    System.out.println("delay: "+item.getSendDelay()+" subscribeTime: "+subscription.getSubscribeTime());
                    cal.setTime(subscription.getSubscribeTime());
                    cal.add(unit, item.getSendDelay());
                    Date sendTime = cal.getTime();
                    Date now = new Date();

                    System.out.println("send time: "+sendTime+" now: "+now);

                    if (sendTime.before(now)) {

                        System.out.println("send time elapsed");

                        if (seriesMail.getStatus() == SeriesMail.Status.unsent) {
                            System.out.println("Queuing mail series: " + seriesMail);
                            mailQueue.createQueuedMail(seriesMail.getSeriesSubscription().getUser(), seriesMail.getSeriesItem().getMail(), sendTime);
                            seriesMail.setStatus(SeriesMail.Status.sent);
                            em.merge(seriesMail);
                        }

                    }

                }
            }
        }
    }
*/

    @Transactional(Transactional.TxType.REQUIRED)
    public SeriesMail createSeriesMail(SeriesSubscription subscription, SeriesItem item)
    {
        subscription = em.getReference(SeriesSubscription.class,subscription.getId());
        item = em.getReference(SeriesItem.class,item.getId());
        SeriesMail seriesMail = new SeriesMail(subscription,item);
        em.persist(seriesMail);
        return seriesMail;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void removeSeriesMail(SeriesSubscription subscription, SeriesItem item)
    {
        SeriesMail seriesMail = getSeriesMail(subscription,item);
        if (seriesMail != null) em.remove(seriesMail);
    }

    public SeriesMail getSeriesMail(SeriesSubscription subscription, SeriesItem item)
    {
        SeriesMail seriesMail = em.find(SeriesMail.class,new SeriesMailId(subscription,item));
        return seriesMail;
    }

}
