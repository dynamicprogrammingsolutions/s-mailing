package dps.simplemailing.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="mail_queue")
@NamedQueries({
        @NamedQuery(name="QueuedMail.getAll",query="SELECT m FROM QueuedMail m"),
        @NamedQuery(name="QueuedMail.count",query="SELECT COUNT(m) FROM QueuedMail m"),
        @NamedQuery(name="QueuedMail.getQueue",query="SELECT m FROM QueuedMail m WHERE m.status = :status AND (m.scheduledTime is null OR m.scheduledTime <= :now)"),
        @NamedQuery(name="QueuedMail.getAllWithGenerated",query="SELECT m FROM QueuedMail m WHERE (m.status <> :status) AND (m.generatedMail IS NOT NULL)")
})
public class QueuedMail implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = 7480663708681616919L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name="mail_id",nullable=false)
    private Mail mail;
    
    @ManyToOne
    @NotNull
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="generated_mail_id")
    private GeneratedMail generatedMail;
    
    private java.util.Date scheduledTime;
    
    private java.util.Date sentTime;
    
    @NotNull
    private Status status;
            
    public enum Status {
        unsent, sent, fail
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GeneratedMail getGeneratedMail() {
        return generatedMail;
    }

    public void setGeneratedMail(GeneratedMail generatedMail) {
        this.generatedMail = generatedMail;
    }

    public java.util.Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(java.util.Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public java.util.Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(java.sql.Date sentTime) {
        this.sentTime = sentTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof QueuedMail)) {
            return false;
        }
        QueuedMail other = (QueuedMail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.QueuedMail[ id=" + id + " ]";
    }
    
}
