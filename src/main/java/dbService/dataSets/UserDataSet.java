package dbService.dataSets;

import users.UserProfile;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
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
