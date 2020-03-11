package dao;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    List<User> getAllUsers() throws SQLException;

    void addUser(User user) throws SQLException; // зарегистрироваться

    void deleteUser(Long id) throws SQLException; //удалить аккаунт

    User getUser(Long id) throws SQLException;

    void updateUser(User user) throws SQLException; // изменить данные пользователя

    User getUserByNamePass(String name, String password) throws SQLException;

}
