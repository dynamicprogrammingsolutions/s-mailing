package dps.simplemailing.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="series")
@NamedQueries({
        @NamedQuery(name="Series.getAll",query="SELECT m FROM Series m ORDER BY m.name"),
        @NamedQuery(name="Series.count",query="SELECT COUNT(m) FROM Series m"),
        @NamedQuery(name="Series.getByName",query = "SELECT u FROM Series u WHERE u.name = :name")
})
public class Series implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = -3686003889724883300L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String displayName;

    private Boolean updateSubscribeTime = false;
    
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

    public List<SeriesSubscription> getSeriesSubscriptions() {
        return seriesSubscriptions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
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
