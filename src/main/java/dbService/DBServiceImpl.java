package dbService;

import accounts.UserProfile;
import dbService.dao.MessageDAO;
import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import exceptions.ExistingUserException;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import dbService.dao.UserDAO;
import org.jetbrains.annotations.Nullable;


/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class DBServiceImpl implements DBService {
    private final SessionFactory sessionFactory;

    private DBServiceImpl() {
        sessionFactory = SessionFactoryHolder.getSessionFactory();
    }

    private static DBServiceImpl service = new DBServiceImpl();

    public static DBServiceImpl getInstance() {
        return service;
    }

    public @Nullable Serializable addUser(@NotNull String login, @NotNull String password) {
        UserDAO dao = new UserDAO(sessionFactory);
        if (dao.getUser(login) != null)
            throw new ExistingUserException("login  = " + login);
        return dao.save(new UserDataSet(-1, login, password));
    }

    public @Nullable UserProfile getUser(@NotNull String login) {
        UserDataSet dataSet = new UserDAO(sessionFactory).getUser(login);
        return dataSet == null ? null : new UserProfile(dataSet.getLogin(), dataSet.getPassword());
    }

    @Override
    public void addMessage(@NotNull String message, @NotNull Date date, @NotNull String user) {
        new MessageDAO(sessionFactory).save(new MessageDataSet(-1, message, date, user));
    }

    @Override
    public @NotNull List<MessageDataSet> getAllMessages() {
        return new MessageDAO(sessionFactory).getAll();
    }
}
