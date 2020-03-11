package factory;

import dao.UserDAO;
import dao.UserHibernateDAO;
import dao.UserJdbcDAO;
import util.DBHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UserDaoFactory {

    public static UserDAO getFactory() throws IOException {

        Properties properties = new Properties();
        ClassLoader loader = UserDaoFactory.class.getClassLoader();
        InputStream io = loader.getResourceAsStream("User.properties");
        properties.load(io);
        if (properties.getProperty("daotype").equalsIgnoreCase("Hibernate")) {
            return new UserHibernateDAO(DBHelper.getSessionFactory().openSession());
        } else if (properties.getProperty("daotype").equalsIgnoreCase("jdbc")) {
            return new UserJdbcDAO(DBHelper.getConnection());
        } else {
            return null;
        }
    }
}
