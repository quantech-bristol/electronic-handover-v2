package com.quantech.controller;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.user.UserCore;
import com.quantech.entities.user.UserFormBackingObject;
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

    @Autowired
    DoctorService doctorService;

    // Go to view to create new handover.
    @GetMapping("/Admin/createUser")
    public String createUser(Model model) {
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("postUrl", "/Admin/createUser");
        model.addAttribute("title", "CreateUser");
        model.addAttribute("ShowRoles", true);
        return "/Admin/createUser";
    }

    @PostMapping("/Admin/createUser")
    public String createUser(@Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Model model, Errors errors) {
        user.CheckValidity(userService,result,true);
        if (errors.hasErrors())
        {
            model.addAttribute("postUrl", "/Admin/createUser");
            model.addAttribute("title", "CreateUser");
            model.addAttribute("ShowRoles", true);
            return "/Admin/createUser";
        }
        else {
            UserCore newUser = user.ToUserCore();
            userService.saveUser(newUser);
            if(newUser.hasAuth(SecurityRoles.Doctor)) {
                Doctor newDoc = new Doctor(newUser);
                doctorService.saveDoctor(newDoc);
            }
            return "redirect:/";
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
       model.addAttribute("usercore",new UserFormBackingObject(user));
       model.addAttribute("title", "Edit My Settings");
        model.addAttribute("ShowRoles", false);
       return "/Admin/createUser";
    }

    @PostMapping(value = "/MyUser")
    public String editSelfSettings(@Valid @ModelAttribute("usercore") UserFormBackingObject userFormBackingObject, BindingResult result, RedirectAttributes model, Errors errors)
    {
        userFormBackingObject.CheckValidity(userService,result, false);
        if (errors.hasErrors())//If we have any errors, send them back to the page. result + model stays with them.
        {
            model.addAttribute("postUrl", "/MyUser");
            model.addAttribute("title", "Edit My Settings");
            model.addAttribute("ShowRoles", false);
            return "/Admin/createUser";
        }

        else {//Otherwise
            UserCore activeUser = (UserCore) authenticator.getAuthentication().getPrincipal();//Get the current users usercore
            userFormBackingObject.setAuthorityStrings(activeUser.getAuthorityStrings());
            activeUser.updateValues(userFormBackingObject);//give it the new values
            userService.saveUser(activeUser);//Save it to database
            return "redirect:/";//Redirect them.
        }
    }

    @GetMapping(value = "/Admin/EditUser")
    public String editUserSettings(Model model, @RequestParam("Id")long id)
    {
        UserCore requestingUser = (UserCore) authenticator.getAuthentication().getPrincipal();
        if (requestingUser.getId() == id){return "redirect:/MyUser";}

        UserCore user  =  userService.findUserById(id);
        if (user == null){return "/403";}
        else {
            model.addAttribute("usercore", new UserFormBackingObject(user));
            model.addAttribute("postUrl", "/Admin/EditUser");
            model.addAttribute("title", "Edit " + user.getUsername() + " settings");
            model.addAttribute("ShowRoles", true);
        }
        return "/Admin/createUser";
    }

    @PostMapping(value = "/Admin/EditUser")
    public String editUserSettings(@Valid @ModelAttribute("usercore") UserFormBackingObject userFormBackingObject, BindingResult result, RedirectAttributes model, Errors errors)
    {
        UserCore user  =  userService.findUserById(userFormBackingObject.getId());
        userFormBackingObject.CheckValidity(userService,result, false);
        if (errors.hasErrors())
        {
            model.addAttribute("postUrl", "/Admin/EditUser");
            model.addAttribute("title", "Edit " + user.getUsername() + " settings");
            model.addAttribute("ShowRoles", true);
            return "/Admin/createUser";
        }
        else {
            user.updateValues(userFormBackingObject);
            userService.saveUser(user);
            return "redirect:/";
        }
    }

}
