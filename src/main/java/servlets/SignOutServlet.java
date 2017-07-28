package servlets;

import accounts.AccountService;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class SignOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Cookie[] cookies = req.getCookies();
//        Cookie loginCookie = null;
//        for (Cookie cookie : cookies) {
//            if ("user".equals(cookie.getName()))
//                loginCookie = cookie;
//        }
//        if (loginCookie != null) {
//            loginCookie.setMaxAge(0);
//            resp.addCookie(loginCookie);
//        }
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        if (!AccountService.singOutUser(req.getSession())) {
            resp.getWriter().print(PageGenerator.instance().getPage("unauthorized.html"));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            resp.sendRedirect("/");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
