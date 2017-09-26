package dps.simplemailing.back;

import dps.simplemailing.entities.Series;
import dps.simplemailing.entities.SeriesItem;
import dps.simplemailing.entities.SeriesMail;
import dps.simplemailing.entities.SeriesSubscription;
import dps.simplemailing.entities.User;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
public class MailSeries {

    @Inject Crud crud;
    @Inject MailQueue mailQueue;
    @Inject MailSeries mailSeries;
    
    public Series getByName(String name)
    {
        Query query = crud.getEntityManager().createQuery("SELECT u FROM Series u WHERE u.name = :name");
        query.setParameter("name", name);
        List<Series> series = query.getResultList();
        if (series.isEmpty()) return null;
        else return series.get(0);
    }
    
    public SeriesSubscription getSubscription(User user, Series series)
    {
        Query query = crud.getEntityManager().createQuery("SELECT u FROM SeriesSubscription u WHERE u.user = :user AND u.series = :series");
        query.setParameter("user", user);
        query.setParameter("series", series);
        List<SeriesSubscription> subscriptions = query.getResultList();
        if (subscriptions.isEmpty()) return null;
        else return subscriptions.get(0);
        
    }
    
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void processAllSeries()
    {
        Query query = crud.getEntityManager().createQuery("SELECT u FROM Series u");
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

        series = crud.getEntityManager().merge(series);

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
                        crud.edit(seriesMail);
                    }
                    if (seriesMail.getStatus() == SeriesMail.Status.unsent) {
                        System.out.println("Queuing mail series: "+seriesMail);
                        mailQueue.createQueuedMail(seriesMail.getSeriesSubscription().getUser(), seriesMail.getSeriesItem().getMail(), sendTime);
                        seriesMail.setStatus(SeriesMail.Status.sent);
                        crud.edit(seriesMail);
                    }

                }
            }
        }
    }

}
