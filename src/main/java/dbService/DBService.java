package dbService;

import accounts.UserProfile;
import dbService.datasets.MessageDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public interface DBService {

    public @Nullable Serializable addUser(@NotNull String login, @NotNull String password);

    public @Nullable UserProfile getUser(@NotNull String login);

    public void addMessage(@NotNull String message, @NotNull Date date, @NotNull String user);

    public @NotNull List<MessageDataSet> getAllMessages();

}
