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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String viewAllPatients(Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        if (userInfo.hasAuth(SecurityRoles.Doctor)){
            model.addAttribute("patients", doctorService.getPatients(userInfo.getId()));
        }
        else { // For now; this should really only be when the user is an admin.
            model.addAttribute("patients", patientService.getAllPatients());
        }
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

    // Sort list of patients.
    @GetMapping("/patient/allSortedBy={sort}")
    public String viewAllPatientsSorted(@PathVariable String sort, Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        List<Patient> patientList;
        if (userInfo.hasAuth(SecurityRoles.Doctor))
            patientList = doctorService.getPatients(userInfo.getId());
        else
            patientList = patientService.getAllPatients();

        if (sort.equals("firstName"))
            patientList = patientService.sortPatientsByFirstName(patientList);
        else if (sort.equals("lastName"))
            patientList = patientService.sortPatientsByLastName(patientList);

        model.addAttribute("patients",patientList);
        return "viewPatients";
    }
    // Search list of patients.
}
