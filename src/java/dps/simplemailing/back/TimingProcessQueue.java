/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import java.util.Date;
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

    @Inject MailQueue mailQueue;
    @Inject MailSeries mailSeries;
    
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
