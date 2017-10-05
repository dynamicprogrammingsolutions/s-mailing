package dps.simplemailing.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GeneratedMail.class)
public abstract class GeneratedMail_ {

	public static volatile SingularAttribute<GeneratedMail, String> subject;
	public static volatile SingularAttribute<GeneratedMail, QueuedMail> queuedMail;
	public static volatile SingularAttribute<GeneratedMail, Long> id;
	public static volatile SingularAttribute<GeneratedMail, String> body;
	public static volatile SingularAttribute<GeneratedMail, String> toEmail;
	public static volatile SingularAttribute<GeneratedMail, String> fromEmail;

}

