package com.quantech.entities.patient;

import com.quantech.entities.doctor.Doctor;
import com.quantech.misc.Title;
import com.quantech.entities.ward.Ward;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Patient {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Doctor doctor;

    @NotNull
    private Title title;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    // TODO: sort date format as it's currently going in wrong
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date birthDate;

    // First character needs to be lower case for this field to work, not a typo.
    @NotNull
    @Column(unique = true)
    private Long nHSNumber;

    @NotNull
    @Column(unique = true)
    private Long hospitalNumber;

    @NotNull
    @ManyToOne
    private Ward ward;

    @NotNull
    private String bed;

    // TODO: sort date format as it's currently inputting wrong
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateOfAdmission;

    @NotNull
    private String relevantHistory;

    @NotNull
    private String socialIssues;

    // TODO: Maybe change this to be a List of enums depending on the team the doctor belongs to.
    @NotNull
    private String risks;

    @NotNull
    private String recommendations;

    @NotNull
    private String diagnosis;

    public Patient() {

    }

    // TODO: Carry out error detection/prevention on these getters and setters.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Long getNHSNumber() {
        return nHSNumber;
    }

    public void setNHSNumber(Long NHSNumber) {
        this.nHSNumber = NHSNumber;
    }

    public Long getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(Long hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) { this.ward = ward; }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public Date getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(Date dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public String getRelevantHistory() {
        return relevantHistory;
    }

    public void setRelevantHistory(String relevantHistory) {
        this.relevantHistory = relevantHistory;
    }

    public String getSocialIssues() {
        return socialIssues;
    }

    public void setSocialIssues(String socialIssues) {
        this.socialIssues = socialIssues;
    }

    public String getRisks() {
        return risks;
    }

    public void setRisks(String risks) {
        this.risks = risks;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
