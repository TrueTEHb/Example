package myApp.controller;

import myApp.model.User;
import myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RESTController {

    @Autowired
    private UserService userService;


    @GetMapping(value = "/admin/list")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }


    @RequestMapping(value = "/admin/email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public User validEmail(@RequestBody String email) {

        if (email != null) {
            return userService.loadUserByUsername(email);
        } else {
            return null;
        }
    }

    @GetMapping(value = "/user/list")
    public User userPage() {
        return getCurrentUser();
    }

    //getUserById
    @GetMapping(value = "/admin/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    //delete user
    @DeleteMapping(value = "/admin/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    //delete role
    @DeleteMapping(value = "/admin/role/{id}")
    public void deleteRole(@PathVariable("id") Long id) {
        userService.deleteRole(id);
    }

    //update
    @PutMapping(value = "/admin/{id}")
    public User updateUser(@RequestBody User user1, @PathVariable Long id) {
        User finalUser = userService.updateUser(user1);
        return finalUser;
    }


    //save
    @PostMapping(value = "/admin/")
    public User addNewUser(@RequestBody User newUser) {

        String email = newUser.getEmail();

        User user = userService.getUserByEmail(email);
        if (user != null) {
            return null;
        } else {
            userService.saveUser(newUser);
        }
        return newUser;
    }

    public User getCurrentUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authUser.getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            User loginedUser = userService.getUserByEmail(email);
            return loginedUser;
        } else {
            return null;
        }
    }
}

