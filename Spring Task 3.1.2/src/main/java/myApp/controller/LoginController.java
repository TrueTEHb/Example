package myApp.controller;


import myApp.model.Role;
import myApp.model.User;
import myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = {"/login", "/"})
    public String authUser(@RequestParam Map<String, String> param, ModelMap model) {

        String message = param.get("logout");
        if (message != null) {
            model.addAttribute("message", "You have been logged out.");
            return "login";
        }
        if (param.get("error") != null){
            model.addAttribute("msg", "Wrong name of password");
            return "login";
        }
        return "login";

    }


    @RequestMapping(value = {"/insert"})
    public String addNewUser(@RequestParam Map<String, String> param, ModelMap model) {

        String email = param.get("email");

        User user = userService.getUserByEmail(email);
        if (user != null) {

            model.addAttribute("error", "This username is already exists");
            return "error";
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
            return "redirect:/admin/list";
        }
    }
}