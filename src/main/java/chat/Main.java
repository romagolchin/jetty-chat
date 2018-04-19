package chat;

import accounts.AccountService;
import accounts.AccountServiceImpl;
import context.Context;
import dbService.DBServiceImpl;
import dbService.SessionFactoryHolder;
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
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(HomeServlet.class, "/home");
        contextHandler.addServlet(ChatWebSocketServlet.class, "/chat");
        contextHandler.addServlet(SignInServlet.class, "/signin");
        contextHandler.addServlet(SignOutServlet.class, "/signout");
        contextHandler.addServlet(SignUpServlet.class, "/signup");
        contextHandler.addServlet(CreateChatWSServlet.class, "/create_chat");
        contextHandler.addServlet(AllRequestsServlet.class, "/*");
//        contextHandler.addServlet(ChatWebSocketServlet.class, "/chatSocket");
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
        Context context = Context.getInstance();
        SessionFactoryHolder.configure("hibernate.cfg.xml");
        DBServiceImpl dbService = new DBServiceImpl(SessionFactoryHolder.getSessionFactory());
        context.setDbService(dbService);
        context.setAccountService(new AccountServiceImpl(dbService));
        context.setChatService(new ChatServiceImpl());
        server.start();
        server.join();
    }
}
