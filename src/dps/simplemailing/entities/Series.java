package dps.simplemailing.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="series")
public class Series implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = -3686003889724883300L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String displayName;
    private Boolean updateSubscribeTime;
    
    @OneToMany(mappedBy = "series")
    private List<SeriesItem> seriesItems;
    
    @OneToMany(mappedBy = "series")
    private List<SeriesSubscription> seriesSubscriptions;
    
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getUpdateSubscribeTime() {
        return updateSubscribeTime;
    }

    public void setUpdateSubscribeTime(Boolean updateSubscribeTime) {
        this.updateSubscribeTime = updateSubscribeTime;
    }

    public List<SeriesItem> getSeriesItems() {
        return seriesItems;
    }
/*
    public void setSeriesItems(List<SeriesItem> seriesItems) {
        this.seriesItems = seriesItems;
    }
*/
    public List<SeriesSubscription> getSeriesSubscriptions() {
        return seriesSubscriptions;
    }
/*
    public void setSeriesSubscriptions(List<SeriesSubscription> seriesSubscriptions) {
        this.seriesSubscriptions = seriesSubscriptions;
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
        if (!(object instanceof Series)) {
            return false;
        }
        Series other = (Series) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.Series[ id=" + id + " ]";
    }

}
