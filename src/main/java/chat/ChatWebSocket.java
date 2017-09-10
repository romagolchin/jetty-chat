package chat;

import dbService.DBService;
import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@WebSocket
public class ChatWebSocket {
    private static AtomicInteger cnt = new AtomicInteger(0);

    private int id;

    private Session session;

    private ChatService chatService;

    private DBService dbService;

    private UserDataSet user;

    private String chat;

    public ChatWebSocket(ChatService chatService, DBService dbService, UserDataSet user, String chat) {
        this.chatService = chatService;
        this.dbService = dbService;
        this.user = user;
        this.chat = chat;
        id = cnt.incrementAndGet();
    }

    public String getChat() {
        return chat;
    }



    @OnWebSocketConnect
    public void onConnect(Session session) {
        chatService.addSocket(this);
        this.session = session;
        List<MessageDataSet> messageDataSets = dbService.getAllMessages();
        sendMessage(messageDataSets.stream().map(MessageDataSet::toString).collect(Collectors.joining("\n")));
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        Date date = new Date();
        final MessageDataSet messageDataSet = new MessageDataSet(message, date, user);
        chatService.broadcastMessage(chat, messageDataSet.toString());
        dbService.addMessage(messageDataSet);
    }

    @OnWebSocketClose
    public void onClose(int status, String reason) {
        chatService.removeSocket(this);
        System.out.println(String.format("status: %d, reason: %s", status, reason));
        session.close();
    }


    void sendMessage(String message) {
        try {
            session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatWebSocket socket = (ChatWebSocket) o;
        return id == socket.id &&
                Objects.equals(user, socket.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
