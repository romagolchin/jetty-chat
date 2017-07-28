package accounts;

import dbService.DBService;
import dbService.DBServiceImpl;
import exceptions.ExistingUserException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class AccountService {

    private static DBService DB_SERVICE = DBServiceImpl.getInstance();

    private static final Map<HttpSession, UserProfile> sessionMap = new ConcurrentHashMap<>();

    /**
     * @param login
     * @param password
     * @throws ExistingUserException
     */
    public static void addUser(@NotNull String login, @NotNull String password) {
        System.out.println(login + " " + password);
        DB_SERVICE.addUser(login, password);
    }

    @Nullable
    public static UserProfile getUser(String login) {
        return DB_SERVICE.getUser(login);
    }

    public static boolean checkCredentials(@NotNull String login, @NotNull String password) {
        UserProfile userProfile = DB_SERVICE.getUser(login);
        return userProfile != null && password.equals(userProfile.getPassword());
    }

    public static boolean isSignedIn(HttpSession session) {
        return getProfileBySession(session) != null;
    }

    public static void signInUser(HttpSession session, UserProfile profile) {
        sessionMap.put(session, profile);
    }

    public static boolean singOutUser(HttpSession session) {
        // the commented code signs user out from all sessions, which is not usually needed
//        UserProfile profile = sessionMap.get(session);
//        boolean ret = false;
//        for (HttpSession hs : sessionMap.keySet()) {
//            if (sessionMap.get(hs).equals(profile)) {
//                sessionMap.remove(hs);
//                ret = true;
//            }
//        }
//        return ret;
        boolean ret = sessionMap.containsKey(session);
        sessionMap.remove(session);
        session.invalidate();
        return ret;
    }

    public static UserProfile getProfileBySession(HttpSession session) {
        return sessionMap.get(session);
    }

}
