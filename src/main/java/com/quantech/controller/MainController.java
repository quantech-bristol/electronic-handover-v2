package com.quantech.controller;

import com.quantech.misc.AuthFacade.Authenticationfacade;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class MainController {

    // TODO: I'm using this admin as a placeholder for a lot of these mappings - separate out later.
    // TODO: Decide what URLS + mappings we need.
    // TODO: Login.
    @Autowired
    IAuthenticationFacade authenticator;

    @GetMapping("/")
    public String viewHome(HttpServletRequest request)
    {
        UserDetails userInfo =  (UserDetails)authenticator.getAuthentication().getPrincipal();
        for (GrantedAuthority g: userInfo.getAuthorities())
        {
            if (g.getAuthority().matches("Admin")){ return "redirect:/Admin";}
            else if (g.getAuthority().matches( "Doctor")){return "redirect:/quantech";}
        }
        return "redirect:/login";
    }

    @RequestMapping(value="quantech")
    public String docHome() {return "/quantech";}

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
