package dps.simplemailing.mailqueue;

import dps.simplemailing.mailqueue.MailQueue;
import dps.simplemailing.manage.SeriesManager;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Date;

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


    @Schedule(hour = "*", minute = "*", persistent = false)
    public void processQueue() {
        //TODO: processQueueAsync in MailQueue (Using managed executorservice allowing only one thread)
        mailQueue.processQueue();
        //mailQueue.processQueue();
    }
    
    @Schedule(hour = "*", persistent = false)
    public void processAllSeries() {
        System.out.println("processing all series");
        //TODO: processAllSeries in MailSeries
        mailSeries.processAllSeries();
        System.out.println("finishing processing all series finished");
    }


}
