package com.quantech.entities.doctor;

import com.quantech.entities.patient.Patient;
import com.quantech.entities.team.Team;
import com.quantech.entities.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    // All of these methods should be admin only, which can be restricted in the Controller layer

    /**
     * @return A list of all doctors stored in the doctor repository.
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctorRepository.findAll().forEach(doctors::add);
        return doctors;
    }


    // A comparator used to sort doctors by their date renewed, most recent first.
    private Comparator<Doctor> dateRenewedMostRecentFirstComparator() {
       return (o1,o2) -> {
           if(o1.getFirstName() != null && o2.getFirstName() != null){
               return (o1.getLastRenewed().compareTo(o2.getLastRenewed()));
           }
           return 0;
       };
    }

    // A comparator used to sort doctors by their date renewed, most recent last.
    private Comparator<Doctor> dateRenewedMostRecentLastComparator() {
        return (o1,o2) -> {
            if(o1.getFirstName() != null && o2.getFirstName() != null){
                return -(o1.getLastRenewed().compareTo(o2.getLastRenewed()));
            }
            return 0;
        };
    }

    /**
     * Sort the given list of doctors by their first name, alphabetically.
     * @param list The list of doctors.
     * @return A sorted list of doctors, by first name.
     */
    public List<Doctor> sortPatientsByFirstName(List<Doctor> list) {
        return sortList(list,firstNameAlphabeticalComparator());
    }

    // A comparator that can be used to sort the doctors by their first name, alphabetically.
    private Comparator<Doctor> firstNameAlphabeticalComparator() {
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
     * Sort the given list of doctors by their last name, alphabetically.
     * @param list The list of doctors.
     * @return A sorted list of doctors, by last name.
     */
    public List<Doctor> sortDoctorsByLastName(List<Doctor> list) {
        return sortList(list,lastNameAlphabeticalComparator());
    }

    // A comparator that can be used to sort the doctors by their first name, alphabetically.
    private Comparator<Doctor> lastNameAlphabeticalComparator() {
        return (o1, o2) -> {
            if(o1.getLastName() != null && o2.getLastName() != null){
                return o1.getLastName().compareTo(o2.getLastName());
            }
            return 0;
        };
    }

    // Returns a list sorted using the input comparator.
    private List<Doctor> sortList(List<Doctor> l, Comparator<Doctor> c) {
        l.sort(c);
        return l;
    }

    /**
     * Finds the doctor object corresponding to a given unique id.
     * @param id The id for which the desired doctor has been assigned.
     * @return The doctor object that corresponds to the id if it exists, null if a doctor with the given id doesn't exist.
     */
    public Doctor getDoctor(Long id) {
        return doctorRepository.findOne(id);
    }

    /**
     * Saves the given doctor object into the repository.
     * @param doctor
     */
    public void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    /**
     * Deletes the doctor with the corresponding id from the repository.
     * @param id The id corresponding to the doctor to be deleted.
     */
    public void deleteDoctor(Long id) {
        doctorRepository.delete(id);
    }

    /**
     * Adds the given team to the doctor's list.
     * @param team The team for which the doctor should be added to.
     * @param id The id corresponding to the doctor in the repository.
     */
    public void addTeamToDoctor(Team team, Long id) {
        // Add the team to the doctor's list of teams
        //     The doctor needs to be added to the Team's list of doctors over in the Team package
        Doctor doctor = doctorRepository.findOne(id);
        List<Team> teams = doctor.getTeams();
        teams.add(team);
        doctor.setTeams(teams);
        doctorRepository.save(doctor);
    }

    /**
     * Removes the given team from the doctor's list.
     * @param team The team for which the doctor should be removed from.
     * @param id The id corresponding to the doctor in the repository.
     */
    public void removeTeamFromDoctor(Team team, Long id) {
        Doctor doctor = doctorRepository.findOne(id);
        List<Team> teams = doctor.getTeams();
        teams.remove(team);
        doctor.setTeams(teams);
        doctorRepository.save(doctor);
    }

    /**
     * Adds a patient to the doctor's list of patients.
     * @param patient The patient for which the doctor now has responsibility over.
     * @param doctor The doctor that will take responsibility over the patient.
     */
    public void addPatient(Patient patient, Doctor doctor) {
        doctor.addPatient(patient);
        doctorRepository.save(doctor);
    }

    /**
     * Returns a list of patients under the doctor's care.
     * @param id The id of the given doctor.
     * @return A list of patients under the doctor's care.
     * @throws NullPointerException When the given id doesn't exist in the database.
     */
    public List<Patient> getPatients(Long id) {
        return doctorRepository.findById(id).getPatients();
    }
    /*
    RE: maintaining a list of patients
        - should we just look at the Teams the Doctor is in, and then call team.getPatients(), rather than
          overlapping ?

     */
}
