package servlets;

import accounts.ExistingUserException;
import org.jetbrains.annotations.Nullable;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class SignUpServlet extends CommonServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print(PageGenerator.getInstance().getPage("signup.html",
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
            resp.getWriter().print(PageGenerator.getInstance().getPage("signup.html",
                    Collections.singletonMap("errorMessage", "Passwords do not match")));
        } else {
            try {
                context.getAccountService().signUp(login, password);
                resp.sendRedirect("/");
            } catch (ExistingUserException e) {
                resp.getWriter().print(PageGenerator.getInstance().getPage("signup.html",
                        Collections.singletonMap("errorMessage", e.getMessage())));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
