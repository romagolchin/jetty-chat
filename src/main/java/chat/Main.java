package chat;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import servlets.*;

import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(ChatWebSocketServlet.class, "/chatSocket");
        contextHandler.addServlet(HomeServlet.class, "/home");
        contextHandler.addServlet(ChatServlet.class, "/chat");
        contextHandler.addServlet(new ServletHolder(new SignInServlet()), "/signin");
        contextHandler.addServlet(new ServletHolder(new SignOutServlet()), "/signout");
        contextHandler.addServlet(new ServletHolder(new SignUpServlet()), "/signup");
        contextHandler.addServlet(AllRequestsServlet.class, "/*");
//        contextHandler.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
//        contextHandler.setWelcomeFiles(new String[]{"welcome.html"});

//        System.out.println(Stream.of(contextHandler.getWelcomeFiles()).collect(Collectors.joining(", ", "[", "]")));

        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.setUsingCookies(false);


        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        URL resourcesUrl = Main.class.getResource("/public_html/static");
        resourceHandler.setBaseResource(Resource.newResource(resourcesUrl));

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, contextHandler});

        server.setHandler(handlers);

        System.err.println("Server started");
        server.start();
        server.join();
    }
}
