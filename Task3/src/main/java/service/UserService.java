package service;

import dao.UserHibernateDAO;
import dao.UserJdbcDAO;
import model.User;
import org.hibernate.SessionFactory;
import util.DBHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    private static UserService hibernateService;
    private Connection connection;
    private SessionFactory sessionFactory;

    public UserService() {

    }

    private UserService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static UserService getInstance() {
        if (hibernateService == null) {
            hibernateService = new UserService(DBHelper.getSessionFactory());
        }
        return hibernateService;
    }

    private static UserHibernateDAO getUserDao() {
        return new UserHibernateDAO(hibernateService.sessionFactory.openSession());
    }

    private static UserJdbcDAO getUserJdbcDAO() {
        return new UserJdbcDAO(DBHelper.getMysqlConnection());
    }

    public List<User> getAllUsers() throws SQLException {
        return (getInstance().getUserDao().getAllUsers() == null ? null : getInstance().getUserDao().getAllUsers());
    }

    public boolean addUser(User user) {
        try {
            getInstance().getUserDao().addUser(user);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public void deleteUser(Long id) {
        getInstance().getUserDao().deleteUser(id);
    }

    public User getUser(Long id) {

        try {
            return getInstance().getUserDao().getUser(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(User user) {

        try {
            getInstance().getUserDao().updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}