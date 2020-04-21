package myApp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class LoginController {


    @RequestMapping(value = {"/", "login"})
    public String index(@RequestParam Map<String, String> param, ModelMap model) {

        if (param.get("logout") != null) {
            model.addAttribute("message", "You have been logged out.");
            return "login";
        }
        if (param.get("error") != null) {
            model.addAttribute("msg", "Wrong name of password");
            return "login";
        }
        if (param.get("email") != null && param.get("password") != null) {
            model.addAttribute("message", "it works");
        }

        return "login";
    }
}