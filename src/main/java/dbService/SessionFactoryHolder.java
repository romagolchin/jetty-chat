package dbService;

import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;

/**
 * @author Roman Golchin (romagolchin@gmail.com)
 */
public class SessionFactoryHolder {

    private static volatile SessionFactory sessionFactory;

    private static void setSessionFactory() {
        sessionFactory = createSessionFactory(getH2Configuration());
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (SessionFactoryHolder.class) {
                if (sessionFactory == null)
                    setSessionFactory();
            }
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() throws IOException {
        if (sessionFactory != null)
            sessionFactory.close();
    }

    private static Configuration getH2Configuration() {
        Configuration configuration = new Configuration();
        return configuration.configure("hibernate.cfg.xml").addAnnotatedClass(UserDataSet.class).addAnnotatedClass(MessageDataSet.class);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();
        return configuration.buildSessionFactory(registry);
    }
}
