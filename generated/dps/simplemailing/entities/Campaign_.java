package dps.simplemailing.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Campaign.class)
public abstract class Campaign_ {

	public static volatile SetAttribute<Campaign, Mail> mails;
	public static volatile SingularAttribute<Campaign, String> name;
	public static volatile SingularAttribute<Campaign, Long> id;
	public static volatile SetAttribute<Campaign, User> unsubscribedUsers;
	public static volatile SingularAttribute<Campaign, String> longName;

}

