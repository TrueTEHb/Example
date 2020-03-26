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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/admin/list")
    public String listUser(ModelMap model) {
        User logginedUser = getCurrentUser();
        model.addAttribute("u_name", logginedUser.getUsername());
        model.addAttribute("people", userService.getAllUsers());
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

    @RequestMapping(value = "/admin/edit/{id}")
    public String editUser(@PathVariable("id") Long id, ModelMap model) {

        if (id != null) {
            User user = userService.getUser(id);
            model.addAttribute("id", id);
            model.addAttribute("user", user);
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                model.addAttribute("role", role.getValue());
            }
            return "personForm";
        } else {
            model.addAttribute("msg", "Problem with editing account");
            return "error";
        }
    }

    @RequestMapping(value = "/admin/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @RequestParam Map<String, String> param, ModelMap model) {
        User editUser = userService.getUser(id);
        String name = param.get("name");
        String pass = param.get("password");
        String role = param.get("role").toUpperCase();
        Long curId;
        String curRole = "ADMIN";

        User loginedUser = getCurrentUser();
        if (loginedUser == null) {
            return "error";
        }
        curId = loginedUser.getId();

        if (curId == id) { //изменить свою запись
            if (curRole == role) { // роль оставить прежней
                userService.updateUser(new User(id, name, pass, loginedUser.getRoles()));
                return "redirect:/admin/list";
            } else {
                // удалить роль ADMIN для самого себя нельзя
                userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                return "redirect:/admin/list";
            }
        } else { // редактировать чужую запись

            if (!curRole.equals(role)) { // изменить данные другого пользователя на user
                for (Role r : editUser.getRoles()) {
                    if (r.getValue().equals(curRole)) { // если пользователь был админом - лишить прав
                        editUser.getAuthorities().clear();
                        editUser.getRoles().clear();
                        userService.deleteRole(id);

                        Role newRole = new Role();
                        newRole.setValue(role);
                        newRole.setUser(new User(id, name, pass, Collections.singletonList(newRole)));
                        editUser.getRoles().add(newRole);
                        userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                        return "redirect:/admin/list";
                    }
                }
                userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                return "redirect:/admin/list";
            } else { // добавляет пользователю права админа
                for (Role r : editUser.getRoles()) { // если были права админа - не изменять роль
                    if (r.getValue().equals(curRole)) {
                        userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                        return "redirect:/admin/list";
                    }
                }
                Role newRole = new Role();
                newRole.setValue(role);
                newRole.setUser(new User(id, name, pass, Collections.singletonList(newRole)));
                editUser.getRoles().add(newRole);
                userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                return "redirect:/admin/list";
            }
        }
    }

    public User getCurrentUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authUser.getPrincipal();
        if (principal instanceof UserDetails) {
            String name = ((UserDetails) principal).getUsername();
            User loginedUser = userService.getUserByName(name);
            return loginedUser;
        } else {
            return null;
        }
    }
}