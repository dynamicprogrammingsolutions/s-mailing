package dps.simplemailing.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.persistence.*;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="series_subscriptions")
@NamedQueries({
        @NamedQuery(name="SeriesSubscription.getSubscription",query = "SELECT u FROM SeriesSubscription u WHERE u.user = :user AND u.series = :series")
})
public class SeriesSubscription implements Serializable, EntityBase<Long> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="series_id")
    private Series series;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    private Date subscribeTime;
    
    @Lob
    private String extraData;

    @OneToMany(mappedBy = "seriesSubscription")
    @MapKey(name="seriesItem")
    private Map<SeriesItem,SeriesMail> seriesMails;
    
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<SeriesItem,SeriesMail> getSeriesMails() {
        return seriesMails;
    }
/*
    public void setSeriesMails(List<SeriesMail> seriesMails) {
        this.seriesMails = seriesMails;
    }
*/

    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
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
        if (!(object instanceof SeriesSubscription)) {
            return false;
        }
        SeriesSubscription other = (SeriesSubscription) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dps.simplemailing.entities.SeriesSubscription[ id=" + id + " ]";
    }
    
}
