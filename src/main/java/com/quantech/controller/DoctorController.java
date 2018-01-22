package com.quantech.controller;

import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
//import com.quantech.facade.FacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DoctorController extends WebMvcConfigurerAdapter {

    @Autowired
    private DoctorService doctorService;

    // Go to page to add a new doctor.
    @GetMapping("/addDoctor")
    public String addDoctor(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "addDoctor";
    }

    // Submit the new doctor.
    @PostMapping("/doctor")
    public String submitDoctor(@ModelAttribute Doctor doctor) {
        doctorService.saveDoctor(doctor);
        return "quantech";
    }

    // Send to homepage
    @GetMapping("/doctor")
    public String doctor() {
        return "redirect:/";
    }

    // Search doctors.

    // Sort doctors.

    // Filter doctors.

    // Remove doctor status (?) from a doctor.

    // Methods to edit doctor information.
}
