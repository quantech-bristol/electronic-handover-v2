package com.quantech.controller;

import com.quantech.facade.FacadeService;
import com.quantech.entities.handover.Handover;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FacadeService dbs;

    // Go to view to create new handover.
    @GetMapping("/createHandover")
    public String createHandover(Model model) {
        //TODO: Need to refine this to only the doctors patients, not all patients.
        model.addAttribute("patients", dbs.allPatients());
        model.addAttribute("handover", new Handover());
        model.addAttribute("doctors", dbs.allDoctors());
        return "createHandover";
    }

    // Submit the new handover
    @PostMapping("/handover")
    public String submitHandover(@ModelAttribute Handover handover, Model model) {
        dbs.saveHandover(handover);
        model.addAttribute("handovers", dbs.allHandovers());
        return "viewHandovers";
    }

    // View all handovers, uses the viewPatients template
    @GetMapping("/viewHandovers")
    public String viewHandovers(Model model) {
        model.addAttribute("handovers", dbs.allHandovers());
        return "viewHandovers";
    }

    // View all sent handovers.

    // Cancel sent handover.

    // View all incoming handovers.

    // Accept incoming handover.

    // Reject incoming handover.

    // Reroute incoming handover(?)
}
