package servlets;

import context.Context;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class CommonServlet extends HttpServlet {
    protected static Context context = Context.getInstance();

    protected static void servePageByUri(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            URI uri = new URI(req.getRequestURI());
            String path = uri.getPath();
            String content = PageGenerator.getInstance().getPage(path.substring(1) + ".html");
            resp.getWriter().write(content);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    protected static void serveStaticPage(HttpServletResponse resp, String fileName, int status) throws IOException {
        String content = PageGenerator.getInstance().getPage(fileName);
        resp.getWriter().write(content);
        resp.setStatus(status);
    }

}
