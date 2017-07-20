package accounts;

import dbService.DBService;
import dbService.DBServiceImpl;
import exceptions.ExistingUserException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class AccountService {

    private static DBService DB_SERVICE;

    public static void setDbService(DBService dbService) {
        DB_SERVICE = dbService;
    }

    /**
     *
     * @param login
     * @param password
     * @throws ExistingUserException
     */
    public static void addUser(@NotNull String login, @NotNull String password) {
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

}
