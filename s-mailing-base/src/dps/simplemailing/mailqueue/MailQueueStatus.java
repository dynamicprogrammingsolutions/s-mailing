package dps.simplemailing.mailqueue;

import javax.enterprise.context.ApplicationScoped;

//TODO: Move status to MailQueue
@ApplicationScoped
public class MailQueueStatus {
    Boolean started = false;
    int generated = 0;
    int sent = 0;
    int failed = 0;

    synchronized public int getGenerated() {
        return generated;
    }

    synchronized public void setGenerated(int generated) {
        this.generated = generated;
    }

    synchronized public int getSent() {
        return sent;
    }

    synchronized public void setSent(int sent) {
        this.sent = sent;
    }

    synchronized public int getFailed() {
        return failed;
    }

    synchronized public void setFailed(int failed) {
        this.failed = failed;
    }

    synchronized public Boolean getStarted() {
        return started;
    }

    synchronized public void setStarted(Boolean started) {
        this.started = started;
        if (this.started) {
            this.generated = 0;
            this.sent = 0;
            this.failed = 0;
        }
    }

    synchronized public String getStringStatus()
    {
        return "started: "+this.getStarted()+" generated: "+this.getGenerated()+" sent: "+this.getSent()+" failed: "+this.getFailed();
    }

}