package chat;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class ChatServiceImpl implements ChatService {
    public ChatServiceImpl() {
    }

    private final Set<ChatWebSocket> sockets = ConcurrentHashMap.newKeySet();

    @Override
    public void addSocket(ChatWebSocket webSocket) {
        sockets.add(webSocket);
    }

    @Override
    public void removeSocket(ChatWebSocket webSocket) {
        sockets.remove(webSocket);
    }

    @Override
    public void broadcastMessage(String message) {
        for (ChatWebSocket socket : sockets) {
            socket.sendMessage(message);
        }
    }
}
