package dbService.datasets;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@Entity(name = "ChatDataSet")
//@Table(name = "chats")
public class ChatDataSet implements DataSet {

    public ChatDataSet() {
    }

    public ChatDataSet(Date creationStamp, String name) {
        this.creationStamp = new Timestamp(creationStamp.getTime());
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Timestamp creationStamp;

    private String name;

    @ManyToMany
    private Set<UserDataSet> users = ConcurrentHashMap.newKeySet();

    public Integer getId() {
        return id;
    }

    public Timestamp getCreationStamp() {
        return creationStamp;
    }

    public String getName() {
        return name;
    }

    public Set<UserDataSet> getUsers() {
        return users;
    }

    public void addUser(UserDataSet userDataSet) {
        userDataSet.getChats().add(this);
        users.add(userDataSet);
    }

    public void addAllUsers(Collection<UserDataSet> userDataSets) {
        for (UserDataSet userDataSet : userDataSets) {
            addUser(userDataSet);
        }
    }

    public void removeUser(UserDataSet userDataSet) {
        users.remove(userDataSet);
        userDataSet.getChats().remove(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatDataSet that = (ChatDataSet) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(creationStamp, that.creationStamp) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationStamp, name);
    }
}
