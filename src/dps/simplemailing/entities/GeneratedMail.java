package dps.simplemailing.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="generated_mails")
public class GeneratedMail implements Serializable {

    private static final long serialVersionUID = -8180324853934395167L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String fromEmail;
    
    @NotNull
    private String toEmail;
    
    @NotNull
    private String subject;
    
    @Lob
    private String body;

    @OneToOne(mappedBy = "generatedMail")
    private QueuedMail queuedMail;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    public QueuedMail getQueuedMail() {
        return queuedMail;
    }

    public void setQueuedMail(QueuedMail queuedMail) {
        this.queuedMail = queuedMail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneratedMail)) {
            return false;
        }
        GeneratedMail other = (GeneratedMail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.GeneratedMail[ id=" + id + " ]";
    }
    
}
