package dbService.dao;

import dbService.dataSets.UserDataSet;
import dbService.executor.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import users.UserProfile;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Translates actions with DB to SQL statements and passes them to an Executor
 *
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class UserDAO {
    private Executor executor;

    public UserDAO(Connection connection) {
        executor = new Executor(connection);
    }

    @Nullable
    public UserDataSet get(long id) throws SQLException {
        return executor.execQuery("select * from users where id='" + id + "'", result -> {
            if (!result.next())
                return null;
            return new UserDataSet(result.getLong("id"), new UserProfile(result.getString("login"), result.getString("password")));
        });
    }

    public long getUserId(String login) throws SQLException {
        return executor.execQuery("select * from users where login='" + login + "'", result -> {
                    if (!result.next())
                        return -1L;
                    return result.getLong("id");
                }
        );
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users " +
                "(id bigint auto_increment, login varchar(256), password varchar(256), primary key (id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table if exists users");
    }

    public void insertUser(@NotNull String login, @NotNull String password) throws SQLException {
        executor.execUpdate(String.format("insert into users (login, password) values ('%s', '%s')", login, password));
    }

}
