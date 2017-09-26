package dps.simplemailing.back;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

/**
 *
 * @author ferenci84
 */
@Singleton
public class MailQueueStatus {
    Boolean started = false;
    int generated = 0;
    int sent = 0;
    int failed = 0;

    @Lock(LockType.READ)
    public int getGenerated() {
        return generated;
    }

    @Lock(LockType.WRITE)
    public void setGenerated(int generated) {
        this.generated = generated;
    }

    @Lock(LockType.READ)
    public int getSent() {
        return sent;
    }

    @Lock(LockType.WRITE)
    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }
    
    @Lock(LockType.READ)
    public Boolean getStarted() {
        return started;
    }

    @Lock(LockType.WRITE)
    public void setStarted(Boolean started) {
        this.started = started;
        if (this.started) {
            this.generated = 0;
            this.sent = 0;
            this.failed = 0;
        }
    }
    
    public String getStringStatus()
    {
        return "started: "+this.getStarted()+" generated: "+this.getGenerated()+" sent: "+this.getSent()+" failed: "+this.getFailed();
    }
    
}
