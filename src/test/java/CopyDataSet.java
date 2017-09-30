import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static util.Constants.*;
/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class CopyDataSet {
    public static void main(String[] args) {
        try (OutputStream os = Files.newOutputStream(Paths.get("dataset.xml"))) {
            Properties properties = new Properties();
            properties.load(CopyDataSet.class.getClassLoader().getResourceAsStream(HIB_PROPERTIES));
            final String url = properties.getProperty(HIB_URL);
            System.out.println("URL " + url);
            Connection jdbcConnection = DriverManager.getConnection(url,
                    properties.getProperty(HIB_USER), properties.getProperty(HIB_PASSWORD));
            final DatabaseConnection databaseConnection = new DatabaseConnection(jdbcConnection);
            databaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            final IDataSet dataSet = databaseConnection.createDataSet();
            FlatXmlDataSet.write(dataSet, os);
        } catch (DatabaseUnitException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
