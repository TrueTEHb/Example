package myapp.service;

import myapp.dao.UserDao;
import myapp.model.Role;
import myapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isEmpty() || username == null) {
            throw new UsernameNotFoundException("Username is empty");
        } else {
            User user = userDao.getUserByName(username);
            List<GrantedAuthority> authorities = buildUserAuth(user.getRoles());
            return buildUserForAuth(user, authorities);
        }
    }

    private org.springframework.security.core.userdetails.User buildUserForAuth(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuth(List<Role> roles) {
        List<GrantedAuthority> authList = new ArrayList<>();
        for (Role role : roles) {
            authList.add(new SimpleGrantedAuthority(role.getValue()));
        }
        List<GrantedAuthority> resultList = new ArrayList<>(authList);

        return resultList;
    }


    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional
    @Override
    public void addUser(User user) {
        if (user != null) {
            Role role = new Role();
            role.setValue("USER");
            role.setUser(user);
            user.setRoles(Collections.singletonList(role));
            userDao.addUser(user);
        }
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (id != null || id != 0) {
            userDao.deleteUser(id);
        }
    }

    @Override
    public void deleteRole(Long id) {
        if (id != null || id != 0) {
            userDao.deleteRoleById(id);
        }
    }

    @Transactional
    @Override
    public User getUser(Long id) {
        if (id != null || id != 0) {
            return userDao.getUser(id);
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        if (user != null) {
            userDao.updateUser(user);
        }
    }

    @Override
    public User getUserByName(String name) {
        if (name != null && !name.isEmpty()) {
            return userDao.getUserByName(name);
        } else {
            return null;
        }
    }
}
