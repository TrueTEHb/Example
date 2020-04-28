package myApp.controller;


import myApp.model.User;
import myApp.rest.RestService;
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
    private RestService restService;

    @GetMapping(value = "/user/list")
    public ResponseEntity<?> userPage() {
        ModelMap model = new ModelMap();
        model.addAttribute("user", getCurrentUser());
        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/user")
    public ModelAndView viewPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("userPage");
        return model;
    }

    public User getCurrentUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authUser.getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            User loginedUser = restService.getUserByEmail(email);
            return loginedUser;
        } else {
            return null;
        }
    }

}
