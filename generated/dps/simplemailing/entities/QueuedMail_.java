package dps.simplemailing.entities;

import dps.simplemailing.entities.QueuedMail.Status;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QueuedMail.class)
public abstract class QueuedMail_ {

	public static volatile SingularAttribute<QueuedMail, Date> scheduledTime;
	public static volatile SingularAttribute<QueuedMail, Mail> mail;
	public static volatile SingularAttribute<QueuedMail, Date> sentTime;
	public static volatile SingularAttribute<QueuedMail, Long> id;
	public static volatile SingularAttribute<QueuedMail, User> user;
	public static volatile SingularAttribute<QueuedMail, GeneratedMail> generatedMail;
	public static volatile SingularAttribute<QueuedMail, Status> status;

}

