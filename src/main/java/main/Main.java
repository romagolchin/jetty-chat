package main;

import dbService.DBException;
import dbService.DBService;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class Main {
    public static void main(String[] args) throws DBException {
        try (Connection connection = DBService.getMysqlConnection()) {
            DBService service = new DBService(connection);
            service.printConnectionInfo();
            long id = service.addUser("test", "fest");
            System.out.println(id);
            System.out.println(service.getUser(id));
            System.out.println(service.getUser(-239));
            service.cleanUp();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
