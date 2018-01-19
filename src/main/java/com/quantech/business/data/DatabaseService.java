package com.quantech.business.data;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains all methods that may be needed in accessing the database.
 */
@Service
public interface DatabaseService {
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
     * @return Patient corresponding to ID if found, null otherwise.
     */
    public Patient getPatientById(Long id);

    /**
     *
     * @param id The hospital number of patient in database.
     * @return Patient corresponding to number if found, null otherwise.
     */
    public Patient getPatientByHospitalNumber(Long id);
}
