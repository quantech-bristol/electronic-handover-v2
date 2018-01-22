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

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        patientRepository.findAll().forEach(patients::add);
        return patients;
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findOne(id);
    }

    public List<Patient> getPatientsByName(Optional<String> firstName, Optional<String> lastName) {
        List<Patient> patients = getAllPatients();
        if (firstName.isPresent()) {
            patients.stream()
                    .filter(p -> p.getLastName().equals(firstName))
                    .collect(Collectors.toList());
        }
        if (lastName.isPresent()) {
            patients.stream()
                    .filter(p -> p.getLastName().equals(lastName))
                    .collect(Collectors.toList());
        }
        return patients;
    }

    public Patient getPatientByHospitalNumber(Long id) {
        return patientRepository.findByHospitalNumber(id);
    }

    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }

    public void deletePatient(Patient id) {
        patientRepository.delete(id);
    }
}
