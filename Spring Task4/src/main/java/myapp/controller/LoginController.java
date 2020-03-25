package myapp.controller;

import myapp.model.Role;
import myapp.model.User;
import myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/")
    public String startPage(@RequestParam Map<String, String> param) {

        if (param.get("/new") != null) {
            return showNewForm();
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/login")
    public String authUser(@RequestParam Map<String, String> param, ModelMap model) {
        String name = param.get("j_username");
        String password = param.get("j_password");

        String message = param.get("logout");
        if (message != null) {
            model.addAttribute("message", "You have been logged out.");
            return "login";
        }

        User user = userService.getUserByNamePass(name, password);
        if (user != null) {
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getValue().equals("ADMIN")) {
                    return "redirect:/admin";
                } else if (role.getValue().equals("USER")) {
                    return "redirect:/user";
                }
            }
        }
        model.addAttribute("msg", "Wrong name of password");
        return "error";
    }

    @RequestMapping(value = "/new")
    public String showNewForm() {
        return "PersonForm";
    }

    @RequestMapping(value = {"/insert"})
    public String addNewUser(@RequestParam Map<String, String> param, ModelMap model) {
        String name = param.get("name");
        String password = param.get("password");

        User user = userService.getUserByName(name);
        if (user != null) {
            model.addAttribute("error", "This username is already exists");
            return "error";
        } else {
            userService.addUser(new User(name, password));
            return "login";
        }
    }
}