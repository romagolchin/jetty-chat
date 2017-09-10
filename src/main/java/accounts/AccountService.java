package accounts;

import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpSession;

/**
 * Supports actions with accounts, such as adding one, signing in, signing out.
 *
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public interface AccountService {

    /**
     * Add user if the login is not already used and both login and password are valid
     *
     * @param login
     * @param password
     * @throws IllegalArgumentException if login or password are invalid
     * @throws ExistingUserException if user with the login already exists
     */
    void signUp(@NotNull String login, @NotNull String password);

    /**
     * Check if user exists
     *
     * @param login
     * @return
     */
    boolean doesUserExist(@NotNull String login);

    /**
     * Check if user is signed in. This method and others that take a session as an argument are required to synchronize on session
     *
     * @param session
     * @return
     */
    boolean isSignedIn(@NotNull HttpSession session);

    /**
     * Sign in user associated with session.
     *
     * @param session
     * @param login
     * @param password
     * @return
     */
    boolean signIn(@NotNull HttpSession session, @NotNull String login, @NotNull String password);

    /**
     * Sign out user associated with session. All subsequent calls to methods  {@link #isSignedIn(HttpSession)}, {@link #signIn(HttpSession, String, String)}, {@link #signOut(HttpSession)} return false and calls to {@link #getProfileBySession(HttpSession)} return null, as seesion gets invalidated.
     * @param session
     * @return true if sign-in was successful, false otherwise
     */
    boolean signOut(@NotNull HttpSession session);

    /**
     * Get user associated with session
     */
    UserProfile getProfileBySession(@NotNull HttpSession session);

}
