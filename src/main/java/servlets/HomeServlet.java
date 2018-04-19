package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBService;
import dbService.datasets.ChatDataSet;
import dbService.datasets.UserDataSet;
import templater.PageGenerator;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class HomeServlet extends CommonServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String hide = "display: none";
        Map<String, Object> map = new HashMap<>();
        HttpSession session = req.getSession();
        synchronized (session) {
            AccountService accountService = context.getAccountService();
            DBService dbService = context.getDbService();
            Set<ChatDataSet> chats = Collections.emptySet();
            int numberOfChats = 0;
            String login = "";
            if (accountService.isSignedIn(session)) {
                map.put("displaySignIn", hide);
                map.put("displaySignOut", "");
                map.put("displaySignUp", hide);
                UserProfile profile = accountService.getProfileBySession(session);
                UserDataSet user = dbService.getUser(profile.getLogin());
                if (user != null) {
                    login = user.getLogin();
                    chats = dbService.getChatsOfUser(user);
                    numberOfChats = chats.size();
                }
            } else {
                map.put("displaySignIn", "");
                map.put("displaySignOut", hide);
                map.put("displaySignUp", "");
            }
            map.put("login", login);
            map.put("chats", chats);
            map.put("numberOfChats", numberOfChats);
        }
        resp.setContentType(Constants.HTML_CONTENT_TYPE);
        resp.getWriter().println(PageGenerator.getInstance().getPage("home.html", map));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
