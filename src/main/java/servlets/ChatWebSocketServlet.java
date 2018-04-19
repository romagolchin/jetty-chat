package servlets;

import chat.ChatWebSocket;
import context.Context;
import dbService.DBService;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@WebServlet(name = "ChatWebSocketServlet", urlPatterns = {"/chat?chat=*"})
public class ChatWebSocketServlet extends WebSocketServlet {

    private Context context;

    public ChatWebSocketServlet() {
        context = Context.getInstance();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        final HttpSession session = req.getSession();
        synchronized (session) {
            if (context.getAccountService().isSignedIn(session)) {
                String html = PageGenerator.getInstance().getPage("chat.html",
                        Collections.singletonMap("chat", req.getParameter("chat")));
                resp.getWriter().print(html);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.getWriter().print(PageGenerator.getInstance().getPage("static/unauthorized.html"));
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
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
                String chatName = beginIndex != -1 && endIndex != -1 ?
                        requestString.substring(beginIndex, endIndex) : null;
                return new ChatWebSocket(context.getChatService(), dbService, dbService.getUser(login),
                        chatName);
            }
        });
    }
}
