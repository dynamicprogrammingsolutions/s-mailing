package dps.simplemailing.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Series.class)
public abstract class Series_ {

	public static volatile ListAttribute<Series, SeriesItem> seriesItems;
	public static volatile SingularAttribute<Series, String> displayName;
	public static volatile SingularAttribute<Series, String> name;
	public static volatile ListAttribute<Series, SeriesSubscription> seriesSubscriptions;
	public static volatile SingularAttribute<Series, Long> id;
	public static volatile SingularAttribute<Series, Boolean> updateSubscribeTime;

}

