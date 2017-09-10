package accounts;

import dbService.DBService;
import dbService.datasets.ChatDataSet;
import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class DBServiceFake implements DBService {

    private Map<String, UserProfile> userMap = new ConcurrentHashMap<>();

    private Queue<MessageDataSet> messageDataSetQueue = new ArrayBlockingQueue<>(Integer.MAX_VALUE);

    private final Set<ChatDataSet> chatDataSets = ConcurrentHashMap.newKeySet();

    @Override
    public void addUser(@NotNull String login, @NotNull String password) {
        userMap.put(login, new UserProfile(login, password));
    }

    @Override
    public @Nullable UserDataSet getUser(@NotNull String login) {
        UserProfile profile = userMap.get(login);
        return new UserDataSet(profile.getLogin(), profile.getPassword());
    }

    @Override
    public List<UserDataSet> searchForUser(@NotNull String loginPrefix) {
        return null;
    }

    @Override
    public void addMessage(@NotNull MessageDataSet messageDataSet) {
        messageDataSetQueue.add(messageDataSet);
    }

    @Override
    public @NotNull List<MessageDataSet> getAllMessages() {
        return new ArrayList<>(messageDataSetQueue);
    }

    @Override
    public Serializable addChat(@NotNull ChatDataSet chatDataSet, @NotNull Set<UserDataSet> userDataSets) {
        synchronized (chatDataSets) {
            chatDataSets.add(chatDataSet);
            return chatDataSets.size() - 1;
        }
    }

    @Override
    public ChatDataSet getChat(Serializable id) {
        return null;
    }
}
