package com.quantech.controller;

import com.quantech.entities.user.UserCore;
import com.quantech.entities.user.UserService;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController  extends WebMvcConfigurerAdapter {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    UserService userService;

    // Go to view to create new handover.
    @GetMapping("/Admin/createUser")
    public String createUser(Model model) {
        model.addAttribute("usercore", new UserCore());
        return "/Admin/createUser";
    }

    @PostMapping("/Admin/createUser")
    public String createUser(@ModelAttribute UserCore user, RedirectAttributes model) {
        userService.saveUser(user);
        return "redirect:/Admin";
    }

    @RequestMapping(value = "/Admin")
    public String adminPage(Model model)
    {
        UserDetails userInfo =  (UserDetails)authenticator.getAuthentication().getPrincipal();
        model.addAttribute("username",userInfo.getUsername());
        return "/Admin/adminScreen";
    }

}
