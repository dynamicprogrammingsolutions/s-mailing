package dps.simplemailing.entities;

import dps.simplemailing.entities.SeriesMail.Status;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SeriesMail.class)
public abstract class SeriesMail_ {

	public static volatile SingularAttribute<SeriesMail, SeriesItem> seriesItem;
	public static volatile SingularAttribute<SeriesMail, SeriesSubscription> seriesSubscription;
	public static volatile SingularAttribute<SeriesMail, Long> id;
	public static volatile SingularAttribute<SeriesMail, Status> status;

}

