package dbService.dao;

import dbService.datasets.ChatDataSet;
import dbService.datasets.ChatDataSet_;
import dbService.datasets.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class ChatDAO extends AbstractDAO<ChatDataSet> {
    public enum Outcome {
        OK, EXISTS
    }

    public static class AddResult {
        public AddResult(Outcome outcome, Serializable id) {
            this.outcome = outcome;
            this.id = id;
        }

        private final Outcome outcome;

        private final Serializable id;

        public Outcome getOutcome() {
            return outcome;
        }

        public Serializable getId() {
            return id;
        }
    }

    public ChatDAO(@NotNull SessionFactory factory) {
        super(factory);
        dataSetClass = ChatDataSet.class;
    }


    public AddResult addUsers(@NotNull ChatDataSet chatDataSet, Set<UserDataSet> userDataSets) {
        synchronized (userDataSets) {
            return functionWithSession(session -> {
//                session.saveOrUpdate(chatDataSet);
//                session.flush();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<ChatDataSet> criteriaQuery = builder.createQuery(ChatDataSet.class);
                Root<ChatDataSet> root = criteriaQuery.from(ChatDataSet.class);
                criteriaQuery.select(root);
                criteriaQuery.where(builder.equal(root.get(ChatDataSet_.name), chatDataSet.getName())
//                    , builder.equal(root.get(ChatDataSet_.users), chatDataSet.getUsers())
                );
                Outcome outcome = Outcome.OK;
                int id;
                List<ChatDataSet> resultList = session.createQuery(criteriaQuery).getResultList();
                if (resultList.stream().anyMatch(r -> !r.getId().equals(chatDataSet.getId()))) {
                    outcome = Outcome.EXISTS;
                    id = resultList.get(0).getId();
                } else {
                    session.saveOrUpdate(chatDataSet);
                    session.flush();
                    id = chatDataSet.getId();
                    for (UserDataSet userDataSet : userDataSets) {
                        session.update(userDataSet);
                        chatDataSet.addUser(userDataSet);
                    }

                }
                return new AddResult(outcome, id);
            });
        }
    }


    public Serializable removeUsers(@NotNull ChatDataSet chatDataSet, Set<UserDataSet> userDataSets) {
        // BROKEN as Hibernate does not guarantee that in different sessions there is a unique instance corresponding to a row
//        synchronized (chatDataSet) {
            // BROKEN as we could take two joint sets, they are obviously not the same object
            synchronized (userDataSets) {
                return functionWithSession(session -> {
                    System.out.println("removing " + userDataSets.size() + " users " + " in session " + session.hashCode());
//                    session.update(chatDataSet);
                    session.merge(chatDataSet);
//                    session.flush();
                    for (UserDataSet userDataSet : userDataSets) {
                        session.update(userDataSet);
//                        session.merge(userDataSet);
                        chatDataSet.removeUser(userDataSet);
                    }
                    return chatDataSet.getId();
                });
            }
//        }

    }

    public Set<UserDataSet> getUsers(@NotNull ChatDataSet chat) {
        try (Session session = factory.openSession()) {
            session.update(chat);
            return new HashSet<>(chat.getUsers());
        }
    }

}
