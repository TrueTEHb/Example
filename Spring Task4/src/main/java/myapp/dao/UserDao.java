package myapp.dao;

import myapp.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    void addUser(User user);

    void deleteUser(Long id);

    User getUser(Long id);

    void updateUser(User user);


    User getUserByName(String name);

    void deleteRoleById(Long id);
}
