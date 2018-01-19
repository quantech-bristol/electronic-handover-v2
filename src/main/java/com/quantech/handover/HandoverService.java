package com.quantech.handover;

import com.quantech.doctor.Doctor;
import com.quantech.doctor.DoctorRepository;
import com.quantech.patient.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HandoverService {

    @Autowired
    HandoverRepository handoverRepository;

    public List<Handover> getAllHandovers() {
        List<Handover> handovers = new ArrayList<>();
        handoverRepository.findAll().forEach(handovers::add);
        return handovers;
    }

    public Handover getHandover(Long id) {
        return handoverRepository.findOne(id);
    }

    public List<Handover> getAllFromDoctor(Doctor doctor) {
        return handoverRepository.findByOriginDoctor(doctor);
    }

    public List<Handover> getAllToDoctor(Doctor doctor) {
        return handoverRepository.findByRecipientDoctor(doctor);
    }

    public List<Handover> getAllPending() {
        return handoverRepository.findByAccepted(Boolean.FALSE);
    }

    public List<Handover> getAllForPatient(Patient patient) {
        return handoverRepository.findByPatient(patient);
    }

    public void saveHandover(Handover handover) { handoverRepository.save(handover); }

    public void deleteHandover(Long id) { handoverRepository.delete(id); }
}
