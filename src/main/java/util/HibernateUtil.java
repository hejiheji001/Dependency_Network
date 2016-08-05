package util;

/**
 * Created by FireAwayH on 07/07/2016.
 */

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    static {
        try {
            Configuration configuration = new Configuration();
//            configuration.addSqlFunction("stddev", new StandardSQLFunction("stddev", new DoubleType()));
            sessionFactory = configuration.configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession()
            throws HibernateException {
        return sessionFactory.openSession();
    }
}