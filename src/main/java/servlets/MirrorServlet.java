package servlets;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class MirrorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String value = req.getParameter("key");
        int status = HttpServletResponse.SC_FORBIDDEN;
        if (value != null) {
            resp.getWriter().print(value);
            status = HttpServletResponse.SC_OK;
        }
        resp.setContentType("text/html;charset=UTF-8");
        resp.setStatus(status);
    }
}
