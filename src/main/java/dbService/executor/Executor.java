package dbService.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class Executor {
    public Executor(Connection connection) {
        this.connection = connection;
    }

    private Connection connection;

    public int execUpdate(String update) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(update);
            return statement.getUpdateCount();
        }
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery(query);
            try (ResultSet resultSet = statement.getResultSet()) {
                return handler.handle(resultSet);
            }
        }
    }

    //todo
    public int execAllUpdates(Collection<String> updates) throws SQLException {
        return 0;
    }
}
