package accounts;

import exceptions.ExistingUserException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class AccountService {

    private static final Map<String, UserProfile> userProfileMap = new ConcurrentSkipListMap<>();

    /**
     *
     * @param login
     * @param password
     * @throws ExistingUserException
     */
    public static void addUser(@NotNull String login, @NotNull String password) {
        if (userProfileMap.putIfAbsent(login, new UserProfile(login, password)) != null)
            throw new ExistingUserException();
    }

    @Nullable
    public static UserProfile getUser(String login) {
        return userProfileMap.get(login);
    }

    public static boolean checkCredentials(@NotNull String login, @NotNull String password) {
        UserProfile userProfile = userProfileMap.get(login);
        return userProfile != null && password.equals(userProfile.getPassword());
    }

}
