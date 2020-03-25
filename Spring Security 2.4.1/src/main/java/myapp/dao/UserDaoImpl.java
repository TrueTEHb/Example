package myapp.dao;

import myapp.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public List<User> getAllUsers() {
        return sessionFactory.getCurrentSession().createQuery("from User ").getResultList();
    }

    @Override
    public void addUser(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public void deleteUser(Long id) {
        sessionFactory.getCurrentSession().createQuery("delete from User where id = :id").
                setParameter("id", id).executeUpdate();
    }

    @Override
    public User getUser(Long id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where id = :id").
                setParameter("id", id);
        return (User) query.uniqueResult();
    }

    @Override
    public void updateUser(User user) {
        sessionFactory.getCurrentSession().update(user);
    }


    @Override
    public User getUserByName(String name) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where name = :name").
                setParameter("name", name);
        return (User) query.uniqueResult();
    }

    @Override
    public void deleteRoleById(Long id) {
        sessionFactory.getCurrentSession().createQuery("delete from Role where user_id = :id").
                setParameter("id", id).executeUpdate();
    }


}
