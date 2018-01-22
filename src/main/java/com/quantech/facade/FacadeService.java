package com.quantech.facade;

import com.quantech.entities.handover.Handover;
import com.quantech.entities.team.Team;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.patient.Patient;
import com.quantech.entities.ward.Ward;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains all methods that may be needed in accessing the database.
 */
@Service
public interface FacadeService {
    // TODO: Decide what methods we need to use and what conditions/exceptions need to hold for each one.

    /**
     * Saves the doctor object into the database, making sure that all of it's fields are valid and do not conflict with
     * existing Doctor objects saved.
     * @param doctor The new doctor object to be saved into the database.
     * @return true if the Doctor was successfully saved, false if it was not.
     * @throws Exception If the doctor isn't valid
     * TODO: Decide on what makes a doctor valid; e.g. email should be unique.
     */
    public boolean saveDoctor(Doctor doctor);

    /**
     * Adds the doctor to the given team.
     * @param team The team that the doctor should be added to.
     * @param doctor The doctor that is going to be added to the team.
     * @return true if the doctor was successfully added, false otherwise;
     * @throws Exception Various ones, e.g. team doesn't exist, doctor already on it, etc.
     */
    public boolean addDoctorToTeam(Team team, Doctor doctor);

    /**
     * Adds the group of doctors to the given team.
     * @param team The team that the doctor should be added to.
     * @param doctor The doctor that is going to be added to the team.
     * @return true if the doctor was successfully added, false otherwise;
     * @throws Exception Various ones, e.g. team doesn't exist, doctor already on it, etc.
     */
    public boolean addDoctorToTeam(Team team, Iterable<Doctor> doctor);

    /**
     * Deletes doctor from the given team.
     * @param team The team from which the doctor should be removed.
     * @param doctor The doctor to remove.
     * @return true if the doctor was successfully removed, false otherwise.
     * @throws Exception Various ones, e.g. team doesn't exist.
     */
    public boolean deleteDoctorFromTeam(Team team, Doctor doctor);

    /**
     *
     * @param doctor The doctor object.
     * @return Returns a list of the patients under the doctor's care.
     */
    public List<Patient> getDoctorsPatients(Doctor doctor);

    /**
     * @return A list of all doctors stored in the database.
     */
    public List<Doctor> allDoctors();

    /**
     * A list of all doctors only satisfying the given predicate.
     * @param filter All of the doctors on this list need to satisfy this.
     * @return A list of all doctors only satisfying the given predicate.
     */
    public List<Doctor> allDoctorsFilteredBy(Boolean filter);

    /**
     * @return A list of doctors sorted alphabetically by their first name.
     */
    public List<Doctor> allDoctorsSortedByFirstName();

    /**
     * @return A list of doctors sorted alphabetically by their last name.
     */
    public List<Doctor> allDoctorsSortedByListName();

    /**
     * Saves the patient object into the database, making sure that all of it's fields are valid and do not conflict with
     * existing patient objects saved. Also updates the corresponding doctor in the database.
     * @param patient The patient object to be saved into the database.
     * @return true if the patient was successfully saved, false otherwise.
     * @throws Exception If there are any problems.
     * TODO: Decide on what makes a patient valid; e.g. identification points should be unique.
     */
    public boolean savePatient(Patient patient);

    /**
     * @return A list of all patients stored in the database.
     */
    public List<Patient> allPatients();

    /**
     *
     * @param id ID of patient in database.
     * @return patient corresponding to ID if found, null otherwise.
     */
    public Patient getPatientById(Long id);

    /**
     *
     * @param id The hospital number of patient in database.
     * @return patient corresponding to number if found, null otherwise.
     */
    public Patient getPatientByHospitalNumber(Long id);

    /**
     * Saves the ward object into the database, making sure that all of it's fields are valid and do not conflict with
     * existing ward objects saved.
     * @param ward The ward object to be saved.
     * @return true if the ward was successfully saved, false otherwise.
     * @throws Exception If there are any problems.
     */
    public boolean saveWard(Ward ward);

    /**
     * @return a list of all wards stored in the database.
     */
    public List<Ward> allWards();

    /**
     * Saves handover object into database
     * @param handover The handover object to be saved
     * @return true if the handover successfully saved, false otherwise.
     */
    public boolean saveHandover(Handover handover);

    /**
     * @return list of all handovers stored in the database
     */
    public List<Handover> allHandovers();

}
