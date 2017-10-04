package dps.simplemailing.manage;

import dps.simplemailing.entities.*;
import dps.simplemailing.mailqueue.MailQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class SeriesManager extends ManagerBase<Series,Long> {

    @Inject
    SeriesManager mailSeries;

    @Inject
    MailManager mailManager;

    @Inject
    UserManager userManager;

    @Inject
    MailQueue mailQueue;

    @Transactional(Transactional.TxType.REQUIRED)
    public void createSubscription(Series series, User user, SeriesSubscription seriesSubscription)
    {
        seriesSubscription.setSeries(series);
        seriesSubscription.setUser(user);
        em.persist(seriesSubscription);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createSubscription(Long seriesId, Long userId, SeriesSubscription seriesSubscription)
    {
        Series series = this.getById(seriesId);
        User user = userManager.getById(userId);
        this.createSubscription(series, user, seriesSubscription);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createItem(Series series, Mail mail, SeriesItem item)
    {
        item.setMail(mail);
        item.setSeries(series);

        Set<ConstraintViolation<SeriesItem>> constraintViolations = validator.validate(item);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed");
        }

        em.persist(item);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createItem(Long seriesId, Long mailId, SeriesItem item)
    {
        Series series = this.getById(seriesId);
        Mail mail = mailManager.getById(mailId);
        this.createItem(series,mail,item);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<SeriesItem> getItems(Long seriesId)
    {
        Series series = this.getById(seriesId);
        for (SeriesItem item: series.getSeriesItems()) {
            item.getId();
        }
        return series.getSeriesItems();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<SeriesItem> getItems(Series series)
    {
        em.refresh(series);
        for (SeriesItem item: series.getSeriesItems()) {
            item.getId();
        }
        return series.getSeriesItems();
    }

    public Series getByName(String name)
    {
        Query query = em.createQuery("SELECT u FROM Series u WHERE u.name = :name");
        query.setParameter("name", name);
        List<Series> series = query.getResultList();
        if (series.isEmpty()) return null;
        else return series.get(0);
    }

    public SeriesSubscription getSubscription(User user, Series series)
    {
        Query query = em.createQuery("SELECT u FROM SeriesSubscription u WHERE u.user = :user AND u.series = :series");
        query.setParameter("user", user);
        query.setParameter("series", series);
        List<SeriesSubscription> subscriptions = query.getResultList();
        if (subscriptions.isEmpty()) return null;
        else return subscriptions.get(0);

    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void processAllSeries()
    {
        Query query = em.createQuery("SELECT u FROM Series u");
        List<Series> allSeries = query.getResultList();
        if (allSeries.isEmpty()) return;
        for (Series series: allSeries) {
            mailSeries.processSeries(series);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void processSeries(Series series)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 1440);
        Date processUntil = cal.getTime();

        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -30240);
        Date processAfter = cal.getTime();

        series = em.merge(series);

        for(SeriesSubscription subscription: series.getSeriesSubscriptions()) {
            if (subscription.getUser().getStatus() != User.Status.subscribed) continue;
            for(SeriesItem item: series.getSeriesItems()) {
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
                        System.out.println("Queuing mail series: "+seriesMail);
                        mailQueue.createQueuedMail(seriesMail.getSeriesSubscription().getUser(), seriesMail.getSeriesItem().getMail(), sendTime);
                        seriesMail.setStatus(SeriesMail.Status.sent);
                        em.merge(seriesMail);
                    }

                }
            }
        }
    }
}
