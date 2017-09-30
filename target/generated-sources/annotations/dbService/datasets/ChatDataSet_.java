package dbService.datasets;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ChatDataSet.class)
public abstract class ChatDataSet_ {

	public static volatile SingularAttribute<ChatDataSet, Timestamp> creationStamp;
	public static volatile SingularAttribute<ChatDataSet, String> name;
	public static volatile SingularAttribute<ChatDataSet, Integer> id;
	public static volatile SetAttribute<ChatDataSet, UserDataSet> users;

}

