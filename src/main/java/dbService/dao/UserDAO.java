package dbService.dao;

import dbService.datasets.UserDataSet;
import dbService.datasets.UserDataSet_;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;


/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class UserDAO extends AbstractDAO<UserDataSet> {

    public UserDAO(@NotNull SessionFactory factory) {
        super(factory);
        dataSetClass = UserDataSet.class;
    }

    public @Nullable UserDataSet getUser(@NotNull String login) {
        try (Session session = factory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
            Root<UserDataSet> root = criteria.from(UserDataSet.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get(UserDataSet_.login), login));
            return session.createQuery(criteria).uniqueResult();
        }
    }

}
