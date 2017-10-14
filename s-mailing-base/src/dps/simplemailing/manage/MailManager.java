package dps.simplemailing.manage;

import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.User;
import dps.simplemailing.mailqueue.MailQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@ApplicationScoped
public class MailManager extends ManagerBase<Mail,Long> {

    @Inject
    UserManager users;

    @Inject
    MailQueue queue;

    @Transactional(Transactional.TxType.REQUIRED)
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

}
