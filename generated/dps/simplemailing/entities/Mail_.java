package dps.simplemailing.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Mail.class)
public abstract class Mail_ {

	public static volatile SetAttribute<Mail, Campaign> campaigns;
	public static volatile SingularAttribute<Mail, String> body_text;
	public static volatile SingularAttribute<Mail, String> subject;
	public static volatile SingularAttribute<Mail, String> name;
	public static volatile SingularAttribute<Mail, String> from;
	public static volatile SingularAttribute<Mail, Long> id;

}

