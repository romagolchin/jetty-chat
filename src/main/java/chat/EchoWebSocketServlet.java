package chat;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@WebServlet(name = "EchoWebSocketServlet", urlPatterns = "/chat")
public class EchoWebSocketServlet extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10_000_000);
        factory.setCreator((req, resp) -> new EchoWebSocket());
    }
}
