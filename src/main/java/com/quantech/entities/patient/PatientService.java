package com.quantech.entities.patient;

import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.user.UserService;
import com.quantech.entities.ward.Ward;
import com.quantech.misc.EntityFieldHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    /**
     * Returns all patients currently stored in the repository.
     * @return A list of all patients currently stored in the repository.
     */
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        patientRepository.findAll().forEach(patients::add);
        patients.removeIf(patient -> patient.getDischarged());
        return patients;
    }

    /**
     * Returns all patients currently stored in the repository belonging to the doctor
     * * @param doctor The doctor to filter by.
     * @return A list of all patients currently stored in the repository belonging to the doctor.
     */
    public List<Patient> getAllDoctorsPatients(Doctor doctor) {
        List<Patient> patients = new ArrayList<>();
        List<Patient> docPatients = new ArrayList<>();
        patientRepository.findAll().forEach(patients::add);
        for(Patient p : patients){
            if(p.getDoctor() == doctor) docPatients.add(p);
        }
        return docPatients;
    }

    /**
     * Sort the given list of patients by their first name, alphabetically.
     * @param list The list of patients.
     * @return A sorted list of patients, by first name.
     */
    public List<Patient> sortPatientsByFirstName(List<Patient> list) {
        return sortList(list,firstNameAlphabeticalComparator());
    }

    // A comparator that can be used to sort the patients by their first name, alphabetically.
    private Comparator<Patient> firstNameAlphabeticalComparator() {
        return (o1, o2) -> {
            if(o1.getFirstName() != null && o2.getFirstName() != null){
                if (o1.getFirstName().compareTo(o2.getFirstName()) == 0)
                    return lastNameAlphabeticalComparator().compare(o1,o2);
                else
                    return o1.getFirstName().compareTo(o2.getFirstName());
            }
            return 0;
        };
    }

    /**
     * Sort the given list of patients by their last name, alphabetically.
     * @param list The list of patients.
     * @return A sorted list of patients, by last name.
     */
    public List<Patient> sortPatientsByLastName(List<Patient> list) {
        return sortList(list,lastNameAlphabeticalComparator());
    }

    // A comparator that can be used to sort the patients by their first name, alphabetically.
    private Comparator<Patient> lastNameAlphabeticalComparator() {
        return (o1, o2) -> {
            if(o1.getLastName() != null && o2.getLastName() != null){
                return o1.getLastName().compareTo(o2.getLastName());
            }
            return 0;
        };
    }

    // Returns a list sorted using the input comparator.
    private List<Patient> sortList(List<Patient> l, Comparator<Patient> c) {
        l.sort(c);
        return l;
    }


    /**
     * Finds a patient stored in the repository corresponding to a unique id.
     * @param id The id for which the patient corresponds to.
     * @return  A patient corresponding to the id if it exists, null otherwise.
     */
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * A list of patients corresponding to a given name.
     * @param firstName The first name to search patients by.
     * @param lastName The last name to search patients by.
     * @return A list of patients that match this description.
     */
    public List<Patient> getPatientsByName(Optional<String> firstName, Optional<String> lastName) {
        List<Patient> patients = getAllPatients();
        if (firstName.isPresent()) {
            patients.stream()
                    .filter(p -> p.getLastName().equals(firstName)) //Can you compare an optional and a string?
                    .collect(Collectors.toList());
        }
        if (lastName.isPresent()) {
            patients.stream()
                    .filter(p -> p.getLastName().equals(lastName))
                    .collect(Collectors.toList());
        }
        return patients;
    }

    /**
     * Returns a patient corresponding to their hospital number.
     * @param id The hospital number corresponding to the patient.
     * @return The patient corresponding to the hospital number if they exist, null otherwise.
     */
    public Patient getPatientByHospitalNumber(Long id) {
        return patientRepository.findByHospitalNumber(id);
    }

    /**
     * Saves a given patient to the repository.
     * @param patient The patient to be saved into the repository.
     * @throws NullPointerException If the:
     *                                     - doctor,
     *                                     - title,
     *                                     - first name,
     *                                     - last name,
     *                                     - NHS number,
     *                                     - hospital number,
     *                                     - ward,
     *                                     - bed,
     *                                     - recommendations,
     *                                     - diagnosis,
     *                                     - social issues,
     *                                     - relevant history
     *                               has not been set, that is, is null.
     * @throws IllegalArgumentException If there's already a patient in the same bed of the given ward.
     * // TODO: implement this check.
     */
    public void savePatient(Patient patient) throws NullPointerException {
        List<String> fields = Arrays.asList("doctor",
                                            "title",
                                            "first name",
                                            "last name",
                                            "NHS number",
                                            "hospital number",
                                            "ward",
                                            "bed",
                                            "recommendations",
                                            "diagnosis",
                                            "social issues",
                                            "relevant history",
                                            "birthday",
                                            "admission date");
        List<Object> actualValues = Arrays.asList(patient.getDoctor(),
                                                  patient.getTitle(),
                                                  patient.getFirstName(),
                                                  patient.getLastName(),
                                                  patient.getNHSNumber(),
                                                  patient.getHospitalNumber(),
                                                  patient.getWard(),
                                                  patient.getBed(),
                                                  patient.getRecommendations(),
                                                  patient.getDiagnosis(),
                                                  patient.getSocialIssues(),
                                                  patient.getRelevantHistory(),
                                                  patient.getBirthDate(),
                                                  patient.getDateOfAdmission());

        List<Integer> nullValues = new ArrayList<>();

        //Check for null fields.
        for (int i = 0; i < actualValues.size(); i++) {
            if (actualValues.get(i) == null)
                nullValues.add(i);
        }
        // If patient object is valid save into the database.
        if (nullValues.isEmpty())
            patientRepository.save(patient);
        else {
            // Generate a custom error message (useful to put on page)
            StringBuilder errorMessage = new StringBuilder("Error: ");
            errorMessage.append(fields.get(nullValues.get(0)));
            if (nullValues.size() != 1) {
                for (int i = 1; i < nullValues.size() - 1; i++) {
                    errorMessage.append(", ").append(fields.get(nullValues.get(i)));
                }
                errorMessage.append(" and ").append(fields.get(nullValues.get(nullValues.size() - 1))).append(" have");
            }
            else
                errorMessage.append(" has");
            errorMessage.append(" not been set for the patient.");

            String e = errorMessage.toString();
            throw new NullPointerException(e);
        }
    }

    /**
     * Deletes a given patient from the repository.
     * @param patient The patient to be removed from the repository.
     */
    public void deletePatient(Patient patient) {
        patientRepository.delete(patient);
    }

    /**
     * Deletes a given patient from the repository.
     * @param id The patient to be removed from the repository.
     */
    public void deletePatient(Long id) {
        patientRepository.delete(id);
    }

    /**
     * Filter list of a patients by a given predicate.
     * @param list A list of patients.
     * @param predicate A predicate to test the patients against.
     * @return A list of patients filtered by the given predicate.
     */
    public List<Patient> filterPatientsBy(List<Patient> list, Predicate<Patient> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Filter list of a patients by a given predicate.
     * @param list A list of patients.
     * @param predicates A collection of predicates to test the patients against.
     * @return A list of patients filtered by the given predicate.
     */
    public List<Patient> filterPatientsBy(List<Patient> list, Iterable<Predicate<Patient>> predicates) {
        Stream<Patient> stream = list.stream();
        for (Predicate<Patient> p : predicates) {
            stream = stream.filter(p);
        }
        return stream.collect(Collectors.toList());
    }

    // Checks if a patient's first name starts with the given string.
    public Predicate<Patient> patientsFirstNameStartsWith(String str) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getFirstName().startsWith(str);
            }
        };
    }

    // Checks if a patient's last name starts with a given string.
    public Predicate<Patient> patientsLastNameStartsWith(String str) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getLastName().startsWith(str);
            }
        };
    }

    // Checks if the patient is currently staying at a given ward.
    public Predicate<Patient> patientsWardIs(Ward ward) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getWard().equals(ward);
            }
        };
    }

    // Checks if a patient is currently at a given bed.
    public Predicate<Patient> patientsBedIs(String str) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getBed().equals(str);
            }
        };
    }

    // Checks if a patient is currently under the care of a certain doctor.
    public Predicate<Patient> patientsDoctorIs(Doctor doc) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getDoctor().equals(doc);
            }
        };
    }

    // Checks if patient is listed with a given risk.
    private Predicate<Patient> patientHasRisks(String risk) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getRisks().contains(risk);
            }
        };
    }

    // Checks if the patient is listed with a collection of given lists.
    private Predicate<Patient> patientHasRisks(Iterable<String> risks) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                String r = patient.getRisks();
                boolean has = true;
                for (String risk : risks) {
                    has = has && (r.contains(risk));
                }
                return has;
            }
        };
    }

    public void setDischarged(Patient p, Boolean d) {
        p.setDischarged(d);
        patientRepository.save(p);
    }

    /**
     * Checks the validity of a patient's fields, and rejects the result value accordingly.
     * @param result The binding result formed from the view template.
     * @param patient The patient object created through the form.
     */
    public void CheckValidity(BindingResult result, Patient patient) {
        // Checking the validity of the set NHS number.
        // 1. Check that it has the correct number of digits.
        if (!patient.NHSNumberCorrectLength())
            result.rejectValue("NHSNumber","NHS number has too many digits.");
        // 2. Check that the NHS number is valid.
        if (!patient.NHSNumberIsValid())
            result.rejectValue("NHSNumber", "Number given is not a valid NHS number.");
        // 3. Check that the NHS number is unique.
        if (patientRepository.findByNHSNumber(patient.getNHSNumber()) != null)
            result.rejectValue("NHSNumber", "Patient with given NHS number already exists.");

        // Check that the title has been set.
        if (patient.getTitle() == null)
            result.rejectValue("title","Please set title.");

        // Check that no empty strings have been set as the names.
        try {
            EntityFieldHandler.nameValidityCheck(patient.getFirstName());
        } catch (Exception e) {
            result.rejectValue("firstName","Please set valid first name(s)");
        }
        try {
            EntityFieldHandler.nameValidityCheck(patient.getLastName());
        } catch (Exception e) {
            result.rejectValue("lastName","Please set valid last name(s)");
        }

        // Check provided birth date.
        if (patient.getHospitalNumber() == null)
            result.rejectValue("hospitalNumber","Please set a valid hospital number.");
        if (patientRepository.findByHospitalNumber(patient.getHospitalNumber()) != null)
            result.rejectValue("hospitalNumber","Patient with given hospital number already exists.");

        // Check wards and beds.
        if (patient.getBed() == null)
            result.rejectValue("bed","Please set a bed for the patient.");
        if (patient.getWard() == null)
            result.rejectValue("ward","Please set a ward for the patient.");
        if (patientRepository.findByWardAndBed(patient.getWard(),patient.getBed()) != null)
            result.rejectValue("bed","Given bed is already occupied by another patient.");

        // Check birth date.
        if (patient.getBirthDate() == null)
            result.rejectValue("birthDate","Please set patient's date of birth.");
        if (patient.getBirthDate().after(patient.getDateOfAdmission()))
            result.rejectValue("birthDate","Patient's date of birth cannot be in the future.");

        if (patient.getRelevantHistory() == null)
            result.rejectValue("relevantHistory","Please provide some form of relevant history.");
        if (patient.getSocialIssues() == null)
            result.rejectValue("socialIssues","Please provide any relevant information (or type \"none\").");
        if (patient.getRisks() == null)
            result.rejectValue("risks","Please provide any relevant risks to the patient.");
        if (patient.getRecommendations() == null)
            result.rejectValue("recommendations","Please provide recommendations for treatment.");
        if (patient.getDiagnosis() == null)
            result.rejectValue("diagnosis","Please provide a diagnosis.");
    }
}
