package dps.simplemailing.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author ferenci84
 */
@Entity
@Table(name="series_mails")
@IdClass(SeriesMailId.class)
public class SeriesMail implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    @Id
    @Column(name="seriesSubscription_id")
    private Long seriesSubscriptionId;

    @Id
    @Column(name="seriesItem_id")
    private Long seriesItemId;
    */

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*/

    @Id
    @ManyToOne
    @JoinColumn(name="seriesSubscription_id")
    private SeriesSubscription seriesSubscription;

    @Id
    @ManyToOne
    @JoinColumn(name="seriesItem_id")
    private SeriesItem seriesItem;

    private Status status;

    private Date sentTime;

    public SeriesMail() {

    }

    public SeriesMail(SeriesSubscription seriesSubscription, SeriesItem seriesItem) {
        this.seriesSubscription = seriesSubscription;
        this.seriesItem = seriesItem;
        this.status = Status.unsent;
    }

    public enum Status {
        unsent, sent, canceled
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public SeriesSubscription getSeriesSubscription() {
        return seriesSubscription;
    }

    public SeriesItem getSeriesItem() {
        return seriesItem;
    }

    public Status getStatus() {
        return status;
    }

    public void setSeriesSubscription(SeriesSubscription seriesSubscription) {
        this.seriesSubscription = seriesSubscription;
    }

    public void setSeriesItem(SeriesItem seriesItem) {
        this.seriesItem = seriesItem;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
