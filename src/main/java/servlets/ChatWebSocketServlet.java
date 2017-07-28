package servlets;

import accounts.AccountService;
import chat.ChatService;
import chat.ChatServiceImpl;
import chat.ChatWebSocket;
import dbService.DBService;
import dbService.DBServiceImpl;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import java.net.HttpCookie;
import java.util.List;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@WebServlet(name = "ChatWebSocketServlet", urlPatterns = "/chatSocket")
public class ChatWebSocketServlet extends WebSocketServlet {
    private static ChatService chatService = new ChatServiceImpl();
    private static DBService dbService = DBServiceImpl.getInstance();



    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10_000_000);
        factory.setCreator((req, resp) -> {
            HttpSession session = req.getSession();
            final String logString = "SocketServlet session id ";
            if (session != null) {
                System.out.println(logString + session.getId());
            } else System.out.println(logString + null);
            String login = AccountService.getProfileBySession(session).getLogin();
            System.out.println("session = " + session + "login = " + login);
//            String login = null;
//            List<HttpCookie> cookies = req.getCookies();
//            for (HttpCookie cookie : cookies) {
//                if ("user".equals(cookie.getName())) {
//                    login = cookie.getValue();
//                    break;
//                }
//            }
//            System.out.println("login = " + login);
            return new ChatWebSocket(chatService, dbService, login);
        });
    }
}
