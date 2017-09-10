package dbService.datasets;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@Entity
@Table(name = "messages")
public class MessageDataSet implements DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private Timestamp timestamp;

    @ManyToOne
    private UserDataSet user;

    public MessageDataSet() {
    }

    public MessageDataSet(long id, String text, Date date, UserDataSet user) {
        this.id = id;
        this.text = text;
        this.timestamp = new Timestamp(date.getTime());
        this.user = user;
    }

    public MessageDataSet(String text, Date date, UserDataSet user) {
        this.text = text;
        this.timestamp = new Timestamp(date.getTime());
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public UserDataSet getUser() {
        return user;
    }


    @Override
    public String toString() {
        Date date = new Date(timestamp.getTime());
        return user + "\n" + text + "\n" + date + "\n";
    }
}
