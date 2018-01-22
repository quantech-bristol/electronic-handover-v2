package com.quantech.controller;

import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.handover.HandoverService;
import com.quantech.entities.patient.PatientService;
import com.quantech.entities.handover.Handover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class HandoverController extends WebMvcConfigurerAdapter {
    // TODO: Decide what URLS + mappings we need.

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private HandoverService handoverService;

    // Go to view to create new handover.
    @GetMapping("/createHandover")
    public String createHandover(Model model) {
        //TODO: Need to refine this to only the doctors patients, not all patients.
        model.addAttribute("patients",patientService.getAllPatients());
        model.addAttribute("handover", new Handover());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "createHandover";
    }

    // Submit the new handover
    @PostMapping("/handover")
    public String submitHandover(@ModelAttribute Handover handover, Model model) {
        handoverService.saveHandover(handover);
        model.addAttribute("handovers", handoverService.getAllHandovers());
        return "viewHandovers";
    }

    // View all handovers, uses the viewPatients template
    @GetMapping("/viewHandovers")
    public String viewHandovers(Model model) {
        model.addAttribute("handovers", handoverService.getAllHandovers());
        return "viewHandovers";
    }

    // View all handovers given NHS, hospital, name etc ...

    // Cancel sent handover.

    // View all incoming handovers.

    // Accept incoming handover.

    // Reject incoming handover.

    // Reroute incoming handover(?)
}
