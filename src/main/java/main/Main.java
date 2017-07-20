package main;

import accounts.AccountService;
import dbService.DBService;
import dbService.DBServiceImpl;
import dbService.SessionFactoryHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AllRequestsServlet;
import servlets.MirrorServlet;
import servlets.SignInServlet;
import servlets.SignUpServlet;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class Main {
    public static void main(String[] args) throws Exception {
        AllRequestsServlet allRequestsServlet = new AllRequestsServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new MirrorServlet()), "/mirror");
        context.addServlet(new ServletHolder(new SignUpServlet()), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet()), "/signin");
        context.addServlet(new ServletHolder(allRequestsServlet), "/*");

        Server server = new Server(8080);
        server.setHandler(context);

        System.err.println("Server started");
        DBService service = new DBServiceImpl();
        AccountService.setDbService(service);
        server.start();
//        try {
//            System.out.println(service.addUser("test", "fest"));
//            System.out.println(service.addUser("test", "fest1"));
//            System.out.println(service.getUser("test"));
//            System.out.println(service.getUser("test2"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        server.join();
        SessionFactoryHolder.closeSessionFactory();
    }
}
