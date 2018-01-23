package com.quantech.entities.handover;

import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.patient.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HandoverService {

    @Autowired
    HandoverRepository handoverRepository;

    /**
     * Returns all handovers currently listed in the system.
     * @return A list of all handovers currently stored in the repository.
     */
    public List<Handover> getAllHandovers() {
        List<Handover> handovers = new ArrayList<>();
        handoverRepository.findAll().forEach(handovers::add);
        return handovers;
    }

    /**
     * Returns the handover stored in the repository corresponding to the unique id.
     * @param id The id to be used to identify the handover in the repository.
     * @return The handover corresponding to the id if it exists, or null otherwise.
     */
    public Handover getHandover(Long id) {
        return handoverRepository.findOne(id);
    }

    /**
     * Gets a list of all handovers sent by a specific doctor.
     * @param doctor The doctor from which the handover was sent.
     * @return A list of handovers that the input doctor has sent.
     */
    public List<Handover> getAllFromDoctor(Doctor doctor) {
        return handoverRepository.findByOriginDoctor(doctor);
    }

    /**
     * Finds a list of all handovers that have been sent to a specific doctor.
     * @param doctor The doctor to which the handover was sent.
     * @return A list of handovers with the input doctor as the recipient.
     */
    public List<Handover> getAllToDoctor(Doctor doctor) {
        return handoverRepository.findByRecipientDoctor(doctor);
    }

    /**
     * Returns a list of handovers that still have not been accepted by their recipient doctors.
     * @return A list of pending handovers.
     */
    public List<Handover> getAllPending() {
        return handoverRepository.findByAccepted(Boolean.FALSE);
    }

    /**
     * Finds a list of handovers that concern a specific patient.
     * @param patient The patient for which the handovers concern.
     * @return A list of handovers that involve the input patient.
     */
    public List<Handover> getAllForPatient(Patient patient) {
        return handoverRepository.findByPatient(patient);
    }

    /**
     * Saves the given handover into the repository.
     * @param handover The handover to be saved.
     */
    public void saveHandover(Handover handover) { handoverRepository.save(handover); }

    /**
     * Deletes a given handover from the repository.
     * @param id The id of the handover for which should be deleted.
     */
    public void deleteHandover(Long id) { handoverRepository.delete(id); }
}
