package dps.simplemailing.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SeriesSubscription.class)
public abstract class SeriesSubscription_ {

	public static volatile SingularAttribute<SeriesSubscription, Date> subscribeTime;
	public static volatile SingularAttribute<SeriesSubscription, String> extraData;
	public static volatile SingularAttribute<SeriesSubscription, Series> series;
	public static volatile SingularAttribute<SeriesSubscription, Date> lastItemSendTime;
	public static volatile SingularAttribute<SeriesSubscription, Long> id;
	public static volatile SingularAttribute<SeriesSubscription, User> user;
	public static volatile MapAttribute<SeriesSubscription, SeriesItem, SeriesMail> seriesMails;
	public static volatile SingularAttribute<SeriesSubscription, Integer> lastItemOrder;

}

