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


    @Schedule(hour = "*", minute = "*", persistent = false)
    public void processQueue() {
        //System.out.println("Timer event: " + new Date());
        //TODO: processQueueAsync in MailQueue (Using managed executorservice allowing only one thread)
        new Thread(()->mailQueue.processQueue(),"MailQueue.processQueue").start();
        //mailQueue.processQueue();
    }
    
    @Schedule(hour = "*", persistent = false)
    public void processAllSeries() {
        //System.out.println("Timer event: " + new Date());
        //TODO: processAllSeries in MailSeries
        new Thread(()->mailSeries.processAllSeries(),"MailSeries.processAllSeries").start();
        ;
    }


}
