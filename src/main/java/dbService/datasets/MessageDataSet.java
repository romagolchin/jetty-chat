package dbService.datasets;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
@Entity
@Table(name = "messages")
public class MessageDataSet implements DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private Date date;

    @Column(name = "user")
    private String user;

    public MessageDataSet() {
    }

    public MessageDataSet(long id, String text, Date date, String user) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }


    @Override
    public String toString() {
        return user + "\n" + text + "\n" + date + "\n";
    }
}
