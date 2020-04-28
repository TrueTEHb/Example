package myApp.service;

import myApp.model.Role;
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
public class UserService implements UserDetailsService{


    private UserRepo userRepo;
    private RoleRepo roleRepo;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

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
        roleRepo.deleteRolesById(id);
    }

    @Transactional
    public User getUser(Long id) {
        return userRepo.getOne(id);
    }

    @Transactional
    public User updateUser(User user, String role) {

        if (role == null){ // authorized admin, roles not changes
            userRepo.save(user);
            return user;
        }

        if (user.getRoles().size() == 2){ // admin + user
            if (role.equals("USER")){ // delete role admin
                user.getAuthorities().clear();
                user.getRoles().clear();
                roleRepo.deleteRolesById(user.getId());

                Role newRole = new Role();
                newRole.setValue(role);
                user.getRoles().add(newRole);
                userRepo.save(user);
                return user;
            }else {
                userRepo.save(user); // roles not changes
                return user;
            }
        }else { //size == 1  - user
            if (role.equals("USER")){ // не менять роль
                userRepo.save(user);
                return user;
            }else { // добавить роль admin
                Role newRole = new Role();
                newRole.setValue(role);
                user.getRoles().add(newRole);
                userRepo.save(user);
                return user;
            }
        }
    }

    @Transactional
    public User getUserByEmail(String email) {
        if (email != null && !email.isEmpty()) {
            return userRepo.getUserByEmail(email);
        } else {
            return null;
        }
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isEmpty() || username == null) {
            throw new UsernameNotFoundException("Username is empty");
        } else {
            return userRepo.getUserByEmail(username);
        }
    }
}
