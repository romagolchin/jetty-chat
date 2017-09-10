package dbService.dao;

import dbService.DBException;
import dbService.datasets.DataSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public abstract class AbstractDAO<T extends DataSet> {
    protected final @NotNull SessionFactory factory;

    protected Class<T> dataSetClass;

    public AbstractDAO(@NotNull SessionFactory factory) {
        this.factory = factory;
    }

    <S> S functionWithSession(Function<Session, S> fun) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            S value = fun.apply(session);
            try {
                transaction.commit();
                return value;
            } catch (RollbackException e) {
                transaction.rollback();
                throw new DBException(e);
            }
        } catch (PersistenceException | IllegalStateException | IllegalArgumentException e) {
            throw new DBException(e);
        }
    }

    void actionWithSession(Consumer<Session> consumer) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            consumer.accept(session);
            try {
                transaction.commit();
            } catch (RollbackException e) {
                transaction.rollback();
                throw new DBException(e);
            }
        } catch (PersistenceException | IllegalStateException | IllegalArgumentException e) {
            // if commit was called on inactive transaction
            throw new DBException(e);
        }
    }

    public Serializable save(@NotNull DataSet dataSet) throws DBException {
        return functionWithSession(session -> session.save(dataSet));
    }

    public T load(Serializable id) {
        synchronized (factory) {
            try (Session session = factory.openSession()) {
                return session.get(dataSetClass, id);
            } catch (PersistenceException e) {
                throw new DBException(e);
            }
        }
    }

    public List<T> getAll() throws DBException {
        try (Session session = factory.openSession()) {
            CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(dataSetClass);
            criteria.select(criteria.from(dataSetClass));
            return session.createQuery(criteria).getResultList();
        } catch (IllegalArgumentException e) {
            // illegal selection or criteria
            throw new DBException(e);
        }
    }
}
