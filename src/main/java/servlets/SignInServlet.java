package servlets;

import org.jetbrains.annotations.Nullable;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class SignInServlet extends CommonServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print(PageGenerator.getInstance().getPage("signin.html",
                Collections.emptyMap()));
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        @Nullable String login = req.getParameter("login");
        @Nullable String password = req.getParameter("password");
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        if (login == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            final HttpSession session = req.getSession();
            final String logString = "SigInServlet session id ";
            if (session != null) {
                synchronized (session) {
                    System.out.println(logString + session.getId());
                    if (context.getAccountService().signIn(session, login, password)) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.sendRedirect("/chat");
                    } else {
                        resp.getWriter().print(PageGenerator.getInstance().getPage("static/unauthorized.html"));
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                }
            } else System.out.println(logString + null);

        }
    }
}
