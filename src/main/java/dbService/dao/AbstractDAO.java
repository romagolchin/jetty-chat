package dbService.dao;

import dbService.datasets.DataSet;
import dbService.datasets.UserDataSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public abstract  class  AbstractDAO <T extends DataSet> {
    protected final @NotNull SessionFactory factory;

    protected Class<T> dataSetClass;

    public AbstractDAO(@NotNull SessionFactory factory) {
        this.factory = factory;
    }

    public Serializable save(@NotNull DataSet dataSet) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Serializable id = session.save(dataSet);
            transaction.commit();
            return id;
        }
    }

    public T load(long id) {
        try (Session session = factory.openSession()) {
            return session.get(dataSetClass, id);
        }
    }

    public List<T> getAll() {
        try (Session session = factory.openSession()) {
            CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(dataSetClass);
            criteria.select(criteria.from(dataSetClass));
            return session.createQuery(criteria).getResultList();
        }
    }
}
