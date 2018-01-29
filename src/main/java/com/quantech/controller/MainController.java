package com.quantech.controller;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.entities.user.UserCore;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;



@Controller
public class MainController {

    // TODO: I'm using this admin as a placeholder for a lot of these mappings - separate out later.
    // TODO: Decide what URLS + mappings we need.
    // TODO: Login.
    @Autowired
    IAuthenticationFacade authenticator;

    @GetMapping("/")
    public String viewHome(HttpServletRequest request) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        if (userInfo.hasAuth(SecurityRoles.Doctor)) {
            return "redirect:/quantech";
        }
        else if (userInfo.hasAuth(SecurityRoles.Admin)){
            return "/Admin/adminScreen";
        }

        return "redirect:/login";
    }

    @RequestMapping(value="/quantech")
    public String docHome() { return "redirect:/quantech"; }

    @RequestMapping(value={"/login"})
    public String login()
    {
        return "login";
    }

    @RequestMapping(value="/403")
    public String Error403()
    {
        return "403";
    }

    // Create new team.

    // Add doctors to existing team.

    // Remove doctors from existing team.

    // Delete team.

    // Deactivate/delete account.
}
