package servlets;

import accounts.AccountService;
import org.eclipse.jetty.servlet.DefaultServlet;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class HomeServlet extends DefaultServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String hide = "display: none";
        Map<String, Object> map = new HashMap<>();
        if (AccountService.getProfileBySession(req.getSession()) != null) {
            map.put("displaySignIn", hide);
            map.put("displaySignOut", "");
            map.put("displaySignUp", hide);
        } else {
            map.put("displaySignIn", "");
            map.put("displaySignOut", hide);
            map.put("displaySignUp", "");
        }
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        resp.getWriter().println(PageGenerator.instance().getPage("home.html", map));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
