package com.quantech.controller;

import com.quantech.entities.handover.Handover;
import com.quantech.entities.user.UserCore;
import com.quantech.entities.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController  extends WebMvcConfigurerAdapter
{
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
        if (user.hasAuth("Doctor"))//This is within the user core object
        {
            model.addFlashAttribute("id",user.getId());
            return "redirect:/addDoctor";
        }
        else
        {
            userService.saveUser(user);
            return "redirect:/admin";
        }
    }
    @RequestMapping(value = "/admin")
    public String adminPage(){return "/Admin/adminScreen";}

}