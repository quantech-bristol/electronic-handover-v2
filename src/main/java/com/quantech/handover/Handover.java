package com.quantech.handover;

import com.quantech.doctor.Doctor;
import com.quantech.patient.Patient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Handover {
    // TODO: Decide what fields we want to use.
    // TODO: Decide what exceptions are thrown/conditions are held when using getters and setters.

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @NotNull
    private Patient patient;

    @ManyToOne
    @NotNull
    private Doctor originDoctor;

    @ManyToOne
    @NotNull
    private Doctor recipientDoctor;

    @NotNull
    private String note;

    @NotNull
    private Boolean accepted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getOriginDoctor() {
        return originDoctor;
    }

    public void setOriginDoctor(Doctor originDoctor) {
        this.originDoctor = originDoctor;
    }

    public Doctor getRecipientDoctor() {
        return recipientDoctor;
    }

    public void setRecipientDoctor(Doctor recipientDoctor) {
        this.recipientDoctor = recipientDoctor;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}