package myapp.controller;

import myapp.model.User;
import myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user")
    public String adminPage(@RequestParam Map<String, String> param, ModelMap model) {
        String name = param.get("j_username");
        String password = param.get("j_password");
        User user = userService.getUserByNamePass(name, password);
        if (user == null) {
            Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authUser.getPrincipal();
            if (principal instanceof UserDetails) {
                name = ((UserDetails) principal).getUsername();
                user = userService.getUserByName(name);
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("u_name", name);
        model.addAttribute("role", "USER");

        return "userPage";
    }
}
