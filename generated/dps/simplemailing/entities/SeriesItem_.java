package dps.simplemailing.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SeriesItem.class)
public abstract class SeriesItem_ {

	public static volatile SingularAttribute<SeriesItem, Mail> mail;
	public static volatile SingularAttribute<SeriesItem, Series> series;
	public static volatile SingularAttribute<SeriesItem, Long> id;
	public static volatile ListAttribute<SeriesItem, SeriesMail> seriesMails;
	public static volatile SingularAttribute<SeriesItem, Integer> sendDelay;

}

