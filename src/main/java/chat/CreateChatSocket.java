package chat;

import context.Context;
import dbService.DBService;
import dbService.dao.ChatDAO;
import dbService.datasets.ChatDataSet;
import dbService.datasets.UserDataSet;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.json.*;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@WebSocket
public class CreateChatSocket {
    private Session session;

    private Context context;

    public CreateChatSocket(Context context) {
        this.context = context;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
    }

    @OnWebSocketMessage
    public void onQuery(String query) {
        try (StringReader stringReader = new StringReader(query);
             JsonReader jsonReader = Json.createReader(stringReader)
        ) {


                JsonObject object = jsonReader.readObject();
                String type = object.getString("type");
                JsonObjectBuilder responseObjectBuilder = Json.createObjectBuilder();
                if ("search".equals(type)) {
                    List<UserDataSet> users = context.getDbService().searchForUser(object.getString("query"));
                    JsonArrayBuilder loginsBuilder = Json.createArrayBuilder();
                    users.stream()
                            .map(UserDataSet::getLogin)
                            .forEach(loginsBuilder::add);
                    responseObjectBuilder
                            .add("type", "searchResult")
                            .add("logins", loginsBuilder.build());
                } else if ("createChat".equals(type)) {
                    String name = object.getString("name");
                    JsonArray memberArray = object.getJsonArray("members");
                    int memberNumber = object.getInt("memberNumber");
                    Set<UserDataSet> members = new HashSet<>();
                    DBService dbService = context.getDbService();
                    for (int i = 0; i < memberNumber; i++) {
                        members.add(dbService.getUser(memberArray.getString(i)));
                    }
                    // check if that the chat is unique
                    // TODO more verbose explanation
                    String status = "ok";
                    ChatDataSet newChat = new ChatDataSet(new Date(), name);
                    ChatDAO.AddResult addResult = dbService.addUsersToChat(newChat, members);
                    if (addResult.getOutcome() == ChatDAO.Outcome.EXISTS) {
                        status = "exists";
                    }
                    responseObjectBuilder
                            .add("id", (Integer) addResult.getId())
                            .add("type", "chatCreation")
                            .add("status", status);

                } else throw new AssertionError("unexpected query type " + type);
                sendMessage(responseObjectBuilder.build().toString());
        }

    }

    @OnWebSocketClose
    public void onClose(int status, String reason) {
        session.close();
        System.err.println("status: " + status + ", reason: " + reason);
    }


    private void sendMessage(String message) {
        try {
            session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
