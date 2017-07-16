package servlets;

import accounts.AccountService;
import org.jetbrains.annotations.Nullable;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class SignInServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print(PageGenerator.instance().getPage("signin.html",
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
        } else if (AccountService.checkCredentials(login, password)) {
            resp.getWriter().print("Authorized: " + login);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.getWriter().print("Unauthorized");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
