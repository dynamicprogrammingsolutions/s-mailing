package dps.simplemailing.entities;

import java.io.Serializable;

public class SeriesMailId implements Serializable {
    private SeriesSubscription seriesSubscription;
    private SeriesItem seriesItem;
    public SeriesMailId() {

    }
    public SeriesMailId(SeriesSubscription seriesSubscription, SeriesItem seriesItem) {
        this.seriesSubscription = seriesSubscription;
        this.seriesItem = seriesItem;
    }
    public SeriesSubscription getSeriesSubscription() {
        return seriesSubscription;
    }

    public SeriesItem getSeriesItem() {
        return seriesItem;
    }

    @Override
    public int hashCode() {
        return new Long(seriesSubscription.hashCode()+seriesItem.hashCode()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof SeriesMailId)
                && seriesSubscription.equals(((SeriesMailId)obj).seriesSubscription)
                && seriesItem.equals(((SeriesMailId) obj).seriesItem);
    }
}
