package dbService.datasets;


import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"login"}))
public class UserDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "login")
    private String login;
    @NotNull
    @Column(name = "password")
    private String password;

    public UserDataSet() {
    }

    public UserDataSet(long id, @NotNull String login, @NotNull String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public @NotNull String getLogin() {
        return login;
    }

    public @NotNull String getPassword() {
        return password;
    }
}
