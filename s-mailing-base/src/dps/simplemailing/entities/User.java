package dps.simplemailing.entities;

import dps.simplemailing.mailqueue.MailQueue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="users")
@NamedQueries({
        @NamedQuery(name="User.getAll",query="SELECT m FROM User m ORDER BY m.email"),
        @NamedQuery(name="User.count",query="SELECT COUNT(m) FROM User m"),
        @NamedQuery(name="User.getWithStatus",query = "SELECT u FROM User u WHERE u.status = :status"),
        @NamedQuery(name="User.getByEmail",query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = 1608424383634986692L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    private Date lastSeriesMailSendTime;
    
    
    @Column(name="status")
    @NotNull
    private Status status;
    
    @ManyToMany(mappedBy = "unsubscribedUsers")
    private Set<Campaign> unsubscribedFromCampaigns;

    @OneToMany(mappedBy = "mail",cascade = CascadeType.REMOVE)
    private List<QueuedMail> queuedMails;

    static public enum Status {
        subscribed,
        unsubscribed,
        test,
        bounced
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getLastSeriesMailSendTime() {
        return lastSeriesMailSendTime;
    }

    public void setLastSeriesMailSendTime(Date lastSeriesMailSendTime) {
        this.lastSeriesMailSendTime = lastSeriesMailSendTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.User[ id=" + id + " ]";
    }
    
}
