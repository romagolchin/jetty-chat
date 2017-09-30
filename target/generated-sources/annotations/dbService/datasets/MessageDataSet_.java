package dbService.datasets;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MessageDataSet.class)
public abstract class MessageDataSet_ {

	public static volatile SingularAttribute<MessageDataSet, Long> id;
	public static volatile SingularAttribute<MessageDataSet, String> text;
	public static volatile SingularAttribute<MessageDataSet, UserDataSet> user;
	public static volatile SingularAttribute<MessageDataSet, Timestamp> timestamp;

}

