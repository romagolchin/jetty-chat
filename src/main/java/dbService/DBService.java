package dbService;

import dbService.dao.UserDAO;
import dbService.dataSets.UserDataSet;
import org.jetbrains.annotations.Nullable;
import users.UserProfile;
import org.jetbrains.annotations.NotNull;

import java.sql.*;


/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class DBService {
    private Connection connection;

    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    public DBService(@NotNull Connection connection) throws DBException {
        this.connection = connection;
        try {
            new UserDAO(connection).createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public long addUser(@NotNull String login, @NotNull String password) throws DBException {
        UserDAO dao = new UserDAO(connection);
        try {
            // disable saving changes after each statement
            connection.setAutoCommit(false);
            // these actions constitute a transaction, i.e. either nothing or all of them are completed
            long userId = dao.getUserId(login);
            if (userId < 0) {
                dao.insertUser(login, password);
                userId = dao.getUserId(login);
            } else userId = -1;
            return userId;
        } catch (SQLException e) {
            try {
                // if an error happens while executing one of them, we roll the changes back
                connection.rollback();
            } catch (SQLException ignore) {}
            throw new DBException(e);
        } finally {
            try {
                // restore auto-commit
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    public UserProfile getUser(long id) throws DBException {
        try {
            UserDataSet userDataSet = new UserDAO(connection).get(id);
            return userDataSet == null ? null : userDataSet.getProfile();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void cleanUp() throws DBException {
        try {
            new UserDAO(connection).dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Nullable
    public static Connection getMysqlConnection() {
        try {
            Driver driver = (Driver) Class.forName(DRIVER_CLASS_NAME).newInstance();
            DriverManager.registerDriver(driver);
            String url = new StringBuilder()
                    .append("jdbc:mysql://")
                    .append("localhost:")
                    .append("3306/")
                    .append("server_db?")
                    .append("user=root&")
                    .append("password=dangerousdentistchocolate")
                    .toString();
            return DriverManager.getConnection(url);
        } catch (SQLException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void printConnectionInfo() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("Name: " + metaData.getDatabaseProductName());
            System.out.println("Version: " + metaData.getDatabaseMajorVersion());
            System.out.println("Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());
            System.out.println("Auto-commit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
