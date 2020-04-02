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
    public String updateUser(@Valid @ModelAttribute("usr") User user1, ModelMap model, @RequestParam Map<String, String> param) {
        Long id = user1.getId();
        User editUser = userService.getUser(id);
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        int age = Integer.parseInt(param.get("age"));
        String email = param.get("email");
        String pass = param.get("password");
        String role = param.get("role-edit");
        Long curId;
        String curRole = "ADMIN";

        User loginedUser = getCurrentUser();

        if (loginedUser == null) {
            return "error";
        }

        curId = loginedUser.getId();

        if (role == null || role.equals("")) {
            for (Role r : editUser.getRoles()) {
                if (r.getValue().contains("ADMIN")) {
                    role = r.getValue();
                    break;
                } else {
                    role = r.getValue();
                    break;
                }
            }
        }

        if (pass.isEmpty() || pass.equals("")) {
            pass = editUser.getPassword();
        }
        if (curId == id) { //изменить свою запись
            // роль оставить прежней
            userService.updateUser(new User(id, firstName, lastName, age, email, pass, loginedUser.getRoles()));
            return "redirect:/admin/list";

        } else { // редактировать чужую запись
            if (!curRole.equals(role)) { // изменить данные другого пользователя на user
                for (Role r : editUser.getRoles()) {
                    if (r.getValue().equals(curRole)) { // если пользователь был админом - лишить прав
                        editUser.getAuthorities().clear();
                        editUser.getRoles().clear();
                        userService.deleteRole(id);

                        Role newRole = new Role();
                        newRole.setValue(role);
                        newRole.setUser(new User(id, firstName, lastName, age, email, pass));
                        editUser.getRoles().add(newRole);
                        userService.updateUser(new User(id, firstName, lastName, age, email, pass, editUser.getRoles()));
                        return "redirect:/admin/list";
                    }
                }
                //если были права только user - роль не обновлять
                userService.updateUser(new User(id, firstName, lastName, age, email, pass, editUser.getRoles()));
                return "redirect:/admin/list";
            } else { // добавляет пользователю права админа
                for (Role r : editUser.getRoles()) { // если были права админа - не изменять роль
                    if (r.getValue().equals(curRole)) {
                        userService.updateUser(new User(id, firstName, lastName, age, email, pass, editUser.getRoles()));
                        return "redirect:/admin/list";
                    }
                }
                Role newRole = new Role();
                newRole.setValue(role);
                newRole.setUser(new User(id, firstName, lastName, age, email, pass));
                editUser.getRoles().add(newRole);
                userService.updateUser(new User(id, firstName, lastName, age, email, pass, editUser.getRoles()));
                return "redirect:/admin/list";
            }
        }
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