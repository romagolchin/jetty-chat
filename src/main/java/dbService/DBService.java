package dbService;

import dbService.dao.ChatDAO;
import dbService.datasets.ChatDataSet;
import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public interface DBService {

    UserDataSet addUser(@NotNull String login, @NotNull String password);

    @Nullable UserDataSet getUser(@NotNull String login);

    List<UserDataSet> searchForUser(@NotNull String loginPrefix);

    void addMessage(@NotNull MessageDataSet messageDataSet);

    @NotNull List<MessageDataSet> getAllMessages();

//    boolean chatExists(@NotNull ChatDataSet chatDataSet);

    /**
     * If chat with such name and members already exists, return null.
     * @param chatDataSet
     * @param userDataSets
     * @return
     */
    ChatDAO.AddResult addUsersToChat(@NotNull ChatDataSet chatDataSet, @NotNull Set<UserDataSet> userDataSets);

    void removeUsersFromChat(@NotNull ChatDataSet chatDataSet, @NotNull Set<UserDataSet> userDataSets);

    ChatDataSet getChat(Serializable id);

    Set<UserDataSet> getUsersInChat(@NotNull ChatDataSet chat);

    Set<ChatDataSet> getChatsOfUser(@NotNull UserDataSet userDataSet);

}
