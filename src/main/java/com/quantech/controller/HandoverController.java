package com.quantech.controller;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.handover.HandoverService;
import com.quantech.entities.patient.Patient;
import com.quantech.entities.patient.PatientService;
import com.quantech.entities.handover.Handover;
import com.quantech.entities.user.UserCore;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;


@Controller
public class HandoverController extends WebMvcConfigurerAdapter {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private HandoverService handoverService;

    @Autowired
    IAuthenticationFacade authenticator;

    // Go to view to create new handover.
    @GetMapping("/createHandover")
    public String createHandover(Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        Doctor currentDoctor = doctorService.getDoctor(userInfo.getId());
        model.addAttribute("currentDr",currentDoctor);
        List<Patient> ps = patientService.getAllDoctorsPatients(currentDoctor);
        List<Patient> patients = new ArrayList<>();
        for(Patient p : ps){
            if(handoverService.getAllActiveForPatient(p).isEmpty() && !p.getDischarged()) patients.add(p);
        }
        model.addAttribute("patients",patients);
        model.addAttribute("handover", new Handover());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "Doctor/createHandover";
    }

    // Submit the new handover
    @PostMapping("/handover")
    public String submitHandover(@ModelAttribute Handover handover, Model model) {
        handoverService.saveHandover(handover);
        return "redirect:/viewHandovers";
    }

    // View all handovers, uses the viewPatients template
    @GetMapping("/viewHandovers")
    public String viewHandovers(Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        Doctor currentDoctor = doctorService.getDoctor(userInfo.getId());
        model.addAttribute("doctor",currentDoctor);
        model.addAttribute("handovers", handoverService.getAllFromDoctor(currentDoctor));
        return "Doctor/viewHandovers";
    }

    // View pending handovers, uses the viewPatients template
    @GetMapping("/viewPendingHandovers")
    public String viewPendingHandovers(Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        Doctor currentDoctor = doctorService.getDoctor(userInfo.getId());
        model.addAttribute("doctor",currentDoctor);
        model.addAttribute("handovers",handoverService.getAllToDoctor(currentDoctor));

        return "Doctor/viewHandovers";
    }

    //Accept Pending Handover Requests
    @RequestMapping("/handover/accept")
    public String viewAllPatients(@RequestParam(value = "id", required=true) Long id, Model model) {
        Handover handover = handoverService.getHandover(id);
        handover.getPatient().setDoctor(handover.getRecipientDoctor());
        handover.setAccepted(true);
        handoverService.saveHandover(handover);
        return "redirect:/";
    }




    // View all handovers given NHS, hospital, name etc ...

    // Cancel sent handover.

    // View all incoming handovers.

    // Sort incoming handovers.

    // Filter incoming handovers.

    // Accept incoming handover.

    // Reject incoming handover.

    // Reroute incoming handover(?)

}
