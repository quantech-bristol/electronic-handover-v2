package com.quantech.misc;

import com.quantech.team.Team;
import com.quantech.doctor.Doctor;
import com.quantech.doctor.DoctorRepository;
import com.quantech.patient.Patient;
import com.quantech.patient.PatientRepository;
import com.quantech.ward.Ward;
import com.quantech.ward.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("dbs")
public class DatabaseServiceImpl implements DatabaseService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WardRepository wardRepository;

    @Override
    public boolean saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
        return true;
    }

    @Override
    public boolean addDoctorToTeam(Team team, Doctor doctor) {
        // TODO
        return false;
    }

    @Override
    public boolean addDoctorToTeam(Team team, Iterable<Doctor> doctor) {
        //TODO
        return false;
    }

    @Override
    public boolean deleteDoctorFromTeam(Team team, Doctor doctor) {
        //TODO
        return false;
    }

    @Override
    public List<Patient> getDoctorsPatients(Doctor doctor) {
        //TODO
        return null;
    }

    @Override
    public List<Doctor> allDoctors() {
        List<Doctor> list = new ArrayList<>();
        for (Doctor doc : doctorRepository.findAll())
            list.add(doc);
        return list;
    }

    @Override
    public boolean savePatient(Patient patient) {
        patientRepository.save(patient);
        Doctor doctor = doctorRepository.findOne(patient.getDoctor().getId());
        doctor.addPatient(patient);
        this.saveDoctor(doctor);
        return true;
    }

    @Override
    public List<Patient> allPatients() {
        List<Patient> list = new ArrayList<>();
        for (Patient p : patientRepository.findAll())
            list.add(p);
        return list;
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findOne(id);
    }

    @Override
    public Patient getPatientByHospitalNumber(Long id) {
        return patientRepository.findByHospitalNumber(id);
    }

    @Override
    public boolean saveWard(Ward ward) {
        wardRepository.save(ward);
        return true;
    }

    @Override
    public List<Ward> allWards() {
        List<Ward> list = new ArrayList<>();
        for (Ward w : wardRepository.findAll())
            list.add(w);
        return list;
    }

}
