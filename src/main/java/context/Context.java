package context;

import accounts.AccountService;
import chat.ChatService;
import dbService.DBService;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class Context {
    private DBService dbService;

    private ChatService chatService;

    private AccountService accountService;

    private static Context context = new Context();

    private Context () {}

    public static Context getInstance() {
        return context;
    }

    public DBService getDbService() {
        return dbService;
    }

    public void setDbService(DBService dbService) {
        this.dbService = dbService;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
