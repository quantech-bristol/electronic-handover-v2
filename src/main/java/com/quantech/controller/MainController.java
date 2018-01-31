package com.quantech.controller;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.handover.HandoverService;
import com.quantech.entities.patient.Patient;
import com.quantech.entities.patient.PatientService;
import com.quantech.entities.user.UserCore;
import com.quantech.entities.ward.WardService;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import org.springframework.ui.Model;


@Controller
public class MainController {

    // TODO: I'm using this admin as a placeholder for a lot of these mappings - separate out later.
    // TODO: Decide what URLS + mappings we need.
    // TODO: Login.

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private WardService wardService;
    @Autowired
    private HandoverService handoverService;

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
    public String docHome(Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        Doctor currentDoctor = doctorService.getDoctor(userInfo.getId());
        List<Patient> patients;
        patients = doctorService.getPatients(userInfo.getId());
        model.addAttribute("patients",patients);
        model.addAttribute("doctor",currentDoctor);
        model.addAttribute("handovers",handoverService.getAllToDoctor(currentDoctor));
        return "Doctor/quantech";
    }

    @RequestMapping(value={"/login"})
    public String login()
    {
        return "Misc/login";
    }

    // this method catches only when you manually navigate to /logout
    //  pressing the logout button somehow follows Spring's logout handler
    @RequestMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {

        // delete the sensitive patient data when user logs out
        File pdf = new File("pdfout.pdf");
        pdf.delete();

        // handle logout if previously logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value="/403")
    public String Error403()
    {
        return "Error/403";
    }

    // Create new team.

    // Add doctors to existing team.

    // Remove doctors from existing team.

    // Delete team.

    // Deactivate/delete account.
}
