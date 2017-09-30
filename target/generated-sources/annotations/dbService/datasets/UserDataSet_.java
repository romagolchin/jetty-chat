package dbService.datasets;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserDataSet.class)
public abstract class UserDataSet_ {

	public static volatile SingularAttribute<UserDataSet, String> password;
	public static volatile SetAttribute<UserDataSet, ChatDataSet> chats;
	public static volatile SingularAttribute<UserDataSet, Integer> id;
	public static volatile SingularAttribute<UserDataSet, String> login;

}

