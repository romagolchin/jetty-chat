package servlets;

import chat.ChatWebSocket;
import context.Context;
import dbService.DBService;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@WebServlet(name = "ChatWebSocketServlet", urlPatterns = "/chatSocket?chat=*")
public class ChatWebSocketServlet extends WebSocketServlet {

    private Context context;

    public ChatWebSocketServlet() {
        context = Context.getInstance();
    }


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10_000_000);
        factory.setCreator((req, resp) -> {
            HttpSession session = req.getSession();
            synchronized (session) {
                final String logString = "SocketServlet session id ";
                System.out.println(logString + (session.getId()));
                String login = context.getAccountService().getProfileBySession(session).getLogin();
                System.out.println("session = " + session + "login = " + login);
                final DBService dbService = context.getDbService();
                final String requestString = req.getRequestURI().toString();
                final int beginIndex = requestString.indexOf('?');
                final int endIndex = requestString.indexOf('=');
                return new ChatWebSocket(context.getChatService(), dbService, dbService.getUser(login),
                        beginIndex != -1 && endIndex != -1 ? requestString.substring(beginIndex, endIndex) : null);
            }
        });
    }
}
