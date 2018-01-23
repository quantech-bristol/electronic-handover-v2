package com.quantech.entities.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        return patientRepository.findOne(id);
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
     *                                     - hospital,
     *                                     - ward,
     *                                     - bed,
     *                                     - recommendations,
     *                                     - diagnosis.
     * @throws NullPointerException If the last name field hasn't been set.
     * @throws NullPointerException If the NHS number field hasn't been set.
     * @throws NullPointerException If the hospital number field hasn't been set.
     * @throws NullPointerException If the ward field hasn't been set.
     * @throws NullPointerException If the bed field hasn't been set.
     * @throws NullPointerException If the relevant history field hasn't been set.
     * @throws NullPointerException If the social issues field hasn't been set.
     * @throws NullPointerException If the recommendations and diagnosis field hasn't been set.
     */
    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }

    /**
     * Deletes a given patient from the repository.
     * @param patient The patient to be removed from the repository.
     */
    public void deletePatient(Patient patient) {
        patientRepository.delete(patient);
    }
}
