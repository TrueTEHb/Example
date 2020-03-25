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

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user")
    public String adminPage(ModelMap model) {
        String name = null;
        User user = null;

        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authUser.getPrincipal();
        if (principal instanceof UserDetails) {
            name = ((UserDetails) principal).getUsername();
            user = userService.getUserByName(name);
        }

        model.addAttribute("user", user);
        model.addAttribute("u_name", name);
        model.addAttribute("role", "USER");

        return "userPage";
    }
}
