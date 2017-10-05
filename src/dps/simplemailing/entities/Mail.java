package dps.simplemailing.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import javax.validation.constraints.*;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="mails")
@NamedQueries({
    @NamedQuery(name="Mail.getAll",query="SELECT m FROM Mail m ORDER BY m.name"),
    @NamedQuery(name="Mail.count",query="SELECT COUNT(m) FROM Mail m"),
})
public class Mail implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = 7996282373984678899L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String name;
    
    @NotNull
    private String subject;
    
    @NotNull
    @Column(name="fromEmail")
    private String from;

    @ManyToMany(mappedBy = "mails",cascade = CascadeType.MERGE)
    private Set<Campaign> campaigns;
    
    @Lob
    @NotNull
    private String body_text;
    

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

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody_text() {
        return body_text;
    }

    public void setBody_text(String body_text) {
        this.body_text = body_text;
    }

    @XmlTransient
    public Set<Campaign> getCampaigns() {
        return campaigns;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Mail)) {
            return false;
        }
        Mail other = (Mail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.Emails[ id=" + id + " ]";
    }
    
}
