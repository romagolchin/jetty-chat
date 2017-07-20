package dbService;

import accounts.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public interface DBService {

    public @Nullable Serializable addUser(@NotNull String login, @NotNull String password);

    public @Nullable UserProfile getUser(@NotNull String login);
}
