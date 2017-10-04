package dps.simplemailing.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ferenci84
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Campaign.getAll",query="SELECT m FROM Campaign m ORDER BY m.name"),
    @NamedQuery(name="Campaign.count",query="SELECT COUNT(m) FROM Campaign m"),
})
public class Campaign implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = 245322185179652275L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
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
    public void setId(Long id) {
        this.id = id;
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
