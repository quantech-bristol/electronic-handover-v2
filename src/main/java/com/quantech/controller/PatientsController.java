package com.quantech.controller;

import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.patient.PatientService;
import com.quantech.entities.ward.WardService;
import com.quantech.entities.patient.Patient;
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
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private WardService wardService;

    // Go to page to add patient.
    @GetMapping("/addPatient")
    public String addPatient(Model model) {
        model.addAttribute("doctors",doctorService.getAllDoctors());
        model.addAttribute("patient", new Patient());
        model.addAttribute("wards", wardService.getAllWards());
        return "addPatient";
    }

    // Submit a new patient.
    @PostMapping("/patient")
    public String submitPatient(@ModelAttribute Patient patient, Model model) {
        patientService.savePatient(patient);
        doctorService.addPatient(patient, patient.getDoctor());
        model.addAttribute("patients", patientService.getAllPatients());
        return "viewPatients";
    }

    // Send to homepage
    @GetMapping("/patient")
    public String patient() {
        return "viewPatients";
    }

    // View all patients in the system.
    @GetMapping("/patient/all")
    public String viewPatient(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "viewPatients";
    }

    // TODO: Figure out how each patient maps to their own URL (ID? NHS number? etc - probably ID is best.)
    // View patient of specific id
    @GetMapping("/patient/hospitalNumber={id}")
    public String viewAllPatients(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientByHospitalNumber(id));
        return "viewPatient";
    }

    // Methods to edit patient's information/flag them.

    // Filter list of patients.

    // Sort list of patients.

    // Search list of patients.
}
