package com.quantech.entities.handover;

import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.patient.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
public interface JobsService {

    /**
     * Returns the handover stored in the repository corresponding to the unique id.
     * @param id The id to be used to identify the job in the repository.
     * @return The job corresponding to the id if it exists, or null otherwise.
     */
    public Handover getJob(Long id);

    /**
     * Finds a list of all jobs that are currently the responsibility of a certain doctor.
     * @param doctor The doctor to which the job was sent.
     * @return A list of jobs that the doctor is responsible for.
     */
    public List<Handover> getAllToDoctor(Doctor doctor);

    /**
     * Returns a list of jobs that still have not been accepted by their recipient doctors.
     * @return A list of pending jobs.
     */
    public List<Job> getAllPending();

    /**
     * Finds a list of jobs that concern a specific patient.
     * @param patient The patient for which the jobs concern.
     * @return A list of jobs that involve the input patient.
     */
    public List<Job> getAllForPatient(Patient patient);

    /**
     * Finds a list of jobs that concern a specific patient that are incomplete.
     * @param patient The patient for which the jobs concern.
     * @return A list of incomplete jobs that involve the input patient.
     */
    public List<Job> getAllUncompletedForPatient(Patient patient);

    /**
     * Finds a list of jobs that concern a specific patient that are complete.
     * @param patient The patient for which the jobs concern.
     * @return A list of complete jobs that involve the input patient.
     */
    public List<Job> getAllCompletedForPatient(Patient patient);

    /**
     * Saves the given job into the repository.
     * @param job The handover to be saved.
     */
    public void saveJob(Job job);

    /**
     * Send a handover using a given job.
     * @param job The job to send.
     * @param doctor The doctor to handover to.
     */
    public void handoverJob(Job job, Doctor doctor);

    /**
     * Send a handover using a given iterable of jobs.
     * @param job The jobs to send.
     * @param doctor The doctor to handover to.
     */
    public void handoverJob(Iterable<Job> job, Doctor doctor);

    /**
     * Marks a given job as complete.
     * @param job The job to complete.
     */
    public void completeJob(Job job);

    /**
     * Checks the validity of a patient's fields, and rejects the result value accordingly.
     * @param result The binding result formed from the view template.
     * @param job The job object created through the form.
     */
    public void CheckValidity(BindingResult result, Job job);
}
