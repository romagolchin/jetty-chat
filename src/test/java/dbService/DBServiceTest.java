package dbService;

import dbService.datasets.ChatDataSet;
import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import org.junit.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static util.Constants.*;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class DBServiceTest {
    private DBService dbService;

    
    // TODO pass dataset to constructor
//    @Rule
//    public DBUnit dbUnit;
//
//    public DBServiceTest() {
//        try {
//            dbUnit = new DBUnit(new JdbcDatabaseTester("org.h2.Driver", "jdbc:h2:mem:", "sa", "sa"), DatabaseOperation.NONE, DatabaseOperation.DELETE_ALL);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }


    @Before
    public void setUp() throws Exception {
        SessionFactoryHolder.configure("hibernate_h2_mem.cfg.xml");
        dbService = new DBServiceImpl(SessionFactoryHolder.getSessionFactory());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addUser() throws Exception {
        dbService.addUser(LOGIN, PASSWORD);
        assertNotNull(dbService.getUser(LOGIN));
    }

    @Test
    @Ignore
    public void getUser() throws Exception {
    }

    @Test
    @Ignore
    public void addMessage() throws Exception {

    }

    @Test
    public void getAllMessages() throws Exception {
        final int N = 10000;
        final Comparator<MessageDataSet> comparator = Comparator.comparing(MessageDataSet::getId);
        Set<MessageDataSet> messageDataSets = new TreeSet<>(comparator);
        final UserDataSet user = new UserDataSet(LOGIN, PASSWORD);
        dbService.addUser(LOGIN, PASSWORD);
        for (int i = 0; i < N; i++) {
            final Date date = new Date();
            MessageDataSet dbDataSet = new MessageDataSet(MESSAGE, date, dbService.getUser(LOGIN));
            dbService.addMessage(dbDataSet);
            messageDataSets.add(new MessageDataSet(i + 1, MESSAGE, date, user));
        }
        final TreeSet<MessageDataSet> treeSetFromDB = new TreeSet<>(comparator);
        treeSetFromDB.addAll(dbService.getAllMessages());
        assertTrue(treeSetFromDB.size() == N);
        assertTrue(treeSetFromDB.equals(messageDataSets));
    }

    @Test
    public void addChat() throws Exception {
        dbService.addUser(LOGIN, PASSWORD);
        final String otherLogin = LOGIN + "1";
        dbService.addUser(otherLogin, PASSWORD);
        final ChatDataSet chat = new ChatDataSet(new Date(), "chat");
        final Set<UserDataSet> singleton = Collections.singleton(dbService.getUser(LOGIN));
        Serializable id = dbService.addUsersToChat(chat, singleton);
        dbService.addUsersToChat(dbService.getChat(id), Collections.singleton(dbService.getUser(otherLogin)));
        final ChatDataSet loadedChat = dbService.getChat(id);
        System.out.println(loadedChat.getCreationStamp());
        Set<UserDataSet> userDataSets = dbService.getUsersInChat(id);
        System.out.println(userDataSets);
        assertTrue(userDataSets.size() == 2);
    }

    @Test
    public void concurrentlyAddToChat() throws Exception {
        dbService.addUser(LOGIN, PASSWORD);
        Set<UserDataSet> singleton = Collections.singleton(dbService.getUser(LOGIN));
        final String otherLogin = LOGIN + "1";
        dbService.addUser(otherLogin, PASSWORD);
        final UserDataSet user = dbService.getUser(otherLogin);
        Set<UserDataSet> otherSingleton = Collections.singleton(user);
        Serializable firstId = dbService.addUsersToChat(new ChatDataSet(new Date(), "chat1"), singleton);
        Serializable secondId = dbService.addUsersToChat(new ChatDataSet(new Date(), "chat2"), singleton);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() ->
                dbService.addUsersToChat(dbService.getChat(firstId), otherSingleton));
        executorService.submit(() ->
                dbService.addUsersToChat(dbService.getChat(secondId), otherSingleton));
//        executorService.submit(() ->
//                System.out.println("stamp " + dbService.getChat(firstId).getCreationStamp()));
//        executorService.submit(() ->
//                System.out.println("stamp2 " + dbService.getChat(secondId).getCreationStamp()));

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//        dbService.addChat(dbService.getChat(firstId), otherSingleton);
//        dbService.addChat(dbService.getChat(secondId), otherSingleton);
        System.out.println("first " + dbService.getUsersInChat(firstId));
        System.out.println("second " + dbService.getUsersInChat(secondId));
        if (user != null) {
            final Set<ChatDataSet> chatsOfUser = dbService.getChatsOfUser(user);
            System.out.println(chatsOfUser);
            assertTrue(chatsOfUser.size() == 2);
        }
        System.out.printf("hash1 %x\n", dbService.getChat(firstId).hashCode());
        System.out.printf("hash2 %x\n", dbService.getChat(secondId).hashCode());
    }
}