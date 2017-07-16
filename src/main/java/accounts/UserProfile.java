package accounts;

import org.jetbrains.annotations.NotNull;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class UserProfile {
    @NotNull private String login;
    @NotNull private String password;

    public UserProfile(@NotNull String login, @NotNull String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
