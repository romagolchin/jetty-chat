package dbService;

import dbService.datasets.ChatDataSet;
import dbService.datasets.MessageDataSet;
import dbService.datasets.UserDataSet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
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
        sessionFactory = createSessionFactory();
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

    private static SessionFactory createSessionFactory() {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        ServiceRegistry serviceRegistry = builder.configure(configFile).build();
        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(UserDataSet.class)
                .addAnnotatedClass(MessageDataSet.class)
                .addAnnotatedClass(ChatDataSet.class)
                        .getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }
}
