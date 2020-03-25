package myapp.controller;

import myapp.model.Role;
import myapp.model.User;
import myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/admin")
    public String adminPage(ModelMap model) {

        String name = null;
        String role = null;
        User user = null;

        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authUser.getPrincipal();
        if (principal instanceof UserDetails) {
            name = ((UserDetails) principal).getUsername();
            user = userService.getUserByName(name);

            Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
            for (GrantedAuthority auth : authorities) {
                if (auth.getAuthority().equals("ADMIN")) {
                    role = auth.getAuthority();
                    break;
                }
            }
        }
        if (user == null) {
            model.addAttribute("msg", "Not exist");
            return "error";
        }
        model.addAttribute("u_name", name);
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        model.addAttribute("people", userService.getAllUsers());
        return "adminPage";
    }

    @RequestMapping(value = "/admin/list")
    public String listUser(ModelMap model) {
        User logginedUser = getCurrentUser();
        model.addAttribute("u_name", logginedUser.getUsername());
        model.addAttribute("people", userService.getAllUsers());
        return "adminPage";
    }

    @RequestMapping(value = "/admin/delete")
    public String deleteUser(@RequestParam Long id, ModelMap model) {
        User logginedUser = getCurrentUser();
        Long curId = logginedUser.getId();

        if (id != curId) {
            userService.deleteRole(id);
            userService.deleteUser(id);

            model.addAttribute("u_name", logginedUser.getUsername());
            model.addAttribute("people", userService.getAllUsers());
            return "adminPage";
        } else if (id == curId) {
            userService.deleteUser(id);
            model.addAttribute("message", "Your account has been successfully deleted.");
            return "login";
        } else {
            model.addAttribute("msg", "Problem with deleting account");
            return "error";
        }
    }

    @RequestMapping(value = "/admin/edit")
    public String editUser(@RequestParam Long id, ModelMap model) {

        if (id != null) {
            User user = userService.getUser(id);
            model.addAttribute("id", id);
            model.addAttribute("user", user);
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                model.addAttribute("role", role.getValue());
            }
            return "PersonForm";
        } else {
            model.addAttribute("msg", "Problem with editing account");
            return "error";
        }
    }

    @RequestMapping(value = "/admin/update")
    public String updateUser(@RequestParam Long id, @RequestParam Map<String, String> param, ModelMap model) {
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
                model.addAttribute("u_name", loginedUser.getUsername());
                model.addAttribute("people", userService.getAllUsers());
                return "adminPage";
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

                        model.addAttribute("role", curRole);
                        model.addAttribute("u_name", loginedUser.getUsername());
                        model.addAttribute("people", userService.getAllUsers());

                        return "adminPage";
                    }
                }

                model.addAttribute("u_name", loginedUser.getUsername());
                model.addAttribute("people", userService.getAllUsers());
                userService.updateUser(new User(id, name, pass, editUser.getRoles()));

                return "userPage";
            } else { // добавляет пользователю права админа
                for (Role r : editUser.getRoles()) { // если были права админа - не изменять роль
                    if (r.getValue().equals(curRole)) {

                        model.addAttribute("u_name", loginedUser.getUsername());
                        model.addAttribute("people", userService.getAllUsers());
                        userService.updateUser(new User(id, name, pass, editUser.getRoles()));
                        return "adminPage";
                    }
                }
                Role newRole = new Role();
                newRole.setValue(role);
                newRole.setUser(new User(id, name, pass, Collections.singletonList(newRole)));
                editUser.setRoles(Collections.singletonList(newRole));
                userService.updateUser(new User(id, name, pass, editUser.getRoles()));

                model.addAttribute("u_name", loginedUser.getUsername());
                model.addAttribute("people", userService.getAllUsers());
                return "adminPage";
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
