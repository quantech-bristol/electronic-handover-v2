package com.quantech.patient;

import com.quantech.misc.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class PatientsController extends WebMvcConfigurerAdapter {
    // TODO: Decide what URLS + mappings we need.

    @Autowired
    private DatabaseService dbs;

    // Go to page to add patient.
    @GetMapping("/addPatient")
    public String addPatient(Model model) {
        model.addAttribute("doctors",dbs.allDoctors());
        model.addAttribute("patient", new Patient());
        model.addAttribute("wards", dbs.allWards());
        return "addPatient";
    }

    // Submit a new patient.
    @PostMapping("/patient")
    public String submitPatient(@ModelAttribute Patient patient) {
        dbs.savePatient(patient);
        return "patientView";
    }

    // View all patients in the system.
    @GetMapping("/patient/all")
    public String viewPatient(Model model) {
        model.addAttribute("patients",dbs.allPatients());
        return "viewPatients";
    }

    // TODO: Figure out how each patient maps to their own URL (ID? NHS number? etc - probably ID is best.)
    // View patient of specific id
    @GetMapping("/patient/hospitalNumber={id}")
    public String viewAllPatients(@PathVariable Long id, Model model) {
        model.addAttribute("patient", dbs.getPatientByHospitalNumber(id));
        return "patientView";
    }

    //Methods to edit patient's information/flag them.

    // Filter list of patients.

    // Sort list of patients.

    // Search list of patients.
}
