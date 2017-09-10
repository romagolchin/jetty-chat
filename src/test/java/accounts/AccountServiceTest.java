package accounts;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static util.Constants.*;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class AccountServiceTest {
    private AccountService accountService;



    @Before
    public void setUp() throws Exception {
        accountService = new AccountServiceImpl(new DBServiceFake());
    }

    @Test
    public void addUserEmpty() throws Exception {
        try {
            accountService.signUp("", "");
            fail("Must throw IllegalArgumentException");
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Test
    public void addUserDuplicate() throws Exception {
        accountService.signUp("test", "fest");
        try {
            accountService.signUp("test", "test");
            fail("Must throw ExistingUserException");
        } catch (ExistingUserException ignore) {

        } catch (Exception e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void signOut() throws Exception {
        accountService.signUp("a", "a");
        final HttpSession mockSession = new SessionFake();
        accountService.signIn(mockSession, "a", "a");
        assertTrue(accountService.isSignedIn(mockSession));
        accountService.signOut(mockSession);
//        Thread t1 = new Thread(() -> accountService.signOut(mockSession));
//        t1.start();
//        Thread t2 = new Thread(() -> accountService.signIn(mockSession, "a", "a"));
//        t2.start();
//        t1.join();
//        t2.join();
        System.err.println(accountService.getProfileBySession(mockSession));
        assertFalse(accountService.isSignedIn(mockSession));
    }

    @Test
    public void getProfileBySession() throws Exception {
        accountService.signUp(LOGIN, PASSWORD);
        final SessionFake session = new SessionFake();
        accountService.signIn(session, LOGIN, PASSWORD);
        final UserProfile profileBySession = accountService.getProfileBySession(session);
        assertNotNull(profileBySession);
        assertEquals(LOGIN, profileBySession.getLogin());
        assertEquals(PASSWORD, profileBySession.getPassword());
        assertNull(accountService.getProfileBySession(new SessionFake()));
    }

    @Test
    public void doesUserExist() throws Exception {
        assertFalse(accountService.doesUserExist(LOGIN));
        accountService.signUp(LOGIN, PASSWORD);
        assertTrue(accountService.doesUserExist(LOGIN));
    }

    @After
    public void tearDown() throws Exception {

    }
}