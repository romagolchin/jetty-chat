package dbService;

import com.mysql.cj.core.conf.url.HostInfo;
import com.mysql.cj.core.conf.url.SingleConnectionUrl;
import com.mysql.cj.jdbc.ConnectionImpl;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseDataSet;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.ExternalResource;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class DBUnit extends ExternalResource {
//    private final IDataSet beforeData = new DatabaseDataSet(new DatabaseConnection(new ConnectionImpl(new HostInfo(new SingleConnectionUrl(new JdbcDatabaseTester())))));
    private final IDataSet beforeData = new DefaultDataSet();
    private final IDatabaseTester databaseTester;
    private final DatabaseOperation setUpOperation;
    private final DatabaseOperation tearDownOperation;

    public DBUnit(IDatabaseTester databaseTester, DatabaseOperation setUpOperation, DatabaseOperation tearDownOperation) {
        this.databaseTester = databaseTester;
        this.setUpOperation = setUpOperation;
        this.tearDownOperation = tearDownOperation;
    }

    @Override
    protected void before() throws Throwable {
        databaseTester.setDataSet(beforeData);
        databaseTester.setSetUpOperation(setUpOperation);
        databaseTester.onSetup();
    }

    @Override
    protected void after() {
        try {
            databaseTester.setTearDownOperation(tearDownOperation);
            databaseTester.onTearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
