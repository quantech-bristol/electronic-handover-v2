package com.quantech.controller;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.patient.PatientService;
import com.quantech.entities.user.UserCore;
import com.quantech.entities.ward.WardService;
import com.quantech.entities.patient.Patient;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.misc.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.*;
import java.util.function.Predicate;

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
        model.addAttribute("patient", new Patient());
        model.addAttribute("wards", wardService.getAllWards());
        return "Doctor/addPatient";
    }

    // Submit a new patient.
    @PostMapping("/patient")
    public String submitPatient(@ModelAttribute Patient patient, Model model) {
        UserCore userInfo =  (UserCore)authenticator.getAuthentication().getPrincipal();
        Doctor doc = doctorService.getDoctor(userInfo.getId());
        patient.setDoctor(doc);
        patientService.savePatient(patient);
        doctorService.addPatient(patient, patient.getDoctor());
        return ("redirect:/patient/all");
    }

    // Send to homepage - should we get rid of this?
    @GetMapping("/patient")
    public String patient() {
        return "Doctor/viewPatients";
    }

    // View all patients in the system.
    @GetMapping("/patient/all")
    public String viewAllPatients(@RequestParam(value = "sortBy", required=false) String sort,
                                  @RequestParam(value = "filterByFNPrefix", required = false) String fNameFilter,
                                  @RequestParam(value = "filterByLNPrefix", required = false) String lNameFilter,
                                  @RequestParam(value = "filterByWard", required = false) Long wardId,
                                  @RequestParam(value = "filterByBed", required = false) String bed,
                                  @RequestParam(value = "filterByDoc", required = false) Long docId,
                                  Model model) {
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

        // Create a set of predicates corresponding to the filters specified.
        Set<Predicate<Patient>> predicates = new HashSet<>();
        if (fNameFilter != null)
            predicates.add(patientService.patientsFirstNameStartsWith(fNameFilter));
        if (lNameFilter != null)
            predicates.add(patientService.patientsLastNameStartsWith(lNameFilter));
        if (wardId != null)
            predicates.add(patientService.patientsWardIs(wardService.getWard(wardId)));
        if (bed != null)
            predicates.add(patientService.patientsBedIs(bed));
        if (docId != null)
            predicates.add(patientService.patientsDoctorIs(doctorService.getDoctor(docId)));

        // Use the filter generated.
        patients = patientService.filterPatientsBy(patients,predicates);

        model.addAttribute("patients",patients);
        return "Doctor/viewPatients";
    }

    // TODO: Figure out how each patient maps to their own URL (ID? NHS number? etc - probably ID is best.)
    // View patient of specific id
    @GetMapping("/patient/hospitalNumber={id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientByHospitalNumber(id));
        return "Doctor/viewPatient";
    }

    @RequestMapping(value="/pdf/patientId={id}", method=RequestMethod.GET, produces="application/pdf")
    @ResponseBody
    public FileSystemResource patientPdf(@PathVariable("id") Long id) throws Exception {
        PdfGenerator pdfGen = new PdfGenerator();

        List<Patient> patients = Arrays.asList(patientService.getPatientById(id));
        pdfGen.patientAsPdf(patients);

        return new FileSystemResource("pdfout.pdf");
    }

    @RequestMapping(value="/pdf/all", method=RequestMethod.GET, produces="application/pdf")
    @ResponseBody
    public FileSystemResource patientPdf() throws Exception {
        PdfGenerator pdfGen = new PdfGenerator();
        UserCore userInfo =  (UserCore) authenticator.getAuthentication().getPrincipal();

        List<Patient> patients = new ArrayList<>();
        if (userInfo.hasAuth(SecurityRoles.Doctor)){
            patients = doctorService.getPatients(userInfo.getId());
        }

        pdfGen.patientAsPdf(patients);
        return new FileSystemResource("pdfout.pdf");
    }

    // Methods to edit patient's information/flag them.

    // Filter list of patients.

    // Search list of patients.
}
