package com.quantech.entities.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
