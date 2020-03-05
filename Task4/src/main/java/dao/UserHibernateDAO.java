package dao;

import model.User;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class UserHibernateDAO implements UserDAO {

    private Session session;

    public UserHibernateDAO(Session session) {
        this.session = session;
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) session.createQuery("from User").list();
    }

    @Override
    public void addUser(User user) {

        session.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void deleteUser(Long id) {

        session.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("delete User where id = :id").setParameter("id", id).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public User getUser(Long id) {
        return (User) session.load(User.class, id);
    }

    @Override
    public void updateUser(User user) {
        session.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("update User set name = :name, password = :password, money = :money " +
                "where id =:id");
        query.setParameter("name", user.getName());
        query.setParameter("password", user.getPassword());
        query.setParameter("money", user.getMoney());
        query.setParameter("id", user.getId());
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
