package com.quantech.entities.doctor;

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

    @OneToOne(fetch = FetchType.LAZY)//Matches ID with UserCore
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

    public void addPatient(Patient patient) {
        this.patients.add(patient);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public Calendar getLastRenewed() {
        return lastRenewed;
    }

    public void setLastRenewed(Calendar lastRenewed) {
        this.lastRenewed = lastRenewed;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Handover> getSentHandovers() {
        return sentHandovers;
    }

    public void setSentHandovers(List<Handover> sentHandovers) {
        this.sentHandovers = sentHandovers;
    }

    public List<Handover> getReceivedHandovers() {
        return receivedHandovers;
    }

    public void setReceivedHandovers(List<Handover> receivedHandovers) {
        this.receivedHandovers = receivedHandovers;
    }


    public UserCore getUser()
    {
        return user;
    }

    public void setUser(UserCore user)
    {
        this.user = user;
    }
}
