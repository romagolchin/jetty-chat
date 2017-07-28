package chat;

import dbService.DBService;
import dbService.datasets.MessageDataSet;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@WebSocket
public class ChatWebSocket {
    private Session session;

    private ChatService chatService;

    private DBService dbService;

    private String login;

    public ChatWebSocket(ChatService chatService, DBService dbService, String login) {
        this.chatService = chatService;
        this.dbService = dbService;
        this.login = login;
    }



    @OnWebSocketConnect
    public void onConnect(Session session) {
        chatService.addSocket(this);
        this.session = session;
        List<MessageDataSet> messageDataSets = dbService.getAllMessages();
        sendMessage(messageDataSets.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        Date date = new Date();
        chatService.broadcastMessage(new MessageDataSet(-1, message, date, login).toString());
        dbService.addMessage(message, new Date(), login);
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
}
