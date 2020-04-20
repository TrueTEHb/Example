package myApp.controller;

import myApp.model.Role;
import myApp.model.User;
import myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;


    @GetMapping(value = "/admin/list")
    public ResponseEntity<?> dataPage() {
        ModelMap model = new ModelMap();
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("people", userService.getAllUsers());
        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/admin")
    public ModelAndView viewPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("adminPage");
        return model;
    }

    //delete
    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        User logginedUser = getCurrentUser();
        Long curId = logginedUser.getId();
        User existUser = userService.getUser(id);

        if (existUser == null){
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (id != curId) {
            userService.deleteRole(id);
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (id == curId) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //update
    @PutMapping(value = "/admin/{id}")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> user1, @PathVariable Long id) {
        User forRole = userService.getUser(id);
        String firstName = (String) user1.get("firstName");
        String lastName = (String) user1.get("lastName");
        int age = Integer.parseInt((String) user1.get("age"));
        String email = (String) user1.get("email");
        String pass = (String) user1.get("password");
        String role = (String) user1.get("roles");

        ModelMap model = new ModelMap();

        if (pass.isEmpty() || pass.equals("")) {
            pass = userService.getUser(id).getPassword();
        }

        User loginedUser = getCurrentUser();
        User editUser = new User(id, firstName, lastName, age, email, pass, forRole.getRoles());

        if (loginedUser.getId() == id) { // свою роль нельзя изменить
            User finalUser = userService.updateUser(loginedUser, null);
            model.addAttribute("user", finalUser);
            return ResponseEntity.ok(model);
        }
        User finalUser = userService.updateUser(editUser, role);
        model.addAttribute("user", finalUser);
        return ResponseEntity.ok(model);
    }


    //save
    @PostMapping(value = "/admin")
    public ResponseEntity<?> addNewUser(@RequestBody Map<String, String> param) {

        String email = param.get("email");

        User user = userService.getUserByEmail(email);
        if (user != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String firstName = param.get("firstName");
            String lastName = param.get("lastName");
            int age = Integer.parseInt(param.get("age"));
            String pass = param.get("password");
            String role = param.get("role").toUpperCase();
            List<Role> roleList = new ArrayList<>();
            Role toRole = new Role();

            user = new User(firstName, lastName, age, email, pass);
            if (role.equals("ADMIN")) {
                toRole.setValue(role);
                toRole.setUser(user);
                roleList.add(toRole);
                Role userRole = new Role();
                userRole.setValue("USER");
                userRole.setUser(user);
                roleList.add(userRole);
                user.setRoles(roleList);
            } else {
                toRole.setValue(role);
                toRole.setUser(user);
                roleList.add(toRole);
                user.setRoles(roleList);
            }

            userService.saveUser(user);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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