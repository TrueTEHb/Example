package myapp.controller;

import myapp.model.Role;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin**")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/admin")
    public String adminPage(@RequestParam Map<String, String> param, ModelMap model) {

        String name = null;
        User user = null;

        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authUser.getPrincipal();
        if (principal instanceof UserDetails) {
            name = ((UserDetails) principal).getUsername();
            user = userService.getUserByName(name);
        }
        if (user == null) {
            model.addAttribute("msg", "Not exist");
            return "error";
        }
        model.addAttribute("u_name", name);
        model.addAttribute("user", user);
        model.addAttribute("role", "ADMIN");
        return "userPage";
    }

    @RequestMapping(value = "/list")
    public String listUser(ModelMap model) {
        model.addAttribute("people", userService.getAllUsers());
        return "adminPage";
    }

    @RequestMapping(value = "/delete")
    public String deleteUser(@RequestParam Long id, ModelMap model) {
        User logginedUser = getCurrentUser();
        Long curId = logginedUser.getId();

        if (id != curId) {
            userService.deleteUser(id);
            model.addAttribute("u_name", logginedUser.getUsername());
            model.addAttribute("user", logginedUser);
            List<Role> roles = logginedUser.getRoles();
            for (Role role : roles) {
                if (role.getValue().equals("ADMIN")) {
                    model.addAttribute("role", role.getValue());
                } else {
                    model.addAttribute("role", role.getValue());
                }
            }
            return "userPage";
        } else if (id == curId) {
            userService.deleteUser(id);
            model.addAttribute("message", "Your account has been successfully deleted.");
            return "login";
        } else {
            model.addAttribute("msg", "Problem with deleting account");
            return "error";
        }
    }

    @RequestMapping(value = "/edit")
    public String editUser(@RequestParam Long id, ModelMap model) {

        if (id != null) {
            User user = userService.getUser(id);
            model.addAttribute("id", id);
            model.addAttribute("user", user);
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getValue().equals("ADMIN")) {
                    model.addAttribute("role", role.getValue());
                } else {
                    model.addAttribute("role", role.getValue());
                }
            }
            return "PersonForm";
        } else {
            model.addAttribute("msg", "Problem with editing account");
            return "error";
        }
    }

    @RequestMapping(value = "/update")
    public String updateUser(@RequestParam Long id, @RequestParam Map<String, String> param, ModelMap model) {
        User editUser = userService.getUser(id);
        String name = param.get("name");
        String pass = param.get("password");
        String role = param.get("role").toUpperCase();
        Long curId = null;
        String curRole = "ADMIN";

        User loginedUser = getCurrentUser();
        if (loginedUser == null) {
            return "error";
        }
        curId = loginedUser.getId();

        if (curId == id) { //изменить свою у запись
            if (curRole == role) { // роль оставить прежней
                userService.updateUser(new User(id, name, pass, loginedUser.getRoles()));
                model.addAttribute("u_name", name);
                model.addAttribute("user", editUser);
                model.addAttribute("role", role);
                return "userPage";
            } else {
                // удалить роль ADMIN для данного пользователя
                userService.deleteRole(id);
                Role newRole = new Role();
                newRole.setValue(role);
                newRole.setUser(new User(id, name, pass, Collections.singletonList(newRole)));
                editUser.setRoles(Collections.singletonList(newRole));
                userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                model.addAttribute("u_name", name);
                model.addAttribute("user", editUser);
                model.addAttribute("role", role);
                return "userPage";
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
                        editUser.setRoles(Collections.singletonList(newRole));
                        userService.updateUser(new User(id, name, pass, editUser.getRoles()));

                        model.addAttribute("u_name", loginedUser.getUsername());
                        model.addAttribute("user", loginedUser);
                        model.addAttribute("role", curRole);

                        return "userPage";
                    }
                }
                model.addAttribute("u_name", loginedUser.getUsername());
                model.addAttribute("user", loginedUser);
                model.addAttribute("role", curRole);
                userService.updateUser(new User(id, name, pass, editUser.getRoles()));

                return "userPage";
            } else { // добавляет пользователю права админа
                for (Role r : editUser.getRoles()) { // если были права админа - не изменять роль
                    if (r.getValue().equals(curRole)) {
                        model.addAttribute("u_name", loginedUser.getUsername());
                        model.addAttribute("user", loginedUser);
                        model.addAttribute("role", curRole);
                        userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                        return "userPage";
                    }
                }
                Role newRole = new Role();
                newRole.setValue(role);
                newRole.setUser(new User(id, name, pass, Collections.singletonList(newRole)));
                editUser.setRoles(Collections.singletonList(newRole));
                userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                model.addAttribute("u_name", loginedUser.getUsername());
                model.addAttribute("user", loginedUser);
                model.addAttribute("role", curRole);
                return "userPage";
            }
        }
    }

    public User getCurrentUser() { // получить данные залогиненного пользователя
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
