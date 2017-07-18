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
            if (connection == null) {
                System.out.println("failed to connect");
                return;
            }
            DBService service = new DBService(connection);
            service.printConnectionInfo();
            long id = service.addUser("test1", "fest");
            long id1 = service.addUser("test1", "fast");
            System.out.println(id);
            System.out.println(id1);
            System.out.println(service.getUser(id));
            System.out.println(service.getUser(-239));
            service.cleanUp();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
