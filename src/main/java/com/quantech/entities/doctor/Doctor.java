package com.quantech.entities.doctor;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.entities.team.Team;
import com.quantech.entities.user.UserCore;
import com.quantech.misc.Title;
import com.quantech.entities.patient.Patient;
import com.quantech.entities.handover.Handover;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
public class Doctor {

    @Id
    private Long id;


    @OneToOne(fetch = FetchType.LAZY) // Matches ID with UserCore
    @MapsId
    private UserCore user;


    @OneToMany
    @NotNull
    private List<Team> teams;

    @OneToMany(mappedBy = "doctor")
    @NotNull
    private List<Patient> patients;

    @Temporal(TemporalType.DATE)
    private java.util.Calendar lastRenewed;

    @OneToMany(mappedBy = "originDoctor")
    @NotNull
    private List<Handover> sentHandovers;

    @OneToMany(mappedBy = "recipientDoctor")
    @NotNull
    private List<Handover> receivedHandovers;

    public Doctor() {
        this.teams = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.lastRenewed = java.util.Calendar.getInstance();
        this.sentHandovers = new ArrayList<>();
        this.receivedHandovers = new ArrayList<>();
    }

    public Doctor(UserCore user) {
        this.user = user;
        this.teams = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.lastRenewed = java.util.Calendar.getInstance();
        this.sentHandovers = new ArrayList<>();
        this.receivedHandovers = new ArrayList<>();
    }

    public Doctor(Title title, String firstName, String lastName, String email, List<Team> teams,
                  List<Patient> patients, Calendar lastRenewed, List<Handover> sentHandovers,
                  List<Handover> receivedHandovers) {
        user.setTitle(title);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        this.teams = teams;
        this.patients = patients;
        this.lastRenewed = lastRenewed;
        this.sentHandovers = sentHandovers;
        this.receivedHandovers = receivedHandovers;
    }

    public void addPatient(Patient patient) throws NullPointerException {
        nullCheck(patient, "patient");
        this.patients.add(patient);
    }

    public Long getId() {
        return user.getId();
    }

    public Title getTitle() {
        return user.getTitle();
    }

    public void setTitle(Title title) throws NullPointerException {
        nullCheck(title,"title");
       user.setTitle(title);
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public void setFirstName(String firstName) throws NullPointerException{
        nullCheck(firstName, "first name");
        user.setFirstName(firstName);;
    }

    public String getLastName() {
       return user.getLastName();
    }

    public void setLastName(String lastName) throws NullPointerException {
        nullCheck(lastName,"last name");
        user.setLastName(lastName);
    }

    public String getEmail() {
       return user.getEmail();
    }

    public void setEmail(String email) throws NullPointerException {
        nullCheck(email,"email");
        user.setEmail(email);
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) throws NullPointerException {
        nullCheck(patients,"patients list");
        this.patients = patients;
    }

    public Calendar getLastRenewed() {
        return lastRenewed;
    }

    public void setLastRenewed(Calendar lastRenewed) throws NullPointerException {
        nullCheck(lastRenewed,"date last renewed");
        this.lastRenewed = lastRenewed;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) throws NullPointerException {
        nullCheck(teams,"teams list");
        this.teams = teams;
    }

    public List<Handover> getSentHandovers() {
        return sentHandovers;
    }

    // Should this be here? Maybe "add" and "flush" methods are better?
    public void setSentHandovers(List<Handover> sentHandovers) {
        this.sentHandovers = sentHandovers;
    }

    public List<Handover> getReceivedHandovers() {
        return receivedHandovers;
    }

    // Should this be here? Maybe "add" and "flush" methods are better?
    public void setReceivedHandovers(List<Handover> receivedHandovers) {
        this.receivedHandovers = receivedHandovers;
    }

    public UserCore getUser()
    {
        return user;
    }

    /**
     * Set the user that corresponds to the doctor object.
     * @param user The user to set
     * @throws NullPointerException If user is null.
     * @throws IllegalArgumentException If the user does not have Doctor privileges.
     */
    public void setUser(UserCore user) throws NullPointerException, IllegalArgumentException
    {
        nullCheck(user,"user");
        if (!user.getAuthorities().contains(SecurityRoles.Doctor))
            throw new IllegalArgumentException("Error: user assigned to doctor has not got Doctor privileges");
        this.user = user;
    }

    // Throws NullPointerException with custom error message.
    private void nullCheck(Object obj, String name) throws NullPointerException {
        if (obj == null)
            throw new NullPointerException("Error: " + name + " in doctor cannot be assigned null value.");

    }
}
