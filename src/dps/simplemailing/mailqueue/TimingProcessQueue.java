package dps.simplemailing.mailqueue;

import dps.simplemailing.mailqueue.MailQueue;
import dps.simplemailing.manage.SeriesManager;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

/**
 *
 * @author ferenci84
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TimingProcessQueue {

    @Inject
    MailQueue mailQueue;
    @Inject
    SeriesManager mailSeries;
    
    @Schedule(hour = "*", minute = "*", second = "0", persistent = false)
    public void processQueue() {
        //System.out.println("Timer event: " + new Date());
        mailQueue.processQueue();
    }
    
    @Schedule(hour = "*", minute = "0", second = "0", persistent = false)
    public void processAllSeries() {
        //System.out.println("Timer event: " + new Date());
        mailSeries.processAllSeries();
    }

}
