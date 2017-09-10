package servlets;

import dbService.datasets.UserDataSet;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class ChatServlet extends CommonServlet {


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
}
