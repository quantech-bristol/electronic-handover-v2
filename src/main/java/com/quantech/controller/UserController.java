package com.quantech.controller;

import com.quantech.entities.user.UserCore;
import com.quantech.entities.user.UserService;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
    public String createUser(@Valid @ModelAttribute("usercore") UserCore user, BindingResult result, RedirectAttributes model, Errors errors) {
        if (errors.hasErrors()) {return "/Admin/createUser";}
        else {
            userService.saveUser(user);
            return "redirect:/Admin";
        }
    }

    @RequestMapping(value = "/Admin")
    public String adminPage(Model model)
    {
        UserDetails userInfo =  (UserDetails)authenticator.getAuthentication().getPrincipal();
        model.addAttribute("username",userInfo.getUsername());
        return "/Admin/adminScreen";
    }

}
