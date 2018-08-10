package dps.simplemailing.manage;

import dps.simplemailing.entities.*;
import dps.simplemailing.mailqueue.MailQueue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.logging.Level;

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
    private int maxdelay = 10080;

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String loggerName() { return this.getClass().getPackage().getName()+".SeriesManager";}

    @PostConstruct
    public void init()
    {
        setLogLevel(Level.FINEST);
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
        //logInfo("processing all series");
        List<Series> allSeries = this.getAll();
        if (allSeries.isEmpty()) return;
        for (Series series : allSeries) {
            mailSeries.processSeries(series);
        }
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void processSeries(Series series) {


        try {

            mailSeries.updateAllSubscriptionSeriesMails(series);
            mailSeries.processAllSubscriptionSeriesMails(series);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void updateAllSubscriptionSeriesMails(Series series)
    {
        logFine("processing series "+series.getId());

        Collection<SeriesSubscription> seriesSubscriptions = mailSeries.getSeriesSubscriptions(series);
        for (SeriesSubscription subscription : seriesSubscriptions) {
            try {
                mailSeries.updateSeriesMails(subscription);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Collection<SeriesSubscription> getSeriesSubscriptions(Series series)
    {
        series = mailSeries.reload(series/*, Series_.seriesSubscriptions*/);
        return series.getSeriesSubscriptions().values();
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void processAllSubscriptionSeriesMails(Series series)
    {
        Collection<SeriesSubscription> seriesSubscriptions = mailSeries.getSeriesSubscriptions(series);
        for (SeriesSubscription subscription : seriesSubscriptions) {
            try {
                mailSeries.processSeriesMails(subscription);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Transactional(Transactional.TxType.REQUIRED)
    public void updateSeriesMails(SeriesSubscription subscription)
    {
        //Series series = mailSeries.reload(subscription.getSeries(),Series_.seriesItems);
        subscription = subscriptionManager.reload(subscription);
        Series series = subscription.getSeries();
        for (SeriesItem item : series.getSeriesItems()) {
            SeriesMail seriesMail = mailSeries.getSeriesMail(subscription,item);
            if (seriesMail == null) {
                seriesMail = mailSeries.createSeriesMail(subscription, item);
            }
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void processSeriesMails(SeriesSubscription subscription) {

        List<SeriesMail> subscriptionMails = em.createNamedQuery("SeriesMail.getSubscriptionMails", SeriesMail.class)
                .setParameter("subscription", subscription)
                .getResultList();

        logFiner("processing series subscription "+subscription.getId());
        for (SeriesMail seriesMail: subscriptionMails) {
            logFinest("processing series mail: item #"+seriesMail.getSeriesItem().getId()+" subscription #"+seriesMail.getSeriesSubscription().getId());
            if (seriesMail.getStatus().equals(SeriesMail.Status.unsent) && (seriesMail.getSeriesItem().getSendOrder() == 0 || seriesMail.getSeriesItem().getSendOrder() == subscription.getLastItemOrder()+1)) {
                try {
                    mailSeries.processSeriesMail(seriesMail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void processSeriesMail(SeriesMail seriesMail) {

        SeriesSubscription subscription = seriesMail.getSeriesSubscription();
        SeriesItem item = seriesMail.getSeriesItem();
        User user = subscription.getUser();
        Calendar cal = Calendar.getInstance();

        if (user.getStatus() != User.Status.subscribed) {
            logFinest("User unsubscribed");
            seriesMail.setStatus(SeriesMail.Status.canceled);
            em.merge(seriesMail);
            return;
        }

        logFinest("delay: "+item.getSendDelay()+" subscribeTime: "+subscription.getSubscribeTime());

        int delayAfterSubscribe = item.getSendDelay();
        int delayAfterItem = item.getSendDelayLastItem();
        int delayAfterMail = item.getSendDelayLastMail();

        Date subscribeTime = subscription.getSubscribeTime();
        Date lastItemSendTime = subscription.getLastItemSendTime();
        Date lastMailSendTime = user.getLastSeriesMailSendTime();

        long now = this.getCurrentTime().getTime();
        long sendTime = 0;

        logFinest("now: "+now);

        long delayTime = 0;

        if (delayAfterSubscribe > 0 && subscribeTime != null) {
            cal.setTime(subscribeTime);
            cal.add(unit, delayAfterSubscribe);
            delayTime = cal.getTime().getTime();
            logFinest("delay after subscribe time: "+delayTime);
            sendTime = Math.max(sendTime,delayTime);
        }

        if (delayAfterItem > 0 && lastItemSendTime != null && item.getSendOrder() == subscription.getLastItemOrder()+1) {
            cal.setTime(lastItemSendTime);
            cal.add(unit, delayAfterItem);
            delayTime = cal.getTime().getTime();
            logFinest("delay after item time: "+delayTime);
            sendTime = Math.max(sendTime,delayTime);
        }

        if (delayAfterMail > 0 && lastMailSendTime != null)
        {
            cal.setTime(lastMailSendTime);
            cal.add(unit, delayAfterMail);
            delayTime = cal.getTime().getTime();
            logFinest("delay after mail time: "+delayTime);
            sendTime = Math.max(sendTime,delayTime);
        }

        cal.setTime(new Date(sendTime));
        cal.add(unit, maxdelay);
        long latestSendTime = cal.getTime().getTime();

        logFinest("send time: "+sendTime+" now: "+now+" latest send time: "+latestSendTime);

        if (sendTime <= now) {

            logFinest("send time elapsed");

            if (latestSendTime >= now) {

                if (seriesMail.getStatus() == SeriesMail.Status.unsent) {
                    logFinest("Queuing mail series: " + seriesMail);
                    mailQueue.createQueuedMail(seriesMail.getSeriesSubscription().getUser(), seriesMail.getSeriesItem().getMail(), new Date(now));
                    seriesMail.setStatus(SeriesMail.Status.sent);
                    seriesMail.setSentTime(new Date(now));
                    em.merge(seriesMail);
                    subscription.setLastItemSendTime(new Date(now));
                    subscription.setLastItemOrder(item.getSendOrder());
                    em.merge(subscription);
                    user.setLastSeriesMailSendTime(new Date(now));
                    em.merge(user);
                }

            } else {

                logFinest("timeout");
                subscription.setLastItemOrder(item.getSendOrder());
                em.merge(subscription);
                seriesMail.setStatus(SeriesMail.Status.canceled);
                em.merge(seriesMail);

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

    @Transactional(Transactional.TxType.REQUIRED)
    public SeriesMail getSeriesMail(SeriesSubscription subscription, SeriesItem item)
    {
        SeriesMail seriesMail = em.find(SeriesMail.class,new SeriesMailId(subscription,item));
        return seriesMail;
    }

}
