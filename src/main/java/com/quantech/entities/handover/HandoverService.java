package com.quantech.entities.handover;

import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.patient.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

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
        List<Handover> handovers = handoverRepository.findByOriginDoctor(doctor);
        List<Handover> pendingHandovers = new ArrayList<>();
        for(Handover handover:handovers){
            if(handover.getAccepted() == false) pendingHandovers.add(handover);
        }
        return pendingHandovers;
    }

    /**
     * Finds a list of all handovers that have been sent to a specific doctor that haven't been accepted.
     * @param doctor The doctor to which the handover was sent.
     * @return A list of handovers with the input doctor as the recipient.
     */
    public List<Handover> getAllToDoctor(Doctor doctor) {
        List<Handover> handovers = handoverRepository.findByRecipientDoctor(doctor);
        List<Handover> pendingHandovers = new ArrayList<>();
        for(Handover handover:handovers){
            if(handover.getAccepted() == false) pendingHandovers.add(handover);
        }
        return pendingHandovers;
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
     * Finds a list of handovers that concern a specific patient that are active.
     * @param patient The patient for which the handovers concern.
     * @return A list of active handovers that involve the input patient.
     */
    public List<Handover> getAllActiveForPatient(Patient patient) {
        List<Handover> handovers = handoverRepository.findByPatient(patient);
        List<Handover> activeHandovers = new ArrayList<>();
        for(Handover handover:handovers){
            if(handover.getAccepted() == false) activeHandovers.add(handover);
        }
        return activeHandovers;
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

    /**
     * Checks the validity of a patient's fields, and rejects the result value accordingly.
     * @param result The binding result formed from the view template.
     * @param handover The handover object created through the form.
     */
    public void CheckValidity(BindingResult result, Handover handover) {
        if (handover.getPatient() == null)
            result.rejectValue("patient","handover.patient","Please select a patient to hand over.");
        if (!getAllActiveForPatient(handover.getPatient()).isEmpty())
            result.rejectValue("patient","handover.patient","Patient is already part of an active handover.");
        if (handover.getRecipientDoctor() == null)
            result.rejectValue("recipientDoctor","handover.recipientDoctor","Please select a professional to send the handover to.");
    }
}
