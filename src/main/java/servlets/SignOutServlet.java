package servlets;

import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class SignOutServlet extends CommonServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        final HttpSession session = req.getSession();
        synchronized (session) {
            if (!context.getAccountService().signOut(session)) {
                resp.getWriter().print(PageGenerator.getInstance().getPage("static/unauthorized.html"));
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                resp.sendRedirect("/home");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
