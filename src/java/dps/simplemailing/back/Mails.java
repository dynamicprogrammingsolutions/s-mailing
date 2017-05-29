/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.User;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ferenci84
 */
@Stateless
public class Mails/* extends Crud_old<Mail>*/ {
    
    @Inject Users users;
    @Inject MailQueue queue;
    
    public Mails()
    {
        //super(Mail.class);
    }
    
    /*
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;
    protected EntityManager getEntityManager() {
        return em;
    }
    */
    
    public void scheduleMail(Mail mail, Boolean real, java.util.Date time, int msDelay)
    {
        Calendar cal = Calendar.getInstance();
        if (time == null) {
            cal.setTime(new java.util.Date());
        } else {
            cal.setTime(time);
        }
        
        List<User> allUsers;
        if (real) {
            allUsers = users.getActive();
        } else {
            allUsers = users.getTest();
        }
        for (User user: allUsers) {
            cal.add(Calendar.MILLISECOND, msDelay);
            queue.createQueuedMail(user, mail, cal.getTime());
        }
    }

    /*public void scheduleMail(Mail mail, java.util.Date time)
    {
        List<User> allUsers = users.getActive();
        for (User user: allUsers) {
            queue.createQueuedMail(user, mail, time);
        }
    }
    
    public void testMail(Mail mail)
    {
        List<User> allUsers = users.getTest();
        for (User user: allUsers) {
            queue.createQueuedMail(user, mail, null);
        }
    }*/

}
