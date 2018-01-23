package com.quantech.controller;

import com.quantech.entities.ward.Ward;
import com.quantech.entities.ward.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class WardController extends WebMvcConfigurerAdapter {

    @Autowired
    WardService wardService;

    // Add new ward.
    @GetMapping("/addWard")
    public String addWard(Model model) {
        model.addAttribute("ward", new Ward());
        return "addWard";
    }

    // Submit the new ward.
    @PostMapping("/addWard")
    public String submitWard(@ModelAttribute Ward ward) {
        wardService.saveWard(ward);
        return "redirect:/Admin";
    }

    // View all wards

    // Delete ward

    // Update ward information
}
