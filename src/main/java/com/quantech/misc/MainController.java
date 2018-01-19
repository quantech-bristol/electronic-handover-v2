package com.quantech.misc;

import com.quantech.misc.DatabaseService;
import com.quantech.doctor.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @Autowired
    private DatabaseService dbs;

    // TODO: I'm using this admin as a placeholder for a lot of these mappings - separate out later.
    // TODO: Decide what URLS + mappings we need.
    // TODO: Login.

    @GetMapping("/")
    public String viewHome() {
        return "quantech";
    }

    // Go to page to add a new doctor.
    @GetMapping("/addDoctor")
    public String addDoctor(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "addDoctor";
    }

    // Submit the new doctor.
    // TODO: Do not go to /doctor, instead redirect to the home page.
    @PostMapping("/doctor")
    public String submitDoctor(@ModelAttribute Doctor doctor) {
        dbs.saveDoctor(doctor);
        return "quantech";
    }

    // Search doctors.

    // Sort doctors.

    // Filter doctors.

    // Remove doctor status (?) from a doctor.

    // Methods to edit doctor information.

    // Create new team.

    // Add doctors to existing team.

    // Remove doctors from existing team.

    // Delete team.

    // Deactivate/delete account.
}
