package chat;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public interface ChatService {
    /**
     * Add a socket to listen to messages
     * @param webSocket
     */
    void addSocket(ChatWebSocket webSocket);

    void removeSocket(ChatWebSocket webSocket);

    /**
     * Send message to all added sockets in a chat
     * @param message
     */
    void broadcastMessage(String chatName, String message);
}
