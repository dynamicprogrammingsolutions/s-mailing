package dps.simplemailing.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ferenci84
 */
@Entity
public class Campaign implements Serializable {

    private static final long serialVersionUID = 245322185179652275L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String longName;
    
    @XmlTransient
    @ManyToMany
    private Set<Mail> mails = new HashSet<Mail>();
    
    @XmlTransient
    @ManyToMany
    private Set<User> unsubscribedUsers = new HashSet<User>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
    
    @XmlTransient
    public Set<Mail> getMails() {
        return mails;
    }

    @XmlTransient
    public Set<User> getUnsubscribedUsers() {
        return unsubscribedUsers;
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
        if (!(object instanceof Campaign)) {
            return false;
        }
        Campaign other = (Campaign) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.Campaign[ id=" + id + " ]";
    }
    
}
