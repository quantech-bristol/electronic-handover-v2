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

    @NotNull
    private Title title;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Column(unique = true)
    private String email;

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

    public Doctor(UserCore user, Title title, String firstName, String lastName, String email) {
        this.user = user;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.teams = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.lastRenewed = java.util.Calendar.getInstance();
        this.sentHandovers = new ArrayList<>();
        this.receivedHandovers = new ArrayList<>();
    }

    public Doctor(Title title, String firstName, String lastName, String email, List<Team> teams,
                  List<Patient> patients, Calendar lastRenewed, List<Handover> sentHandovers,
                  List<Handover> receivedHandovers) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
        return id;
    }

    // Should this really be here?
    public void setId(Long id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) throws NullPointerException {
        nullCheck(title,"title");
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws NullPointerException{
        nullCheck(firstName, "first name");
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws NullPointerException {
        nullCheck(lastName,"last name");
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws NullPointerException {
        nullCheck(email,"email");
        this.email = email;
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
            throw new IllegalArgumentException("Error: user assigned to doctor has not got Doctor privileges").
        this.user = user;
    }

    // Throws NullPointerException with custom error message.
    private void nullCheck(Object obj, String name) throws NullPointerException {
        if (obj == null)
            throw new NullPointerException("Error: " + name + " in doctor cannot be assigned null value.");

    }
}
