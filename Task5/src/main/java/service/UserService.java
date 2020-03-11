package service;

import factory.UserDaoFactory;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class UserService {

    private static UserService userService;
    private UserDaoFactory daoFactory;

    private UserService() {
    }

    private UserService(UserDaoFactory daoFactory) throws IOException {
        this.daoFactory = daoFactory;
    }

    public static UserService getInstance() throws IOException {
        if (userService == null) {
            userService = new UserService(new UserDaoFactory());
        }
        return userService;
    }

    public List<User> getAllUsers() throws SQLException, IOException {
        return daoFactory.getFactory().getAllUsers();
    }

    public boolean addUser(User user) {
        try {
            if (user.getRole() == null) {
                user.setRole("user");
            }
            daoFactory.getFactory().addUser(user);
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteUser(Long id) {
        try {
            daoFactory.getFactory().deleteUser(id);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser(Long id) {
        try {
            return daoFactory.getFactory().getUser(id);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserByNamePass(String name, String password) {
        try {
            if (name == null || password == null) {
                return null;
            }
            return daoFactory.getFactory().getUserByNamePass(name, password);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUser(User user) {
        try {
            daoFactory.getFactory().updateUser(user);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isExistUser(String name, String password) {
        try {
            if (daoFactory.getFactory().getUserByNamePass(name, password) == null){
                return false;
            }else {
                return true;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}