package servlets;

import accounts.AccountService;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class ChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        if (AccountService.isSignedIn(req.getSession())) {
            String html = PageGenerator.instance().getPage("chat.html");
            resp.getWriter().print(html);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.getWriter().print(PageGenerator.instance().getPage("unauthorized.html"));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
