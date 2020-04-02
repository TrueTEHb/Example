package myApp.service;

import myApp.model.User;
import myApp.repository.RoleRepo;
import myApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Transactional
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Transactional
    @NotNull
    public void saveUser(User user) {
        if (user != null) {
            userRepo.save(user);
        } else {
            throw new IllegalArgumentException("Wrong user parameters");
        }
    }

    @Transactional
    @NotNull
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Transactional
    @NotNull
    public void deleteRole(Long id) {
        roleRepo.deleteRolesByUserId(id);
    }

    @Transactional
    public User getUser(Long id) {
        return userRepo.getOne(id);
    }

    @Transactional
    public void updateUser(User user) {
        userRepo.save(user);
    }

    public User getUserByEmail(String name) {
        if (name != null && !name.isEmpty()) {
            return userRepo.getUserByEmail(name);
        } else {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isEmpty() || username == null) {
            throw new UsernameNotFoundException("Username is empty");
        } else {
            return userRepo.getUserByEmail(username);
        }
    }
}
