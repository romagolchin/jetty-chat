package accounts;

import dbService.DBService;
import dbService.DBServiceImpl;
import dbService.datasets.UserDataSet;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class AccountServiceImpl implements AccountService {

    private final DBService dbService;

    private final Map<HttpSession, UserProfile> sessionMap;


    public AccountServiceImpl(DBService dbService) {
        sessionMap = new ConcurrentHashMap<>();
        this.dbService = dbService;
    }

    public void signUp(@NotNull String login, @NotNull String password) {
        if (login.isEmpty())
            throw new IllegalArgumentException("login must not be empty");
        if (password.isEmpty())
            throw new IllegalArgumentException("password must not be empty");
        if (doesUserExist(login))
            throw new ExistingUserException(login);
        dbService.addUser(login, password);
    }

    @Override
    public boolean doesUserExist(@NotNull String login) {
        return dbService.getUser(login) != null;
    }

    private boolean checkCredentials(@NotNull String login, @NotNull String password) {
        UserDataSet userDataSet = dbService.getUser(login);
        if (userDataSet == null)
            return false;
        UserProfile userProfile = new UserProfile(userDataSet.getLogin(), userDataSet.getPassword());
        return password.equals(userProfile.getPassword());
    }

    public boolean isSignedIn(@NotNull HttpSession session) {
        return sessionMap.containsKey(session);
    }

    public boolean signIn(@NotNull HttpSession session, @NotNull String login, @NotNull String password) {
        try {
            // to check if session is valid
            session.getCreationTime();
            System.err.println("sign in");
            System.err.println(Thread.currentThread().getName());
            boolean ret = checkCredentials(login, password);
            if (ret) sessionMap.putIfAbsent(session, new UserProfile(login, password));
            return ret;
        } catch (IllegalStateException e) {
            return false;
        }

    }

    public boolean signOut(@NotNull HttpSession session) {
        try {
            session.getCreationTime();
            System.err.println("sign out");
            System.err.println(Thread.currentThread().getName());

            boolean ret;
            ret = sessionMap.remove(session) != null;
            if (ret) {
                session.invalidate();
            }
            return ret;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public UserProfile getProfileBySession(@NotNull HttpSession session) {
        return sessionMap.get(session);
    }

}
