package dbService;

import dbService.dao.ChatDAO;
import dbService.dao.MessageDAO;
import dbService.datasets.ChatDataSet;
import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import dbService.dao.UserDAO;
import org.jetbrains.annotations.Nullable;


/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class DBServiceImpl implements DBService {
    private final SessionFactory sessionFactory;

    public DBServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void addUser(@NotNull String login, @NotNull String password) {
        UserDAO dao = new UserDAO(sessionFactory);
        dao.save(new UserDataSet(login, password));
    }

    public @Nullable UserDataSet getUser(@NotNull String login) {
        return new UserDAO(sessionFactory).getUser(login);
    }

    @Override
    public List<UserDataSet> searchForUser(@NotNull String loginPrefix) {
        UserDAO dao = new UserDAO(sessionFactory);
        return dao.searchForUser(loginPrefix);
    }


    @Override
    public void addMessage(@NotNull MessageDataSet messageDataSet) {
        new MessageDAO(sessionFactory).save(messageDataSet);
    }

    @Override
    public @NotNull List<MessageDataSet> getAllMessages() {
        return new MessageDAO(sessionFactory).getAll();
    }

    @Override
    public Serializable addUsersToChat(@NotNull ChatDataSet chatDataSet, @NotNull Set<UserDataSet> userDataSets) {
        return new ChatDAO(sessionFactory).addUsers(chatDataSet, userDataSets);
    }

    @Override
    public ChatDataSet getChat(Serializable id) {
        return new ChatDAO(sessionFactory).load(id);
    }

    @Override
    public Set<UserDataSet> getUsersInChat(@NotNull Serializable chatId) {
        return new ChatDAO(sessionFactory).getUsers(chatId);
    }

    @Override
    public Set<ChatDataSet> getChatsOfUser(@NotNull UserDataSet userDataSet) {
        return new UserDAO(sessionFactory).getChats(userDataSet);
    }


}
