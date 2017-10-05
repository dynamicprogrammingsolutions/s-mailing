package dps.simplemailing.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="series_items")
public class SeriesItem implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = -388067527552213335L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="series_id")
    private Series series;
    
    @ManyToOne
    @JoinColumn(name="mail_id")
    private Mail mail;
    
    private int sendDelay;
    
    @OneToMany(mappedBy = "seriesItem",cascade = {CascadeType.REMOVE})
    private List<SeriesMail> seriesMails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public int getSendDelay() {
        return sendDelay;
    }

    public void setSendDelay(int sendDelay) {
        this.sendDelay = sendDelay;
    }

    public List<SeriesMail> getSeriesMails() {
        return seriesMails;
    }
/*
    public void setSeriesMails(List<SeriesMail> seriesMails) {
        this.seriesMails = seriesMails;
    }
*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeriesItem)) {
            return false;
        }
        SeriesItem other = (SeriesItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.SeriesItems[ id=" + id + " ]";
    }
    
}
