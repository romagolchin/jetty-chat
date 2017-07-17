package users;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class UserProfile {
    private String login;
    private String password;

    public UserProfile(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
