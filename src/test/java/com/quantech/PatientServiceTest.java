package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.entities.patient.Patient;
import com.quantech.entities.patient.PatientRepository;
import com.quantech.entities.patient.PatientService;
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
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)  // junit test runner
@SpringBootTest               // read app context
// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
                         TransactionalTestExecutionListener.class, // use transactional test execution
                         DbUnitTestExecutionListener.class})       // to read data sets from file
@ActiveProfiles("test")       // use application-test.yml properties (in-memory DB)
@Transactional                // rollback DB in between tests
public class PatientServiceTest {
    @Autowired
    PatientService patientService;
    @Autowired
    PatientRepository patientRepository;

    @Test
    @DatabaseSetup("/patientServiceTest-dataSet1.xml")
    // Make sure that the service returns all patients actually in the database.
    public void allPatientsReturnedTest() {
        List<Patient> l1 = new ArrayList<>();
        l1.add(patientRepository.findById(3L));
        l1.add(patientRepository.findById(4L));
        l1.add(patientRepository.findById(5L));
        l1.add(patientRepository.findById(6L));

        List<Patient> l2 = patientService.getAllPatients();
        Assert.assertEquals(l1,l2);
    }

    @Test
    // Make sure that the service actually returns an empty list when there are no patients in the database.
    public void allPatientsReturnedEmptyTest() {
        Assert.assertEquals(patientService.getAllPatients(),new ArrayList<>());
    }

    @Test
    @DatabaseSetup("/patientServiceTest-dataSet1.xml")
    // Check that the service does actually properly sort the patients alphabetically by their first names, even when
    // some of the first names have the same value.
    public void sortPatientsByFirstNameCorrectOrder() {
        List<Patient> l1 = new ArrayList<>();
        l1.add(patientRepository.findById(6L));
        l1.add(patientRepository.findById(3L));
        l1.add(patientRepository.findById(5L));
        l1.add(patientRepository.findById(4L));

        List<Patient> l2 = patientService.getAllPatients();
        l2 = patientService.sortPatientsByFirstName(l2);
        Assert.assertEquals(l1,l2);
    }

}