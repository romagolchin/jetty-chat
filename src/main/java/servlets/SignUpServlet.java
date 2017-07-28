package servlets;

import accounts.AccountService;
import exceptions.ExistingUserException;
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
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print(PageGenerator.instance().getPage("signup.html",
                Collections.singletonMap("errorMessage", "")));
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        @Nullable String login = req.getParameter("login");
        @Nullable String password = req.getParameter("password");
        @Nullable String passwordAgain = req.getParameter("passwordAgain");
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        if (login == null || password == null || passwordAgain == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (!passwordAgain.equals(password)) {
            resp.getWriter().print(PageGenerator.instance().getPage("signup.html",
                    Collections.singletonMap("errorMessage", "Passwords do not match")));
        } else {
            try {
                AccountService.addUser(login, password);
            } catch (ExistingUserException e) {
                resp.getWriter().print(PageGenerator.instance().getPage("signup.html",
                        Collections.singletonMap("errorMessage", String.format(ExistingUserException.ERROR_FMT, login))));
            }
            resp.sendRedirect("/");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
