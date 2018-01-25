package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorRepository;
import com.quantech.entities.handover.HandoverRepository;
import com.quantech.entities.patient.PatientRepository;
import com.quantech.entities.team.TeamRepository;
import com.quantech.entities.ward.WardRepository;
import com.quantech.misc.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class) // junit test runner
@SpringBootTest // read app context
// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class, // use transactional test execution
        DbUnitTestExecutionListener.class}) // to read data sets from file
@ActiveProfiles("test") // use application-test.yml properties (in-memory DB)
@Transactional // rollback DB in between tests
public class RepositoryTest {
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    HandoverRepository handoverRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    WardRepository wardRepository;

    @Test
    @DatabaseSetup("/database_test.xml") // Reads data set from file - these are temporary values for the database.
    // Making sure that I've set up the file properly and all the things that should be there actually are.
    public void basic_test() {
        Assert.assertTrue(patientRepository.count() == 3L);
        Assert.assertTrue(handoverRepository.count() == 0L);
        Assert.assertTrue(teamRepository.count() == 1L);
        Assert.assertTrue(wardRepository.count() == 1L);
        Assert.assertTrue(doctorRepository.count() == 2L);
    }

  /*  @Test
    // Just checking that I know how to write tests properly - an example:
    public void createDoctorTest() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Neal");
        doctor.setLastName("the Doctor");
        doctor.setTitle(Title.Dr);
        doctor.setEmail("doctoremail@email.com");

        doctorRepository.save(doctor);

        Doctor doctor2 = doctorRepository.findByFirstNameAndLastName("Neal","the Doctor").get(0);

        Assert.assertTrue(doctor2.getEmail().equals(doctor.getEmail()));
        Assert.assertTrue(doctor2.getTitle().equals(doctor.getTitle()));
    } */

}