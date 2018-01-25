package com.quantech.controller;

import com.quantech.entities.user.UserCore;
import com.quantech.entities.user.UserService;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        model.addAttribute("postUrl", "/Admin/createUser");
        model.addAttribute("title", "CreateUser");
        return "/Admin/createUser";
    }

    @PostMapping("/Admin/createUser")
    public String createUser(@Valid @ModelAttribute("usercore") UserCore user, BindingResult result, Model model, Errors errors) {
        if (errors.hasErrors())
        {
            model.addAttribute("postUrl", "/Admin/createUser");
            model.addAttribute("title", "CreateUser");
            return "/Admin/createUser";
        }
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

    @GetMapping(value = "/MyUser")
    public String editSelfSettings(Model model)
    {
       UserCore user  =  (UserCore) authenticator.getAuthentication().getPrincipal();
       model.addAttribute("postUrl", "/MyUser");
       model.addAttribute("usercore",user);
       model.addAttribute("title", "Edit My Settings");
       return "/Admin/createUser";
    }

    @PostMapping(value = "/MyUser")
    public String editSelfSettings(@Valid @ModelAttribute("usercore") UserCore user, BindingResult result, RedirectAttributes model, Errors errors)
    {
        if (errors.hasErrors())
        {
            model.addAttribute("postUrl", "/MyUser");
            model.addAttribute("title", "Edit My Settings");
            return "/Admin/createUser";
        }
        else {
            ((UserCore) authenticator.getAuthentication().getPrincipal()).updateValues(user);
            userService.saveUser(user);
            return "redirect:/Admin";
        }
    }

    @GetMapping(value = "/Admin/EditUser/{id}")
    public String editUserSettings(Model model, @PathVariable("id")long id)
    {
        UserCore user  =  userService.findUserById(id);
        if (user == null){return "";}
        else
        model.addAttribute("postUrl", "/Admin/EditUser");
        model.addAttribute("usercore",user);
        model.addAttribute("title", "Edit " + user.getUsername() + " settings");
        return "/Admin/createUser";
    }

    @PostMapping(value = "/Admin/EditUser")
    public String editUserSettings(@Valid @ModelAttribute("usercore") UserCore userReturned, BindingResult result, RedirectAttributes model, Errors errors)
    {
        UserCore user  =  userService.findUserById(userReturned.getId());
        if (errors.hasErrors())
        {
            model.addAttribute("postUrl", "/Admin/EditUser");
            model.addAttribute("title", "Edit " + user.getUsername() + " settings");
            return "/Admin/createUser";
        }
        else {
            userService.saveUser(userReturned);
            return "redirect:/Admin";
        }
    }

}
