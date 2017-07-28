package dbService.dao;

import dbService.datasets.MessageDataSet;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class MessageDAO extends AbstractDAO<MessageDataSet> {

    public MessageDAO(@NotNull SessionFactory factory) {
        super(factory);
        dataSetClass = MessageDataSet.class;
    }


}
