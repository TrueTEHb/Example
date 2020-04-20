package myApp.controller;


import myApp.model.User;
import myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/list")
    public ResponseEntity<?> userPage() {
        ModelMap model = new ModelMap();
        String name;
        User user = null;

        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authUser.getPrincipal();
        if (principal instanceof UserDetails) {
            name = ((UserDetails) principal).getUsername();
            user = userService.getUserByEmail(name);
        }

        ModelAndView view = new ModelAndView();
        view.setViewName("userPage");

        model.addAttribute("user", user);
        model.addAttribute("role", "USER");
        model.addAttribute("view", view);
        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/user")
    public ModelAndView viewPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("userPage");
        return model;
    }
}
