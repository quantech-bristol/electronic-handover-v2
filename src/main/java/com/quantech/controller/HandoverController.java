package com.quantech.controller;

import com.quantech.business.data.DatabaseService;
import com.quantech.business.data.Handover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class HandoverController extends WebMvcConfigurerAdapter{
    // TODO: Decide what URLS + mappings we need.

    @Autowired
    private DatabaseService dbs;

    // Go to view to create new handover.
    @GetMapping("/createHandover")
    public String createHandover(Model model) {
        //TODO: Need to refine this to only the doctors patients, not all patients.
        model.addAttribute("patients",dbs.allPatients());
        model.addAttribute("handover", new Handover());
        return "createHandover";
    }

    // View all sent handovers.

    // Cancel sent handover.

    // View all incoming handovers.

    // Accept incoming handover.

    // Reject incoming handover.

    // Reroute incoming handover(?)
}
