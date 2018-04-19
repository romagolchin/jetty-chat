package servlets;

import accounts.AccountService;
import chat.CreateChatSocket;
import context.Context;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static servlets.CommonServlet.servePageByUri;
import static servlets.CommonServlet.serveStaticPage;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
//@WebServlet(name = "CreateChatWSServlet", urlPatterns = {"/createChatSocket"})
public class CreateChatWSServlet extends WebSocketServlet {
    private Context context;

    public CreateChatWSServlet() {
        context = Context.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountService accountService = context.getAccountService();
        HttpSession session = req.getSession();
        if (accountService.isSignedIn(session)) {
            resp.addCookie(new Cookie("login", accountService.getProfileBySession(session).getLogin()));
            servePageByUri(req, resp);
        } else {
            serveStaticPage(resp, "static/unauthorized.html", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10_000_000);
        factory.setCreator((req, resp) ->
              new CreateChatSocket(context)
            );
    }
}
