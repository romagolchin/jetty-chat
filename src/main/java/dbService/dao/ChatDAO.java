package dbService.dao;

import dbService.datasets.ChatDataSet;
import dbService.datasets.UserDataSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class ChatDAO extends AbstractDAO<ChatDataSet> {
    public ChatDAO(@NotNull SessionFactory factory) {
        super(factory);
        dataSetClass = ChatDataSet.class;
    }

    public Serializable addUsers(@NotNull ChatDataSet chatDataSet, Set<UserDataSet> userDataSets) {
        synchronized (userDataSets) {
            return functionWithSession(session -> {
                session.saveOrUpdate(chatDataSet);
                session.flush();
                for (UserDataSet userDataSet : userDataSets) {
                    session.update(userDataSet);
                    chatDataSet.addUser(userDataSet);
                }
                return chatDataSet.getId();
            });
        }
    }


    public Serializable removeUsers(@NotNull ChatDataSet chatDataSet, Set<UserDataSet> userDataSets) {
        // BROKEN as Hibernate does not guarantee that in different sessions that there is a unique instance corresponding to a row
        synchronized (chatDataSet) {
            // BROKEN as we could take two joint sets, they are obviously not the same object
            synchronized (userDataSets) {
                return functionWithSession(session -> {
                    System.out.println("removing " + userDataSets.size() + " users " + " in session " + session.hashCode());
                    session.update(chatDataSet);
                    session.flush();
                    for (UserDataSet userDataSet : userDataSets) {
                        session.update(userDataSet);
                        chatDataSet.removeUser(userDataSet);
                    }
                    return chatDataSet.getId();
                });
            }
        }

    }

    public Set<UserDataSet> getUsers(@NotNull ChatDataSet chat) {
        try (Session session = factory.openSession()) {
            session.update(chat);
            return new HashSet<>(chat.getUsers());
        }
    }

}
