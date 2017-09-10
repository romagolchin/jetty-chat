package dbService.dao;

import dbService.DBException;
import dbService.datasets.ChatDataSet;
import dbService.datasets.UserDataSet;
import dbService.datasets.UserDataSet_;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
            return session.byNaturalId(dataSetClass).using("login", login).load();
        }
    }

    public @NotNull List<UserDataSet> searchForUser(@NotNull String loginPrefix) {
        try (Session session = factory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserDataSet> criteria = builder.createQuery(dataSetClass);
            Root<UserDataSet> root = criteria.from(dataSetClass);
            criteria.select(root);
            criteria.where(builder.like(root.get(UserDataSet_.login), loginPrefix + "%"));
            return session.createQuery(criteria).getResultList();
        }
    }

    public @NotNull Set<ChatDataSet> getChats(@NotNull UserDataSet userDataSet) {
        try (Session session = factory.openSession()) {
            session.update(userDataSet);
            return new HashSet<>(userDataSet.getChats());
        } catch (PersistenceException e) {
            throw new DBException(e);
        }
    }

}
