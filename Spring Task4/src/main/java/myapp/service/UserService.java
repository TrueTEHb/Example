package myapp.service;

import myapp.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    List<User> getAllUsers();

    void addUser(User user);

    void deleteUser(Long id);

    void deleteRole(Long id);

    User getUser(Long id);

    void updateUser(User user);

    User getUserByNamePass(String name, String password);

    User getUserByName(String name);
}
