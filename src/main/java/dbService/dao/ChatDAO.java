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
        return functionWithSession(session -> {
            System.out.println("session " + session.hashCode());
            session.saveOrUpdate(chatDataSet);
            session.flush();
            for (UserDataSet userDataSet : userDataSets) {
                session.update(userDataSet);
                chatDataSet.addUser(userDataSet);
            }
            return chatDataSet.getId();
        });
    }


    public Set<UserDataSet> getUsers(@NotNull Serializable chatId) {
        try (Session session = factory.openSession()) {
            return new HashSet<>(session.get(dataSetClass, chatId).getUsers());
        }
    }

}
