package dps.simplemailing.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="series_mails")
public class SeriesMail implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="seriesSubscription_id")
    private SeriesSubscription seriesSubscription;
    
    @ManyToOne
    @JoinColumn(name="seriesItem_id")
    private SeriesItem seriesItem;
    
    private Status status;
    
    public enum Status {
        unsent, sent
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SeriesSubscription getSeriesSubscription() {
        return seriesSubscription;
    }

    public void setSeriesSubscription(SeriesSubscription seriesSubscription) {
        this.seriesSubscription = seriesSubscription;
    }

    public SeriesItem getSeriesItem() {
        return seriesItem;
    }

    public void setSeriesItem(SeriesItem seriesItem) {
        this.seriesItem = seriesItem;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeriesMail)) {
            return false;
        }
        SeriesMail other = (SeriesMail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.SeriesMail[ id=" + id + " ]";
    }
    
}
