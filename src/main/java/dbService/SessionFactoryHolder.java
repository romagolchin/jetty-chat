package dbService;

import dbService.datasets.ChatDataSet;
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

    private static String configFile;


    public static void configure(String configFile) {
        SessionFactoryHolder.configFile = configFile;
    }

    private static void setSessionFactory() {
        sessionFactory = createSessionFactory(getConfiguration());
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

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        return configuration.configure(configFile).addAnnotatedClass(UserDataSet.class)
                .addAnnotatedClass(MessageDataSet.class)
                .addAnnotatedClass(ChatDataSet.class);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();
        return configuration.buildSessionFactory(registry);
    }
}
