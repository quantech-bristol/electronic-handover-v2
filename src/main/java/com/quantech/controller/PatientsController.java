package com.quantech.controller;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.patient.PatientService;
import com.quantech.entities.user.UserCore;
import com.quantech.entities.ward.WardService;
import com.quantech.entities.patient.Patient;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Controller
public class PatientsController extends WebMvcConfigurerAdapter {
    // TODO: Decide what URLS + mappings we need.

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private WardService wardService;

    @Autowired
    IAuthenticationFacade authenticator;

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
        return ("redirect:/patient/all");
    }

    // Send to homepage - should we get rid of this?
    @GetMapping("/patient")
    public String patient() {
        return "viewPatients";
    }

    // View all patients in the system.
    @GetMapping("/patient/all")
    public String viewAllPatients(@RequestParam(value = "sortBy", required=false) String sort, Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        List<Patient> patients;
        if (userInfo.hasAuth(SecurityRoles.Doctor)){
            patients = doctorService.getPatients(userInfo.getId());
        }
        else { // For now; this should really only be when the user is an admin.
            patients = patientService.getAllPatients();
        }

        // Sort the values if the sort parameter is provided.
        if (sort != null) {
            if (sort.equals("firstName"))
                patients = patientService.sortPatientsByFirstName(patients);
            if (sort.equals("lastName"))
                patients = patientService.sortPatientsByLastName(patients);
        }

        model.addAttribute("patients",patients);
        return "viewPatients";
    }

    // TODO: Figure out how each patient maps to their own URL (ID? NHS number? etc - probably ID is best.)
    // View patient of specific id
    @GetMapping("/patient/hospitalNumber={id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientByHospitalNumber(id));
        return "viewPatient";
    }

    // Methods to edit patient's information/flag them.

    // Filter list of patients.

    // Search list of patients.
}
