package myApp.controller;

import myApp.model.Role;
import myApp.model.User;
import myApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/admin/list")
    public String listUser(ModelMap model) {
        User logginedUser = getCurrentUser();
        model.addAttribute("people", userService.getAllUsers());
        model.addAttribute("user", logginedUser);
        return "adminPage";
    }

    @RequestMapping(value = "/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, ModelMap model) {
        User logginedUser = getCurrentUser();
        Long curId = logginedUser.getId();

        if (id != curId) {
            userService.deleteRole(id);
            userService.deleteUser(id);

            return "redirect:/admin/list";
        } else if (id == curId) {
            userService.deleteUser(id);
            model.addAttribute("message", "Your account has been successfully deleted.");
            return "login";
        } else {
            model.addAttribute("msg", "Problem with deleting account");
            return "error";
        }
    }

    @RequestMapping(value = "/admin/update")
    public String updateUser(@Valid @ModelAttribute("usr") User user1, @RequestParam Map<String, String> param) {
        Long id = Long.valueOf(param.get("id"));
        User forRole = userService.getUser(id);
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        int age = Integer.parseInt(param.get("age"));
        String email = param.get("email");
        String pass = param.get("password");
        String role = param.get("role-edit");

        if (pass.isEmpty() || pass.equals("")) {
            pass = userService.getUser(id).getPassword();
        }

        User loginedUser = getCurrentUser();
        User editUser = new User(id, firstName, lastName, age, email, pass, forRole.getRoles());

        if (loginedUser.getId() == id){ // свою роль нельзя изменить
            userService.updateUser(loginedUser, null);
            return "redirect:/admin/list";
        }
        userService.updateUser(editUser, role);

        return "redirect:/admin/list";
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