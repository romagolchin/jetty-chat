package dbService;

import dbService.datasets.ChatDataSet;
import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.operation.DatabaseOperation;
import org.junit.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static util.Constants.*;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class DBServiceTest {
    private DBService dbService;


    // TODO pass dataset to constructor
    @Rule
    public DBUnit dbUnit;

    public DBServiceTest() {
        try {
            dbUnit = new DBUnit(new JdbcDatabaseTester("org.h2.Driver", "jdbc:h2:mem:", "sa", "sa"),
                    DatabaseOperation.NONE, DatabaseOperation.DELETE_ALL);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Before
    public void setUp() throws Exception {
        SessionFactoryHolder.configure("hibernate_test.cfg.xml");
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
        UserDataSet user = dbService.addUser(LOGIN, PASSWORD);
        final String otherLogin = LOGIN + "1";
        UserDataSet other = dbService.addUser(otherLogin, PASSWORD);
        final ChatDataSet chat = new ChatDataSet(new Date(), "chat");
        final Set<UserDataSet> singleton = Collections.singleton(user);
        Serializable id = dbService.addUsersToChat(chat, singleton);
        dbService.addUsersToChat(chat, Collections.singleton(other));
        final ChatDataSet loadedChat = dbService.getChat(id);
        System.out.println(loadedChat.getCreationStamp());
        Set<UserDataSet> userDataSets = dbService.getUsersInChat(loadedChat);
        System.out.println(userDataSets);
        assertTrue(userDataSets.size() == 2);
    }

    @Test
    public void concurrentlyAddToChat() throws Exception {
        // add 2 users
        // add first user to 2 new chats
        // add second user to both chats in 2 threads
        Set<UserDataSet> singleton = Collections.singleton(dbService.addUser(LOGIN, PASSWORD));
        final String otherLogin = LOGIN + "1";
        final UserDataSet otherUser = dbService.addUser(otherLogin, PASSWORD);
        Set<UserDataSet> otherSingleton = Collections.singleton(otherUser);
        Set<ChatDataSet> chats = new HashSet<>();
        final int N = 100;
        for (int i = 0; i < N; i++) {
            ChatDataSet chat = new ChatDataSet(new Date(), "chat" + i);
            chats.add(chat);
            dbService.addUsersToChat(chat, singleton);
        }
//        Serializable firstId = dbService.addUsersToChat(new ChatDataSet(new Date(), "chat1"), singleton);
//        Serializable secondId = dbService.addUsersToChat(new ChatDataSet(new Date(), "chat2"), singleton);
        ExecutorService executorService = Executors.newFixedThreadPool(N);
//        executorService.submit(() ->
//                dbService.addUsersToChat(dbService.getChat(firstId), otherSingleton));
//        executorService.submit(() ->
//                dbService.addUsersToChat(dbService.getChat(secondId), otherSingleton));
        for (ChatDataSet chat : chats) {
            executorService.submit(() -> dbService.addUsersToChat(chat, otherSingleton));
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        assertNotNull(otherUser);
        final Set<ChatDataSet> chatsOfUser = dbService.getChatsOfUser(otherUser);
        System.out.println(chatsOfUser);
        assertEquals(N, chatsOfUser.size());
        for (ChatDataSet chat : chats) {
            assertEquals(2, dbService.getUsersInChat(chat).size());
        }
    }

    @Test
    public void removeFromChat() throws Exception {
        final UserDataSet user = dbService.addUser(LOGIN, PASSWORD);
        Set<UserDataSet> singleton = Collections.singleton(user);
        ChatDataSet chat = new ChatDataSet(new Date(), "chat");
        dbService.addUsersToChat(chat, singleton);
        dbService.removeUsersFromChat(chat, singleton);
        assertTrue(dbService.getChatsOfUser(user).isEmpty());
        assertTrue(dbService.getUsersInChat(chat).isEmpty());
    }

    @Test
    public void concurrentlyRemoveFromChat() throws Exception {
        Set<UserDataSet> users = new HashSet<>();
        Set<UserDataSet> firstHalf = new HashSet<>();
        Set<UserDataSet> secondHalf = new HashSet<>();
        final int N = 100;
        for (int i = 0; i < N; i++) {
            final UserDataSet user = dbService.addUser(LOGIN + i, PASSWORD);
            users.add(user);
            if (i < N / 2)
                firstHalf.add(user);
            else secondHalf.add(user);
        }
        final ChatDataSet chat = new ChatDataSet(new Date(), "chat");
        dbService.addUsersToChat(chat, users);
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        executorService.submit(() -> dbService.removeUsersFromChat(chat, firstHalf));
//        executorService.submit(() -> dbService.removeUsersFromChat(chat, secondHalf));
        Thread t1 = new Thread(() -> dbService.removeUsersFromChat(chat, firstHalf));
        Thread t2 = new Thread(() -> dbService.removeUsersFromChat(chat, secondHalf));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
//        executorService.shutdown();
//        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
//        executorService.awaitTermination(2, TimeUnit.SECONDS);
        final Set<UserDataSet> usersInChat = dbService.getUsersInChat(chat);
        System.out.println(usersInChat.size());
        assertTrue(usersInChat.isEmpty());
        for (UserDataSet user : users) {
            final Set<ChatDataSet> chatsOfUser = dbService.getChatsOfUser(user);
            System.out.println(chatsOfUser.size());
            assertTrue(chatsOfUser.isEmpty());
        }
    }

    @Test
    public void concurrentlyRemoveFromMultipleChats() throws Exception {
        UserDataSet user = dbService.addUser(LOGIN, PASSWORD);
        final int N = 100;
        List<ChatDataSet> chats = new ArrayList<>();
        Set<UserDataSet> singleton = Collections.singleton(user);
        for (int i = 0; i < N; i++) {
            ChatDataSet chat = new ChatDataSet(new Date(), "chat" + i);
            chats.add(chat);
            dbService.addUsersToChat(chat, singleton);
        }
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < N / 2; i++) {
                dbService.removeUsersFromChat(chats.get(i), singleton);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = N / 2; i < N; i++) {
                dbService.removeUsersFromChat(chats.get(i), singleton);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        final Set<ChatDataSet> chatsOfUser = dbService.getChatsOfUser(user);
        System.out.println("size = " + chatsOfUser.size());
        assertTrue(chatsOfUser.isEmpty());
    }
}