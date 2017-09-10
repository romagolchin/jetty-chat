package chat;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class ChatServiceImpl implements ChatService {

    public ChatServiceImpl() {
    }

    private final Map<String, Set<ChatWebSocket>> usersOnline = new ConcurrentHashMap<>();


    @Override
    public void addSocket(@NotNull ChatWebSocket webSocket) {
        synchronized (usersOnline) {
            String chatName = webSocket.getChat();
            Set<ChatWebSocket> chatWebSockets = usersOnline.get(chatName);
            if (chatWebSockets != null)
                chatWebSockets.add(webSocket);
            else chatWebSockets = new HashSet<>();
            usersOnline.put(chatName, chatWebSockets);
        }
    }

    @Override
    public void removeSocket(ChatWebSocket webSocket) {
        synchronized (usersOnline) {
            usersOnline.get(webSocket.getChat()).remove(webSocket);
        }
    }

    @Override
    public void broadcastMessage(String chatName, String message) {
        for (ChatWebSocket socket : usersOnline.get(chatName)) {
            socket.sendMessage(message);
        }
    }
}
