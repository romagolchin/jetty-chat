package dbService.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public interface ResultHandler <T> {
    T handle (ResultSet resultSet) throws SQLException;
}
