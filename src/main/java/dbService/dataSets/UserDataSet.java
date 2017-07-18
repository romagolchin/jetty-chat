package dbService.dataSets;

import users.UserProfile;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@Entity
@Table(name = "users", schema = "server_db")

public class UserDataSet {
    private long id;
    private UserProfile profile;

    public UserDataSet(long id, UserProfile profile) {
        this.id = id;
        this.profile = profile;
    }

    public long getId() {
        return id;
    }

    public UserProfile getProfile() {
        return profile;
    }
}
