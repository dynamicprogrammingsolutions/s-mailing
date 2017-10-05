package dps.simplemailing.manage;

import dps.simplemailing.entities.*;
import dps.simplemailing.mailqueue.MailQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.*;

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
    MailQueue mailQueue;

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

    @Transactional(Transactional.TxType.REQUIRED)
    public void processSeries(Series series) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 1440);
        Date processUntil = cal.getTime();

        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -30240);
        Date processAfter = cal.getTime();

        series = em.merge(series);

        for (SeriesSubscription subscription : series.getSeriesSubscriptions().values()) {
            if (subscription.getUser().getStatus() != User.Status.subscribed) continue;
            for (SeriesItem item : series.getSeriesItems()) {
                //System.out.println("processing item "+item.getId()+" on subscription "+subscription.getId());

                cal.setTime(subscription.getSubscribeTime());
                cal.add(Calendar.MINUTE, item.getSendDelay());
                Date sendTime = cal.getTime();

                if (sendTime.before(processUntil) && sendTime.after(processAfter)) {

                    SeriesMail seriesMail = subscription.getSeriesMails().get(item);
                    if (seriesMail == null) {
                        seriesMail = new SeriesMail();
                        seriesMail.setSeriesItem(item);
                        seriesMail.setSeriesSubscription(subscription);
                        seriesMail.setStatus(SeriesMail.Status.unsent);
                        em.merge(seriesMail);
                    }
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

    @Transactional(Transactional.TxType.REQUIRED)
    public void createSeriesMail(SeriesSubscription subscription, SeriesItem item)
    {
        SeriesMail seriesMail = new SeriesMail();
        seriesMail.setSeriesItem(item);
        seriesMail.setSeriesSubscription(subscription);
        seriesMail.setStatus(SeriesMail.Status.unsent);
        em.persist(seriesMail);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void removeSeriesMail(SeriesSubscription subscription, SeriesItem item)
    {
        SeriesMail seriesMail = getSeriesMail(subscription,item);
        if (seriesMail != null) em.remove(seriesMail);
    }

    public SeriesMail getSeriesMail(SeriesSubscription subscription, SeriesItem item)
    {
        return subscriptionManager.reload(subscription,SeriesSubscription_.seriesMails).getSeriesMails().get(item);
    }

}
