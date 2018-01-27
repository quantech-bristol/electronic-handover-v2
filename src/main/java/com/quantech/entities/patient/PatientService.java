package com.quantech.entities.patient;

import com.quantech.entities.ward.Ward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return patients;
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
     * Filter list of a patients by a given predicate.
     * @param list A list of patients.
     * @param predicate A predicate to test the patients against.
     * @return A list of patients filtered by the given predicate.
     */
    public List<Patient> filterPatientsBy(List<Patient> list, Predicate<Patient> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    // Checks if a patient's first name starts with the given string.
    private Predicate<Patient> patientFirstNameStartsWith(String str) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getFirstName().startsWith(str);
            }
        };
    }

    // Checks if a patient's last name starts with a given string.
    private Predicate<Patient> patientsLastNameStartsWith(String str) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getLastName().startsWith(str);
            }
        };
    }

    // Checks if the patient is currently staying at a given ward.
    private Predicate<Patient> patientsWardIs(Ward ward) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getWard().equals(ward);
            }
        };
    }

    // Checks if a patient is currently at a given bed.
    private Predicate<Patient> patientsBedIs(String str) {
        return new Predicate<Patient>() {
            @Override
            public boolean test(Patient patient) {
                return patient.getBed().equals(str);
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

}
