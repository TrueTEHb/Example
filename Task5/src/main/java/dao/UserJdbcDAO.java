package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserJdbcDAO implements UserDAO {

    private Connection connection;

    public UserJdbcDAO(Connection connection) {
        this.connection = connection;
    }

    public void createTable() throws SQLException {

        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists persons " +
                "(id bigint auto_increment, " +
                "name varchar(256), " +
                "password varchar(256), " +
                "money bigint, " +
                "role varchar(256)," +
                "primary key (id))");
        stmt.close();
    }

    @Override
    public List<User> getAllUsers() throws SQLException {

        createTable();
        List<User> people = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from persons");
        while (resultSet.next()) {
            people.add(new User(resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("password"),
                    resultSet.getLong("money"),
                    resultSet.getString("role")));
        }
        return people;
    }

    @Override
    public void addUser(User user) throws SQLException {

        createTable();
        PreparedStatement stmt = connection.prepareStatement("insert into persons (name, password, money, role) VALUES (?,?,?,?)");
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getPassword());
        stmt.setLong(3, user.getMoney());
        stmt.setString(4, user.getRole());
        stmt.executeUpdate();
        stmt.close();
    }

    @Override
    public void deleteUser(Long id) throws SQLException {
        connection.createStatement().executeUpdate("delete from persons where id = '" + id + "'");
    }

    @Override
    public User getUser(Long id) throws SQLException {

        Statement stmt = connection.createStatement();
        stmt.execute("select * from persons where id = '" + id + "'");
        ResultSet result = stmt.getResultSet();
        if (result.next()) {
            return new User(id, result.getString("name"), result.getString("password"),
                    result.getLong("money"), result.getString("role"));
        } else {
            return new User(null, null, null, null);
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("update persons set name = ?, password = ?," +
                "money = ?, role = ? where id = ?");
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getPassword());
        stmt.setLong(3, user.getMoney());
        stmt.setString(4, user.getRole());
        stmt.setLong(5, user.getId());

        stmt.executeUpdate();
        stmt.close();
    }

    @Override
    public User getUserByNamePass(String name, String password) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select * from persons where name = ? and password = ?");
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.executeQuery();
        ResultSet result = stmt.getResultSet();
        if (result.next()) {
            return new User(result.getLong("id"), result.getString("name"), result.getString("password"),
                    result.getLong("money"), result.getString("role"));
        } else {
            return null;
        }
    }
}
