package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.patient.Patient;
import com.quantech.entities.patient.PatientRepository;
import com.quantech.entities.patient.PatientService;
import com.quantech.entities.ward.Ward;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@RunWith(SpringRunner.class)  // junit test runner
@SpringBootTest               // read app context
// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
                         TransactionalTestExecutionListener.class, // use transactional test execution
                         DbUnitTestExecutionListener.class})       // to read data sets from file
@ActiveProfiles("test")       // use application-test.yml properties (in-memory DB)
@Transactional                // rollback DB in between tests
public class PatientTest {
    @Autowired
    PatientService patientService;
    @Autowired
    PatientRepository patientRepository;

    @Test
    @DatabaseSetup("/dataSet1.xml")
    // Making sure the service obtains patients by ID properly.
    public void samePatientReturnedByIdTest() {
        Patient p = patientRepository.findById(3L);
        Assert.assertTrue(p.equals(patientService.getPatientById(3L)));
    }

    @Test
    @DatabaseSetup("/dataSet1.xml")
    // Make sure that the service returns all patients actually in the database.
    public void allPatientsReturnedTest() {
        List<Patient> l1 = getPatientsFromRepository(new long[]{3L,4L,5L,6L});
        List<Patient> l2 = patientService.getAllPatients();
        Assert.assertEquals(l1,l2);
    }

    @Test
    // Make sure that the service actually returns an empty list when there are no patients in the database.
    public void allPatientsReturnedEmptyTest() {
        Assert.assertEquals(patientService.getAllPatients(),new ArrayList<>());
    }

    @Test
    @DatabaseSetup("/dataSet1.xml")
    // Check that the service does actually properly sort the patients alphabetically by their first names, even when
    // some of the first names have the same value.
    public void sortPatientsByFirstNameCorrectOrder() {
        List<Patient> l1 = getPatientsFromRepository(new long[]{6L,3L,5L,4L});
        List<Patient> l2 = patientService.getAllPatients();
        l2 = patientService.sortPatientsByFirstName(l2);
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet1.xml")
    // Check that the service does actually properly sort the patients alphabetically by their last names.
    public void sortPatientsByLastNameCorrectOrder() {
        List<Patient> l1 = getPatientsFromRepository(new long[]{5L,4L,6L,3L});
        List<Patient> l2 = patientService.getAllPatients();
        l2 = patientService.sortPatientsByLastName(l2);
        Assert.assertEquals(l1,l2);
    }

    @Test
    // Check that the patient service detects when all fields of the patient are null, and the patient isn't in the DB.
    public void checkAllNullFieldsDetectedInPatient() {
        Patient p = new Patient();
        boolean thrown = false;
        try {
            patientService.savePatient(p);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Long id = p.getId();
        Assert.assertTrue(thrown);
        Assert.assertTrue(patientRepository.findById(id) == null);
    }

    @Test
    @DatabaseSetup("/dataSet1.xml")
    // Make sure that no null fields are detected when there aren't any.
    public void checkNoNullFieldsDetectedInPatient() {
        boolean thrown = false;
        Patient p = patientRepository.findById(3L);
        p.setBed("1");
        try {
            patientRepository.save(p);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Assert.assertFalse(thrown);
    }

    @Test
    // Should be able to detect when only some of the fields of the patient are null. (In this case it's the birth and admission dates)
    public void checkTwoNullFieldsDetectedInPatient() {
        boolean thrown = false;
        Patient p = new Patient();
        p.setDoctor(new Doctor());
        p.setTitle(Title.Mr);
        p.setFirstName("Name");
        p.setLastName("Name");
        p.setNHSNumber(9943197897L);
        p.setHospitalNumber(111L);
        p.setWard(new Ward());
        p.setBed("1");
        p.setRecommendations("reccs");
        p.setDiagnosis("diag");
        p.setSocialIssues("soc");
        p.setRelevantHistory("rel");

        try {
            patientService.savePatient(p);
        } catch (NullPointerException e) {
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    @DatabaseSetup("/dataSet1.xml")
    // Making sure patients are deleted properly.
    public void deletePatientTest() {
        Patient p = patientRepository.findById(3L);
        patientService.deletePatient(p);
        Assert.assertTrue(patientRepository.findById(3L) == null);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // Making sure that the method of filtering the patients by the start of their first name works.
    public void filterPatientsByStartOfFirstNameIsATest() {
        List<Patient> l1 = getPatientsFromRepository(new long[]{3L,5L,6L,7L,9L});
        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),patientService.patientsFirstNameStartsWith("A"));
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // Making sure that the method of filtering the patients by the start of their first name works.
    public void filterPatientsByStartOfFirstNameIsChaTest() {
        List<Patient> l1 = getPatientsFromRepository(new long[]{8L});

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),patientService.patientsFirstNameStartsWith("Cha"));
        Assert.assertEquals(l1,l2);
    }

    @DatabaseSetup("/dataSet2.xml")
    // Making sure that filtering for "Cha" and "C" only gives strings that match "Cha", and ordering of predicates
    // doesn't matter.
    public void filterPatientsByStartOfFirstNameIsChaAndCTest() {
        Set<Predicate<Patient>> p1 = new HashSet<>();
        p1.add(patientService.patientsFirstNameStartsWith("C"));
        p1.add(patientService.patientsFirstNameStartsWith("Cha"));

        Set<Predicate<Patient>> p2 = new HashSet<>();
        p2.add(patientService.patientsFirstNameStartsWith("Cha"));
        p2.add(patientService.patientsFirstNameStartsWith("C"));

        List<Patient> l1 = getPatientsFromRepository(new long[]{8L});

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),p1);
        List<Patient> l3 = patientService.filterPatientsBy(patientService.getAllPatients(),p2);

        Assert.assertEquals(l1,l2);
        Assert.assertEquals(l1,l3);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // When predicates conflict one another, the filter should yield an empty list.
    public void filterPatientsByStartOfDifferentFirstNameTest() {
        Set<Predicate<Patient>> p = new HashSet<>();
        p.add(patientService.patientsFirstNameStartsWith("A"));
        p.add(patientService.patientsFirstNameStartsWith("C"));

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),p);
        Assert.assertEquals(new ArrayList<>(),l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // Making sure last name filter is working properly.
    public void filterPatientsByStartOfLastNameHaTest() {
        List<Patient> l1 = getPatientsFromRepository(new long[]{6L,8L});

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),patientService.patientsLastNameStartsWith("Ha"));
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // When patient fulfilling predicate isn't found, return empty list.
    public void filterPatientsByStartOfLastNameNoneFoundTest() {
        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),patientService.patientsLastNameStartsWith("Tumia"));
        Assert.assertEquals(new ArrayList<>(),l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // Different filters working in conjunction should be fine.
    public void filterPatientsByStartOfFirstAndLastNameTest() {
        List<Patient> l1 = getPatientsFromRepository(new long[]{5L});

        Set<Predicate<Patient>> p = new HashSet<>();
        p.add(patientService.patientsFirstNameStartsWith("Ab"));
        p.add(patientService.patientsLastNameStartsWith("Bee"));

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),p);
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // When a patient that doesn't fulfil multiple predicates isn't found, return empty list.
    public void filterPatientsByStartOfFirstAndLastNameNoneFoundTest() {
        Set<Predicate<Patient>> p = new HashSet<>();
        p.add(patientService.patientsFirstNameStartsWith("Nuha"));
        p.add(patientService.patientsLastNameStartsWith("Tumia"));

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),p);
        Assert.assertEquals(new ArrayList<>(),l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // Filtering by ward.
    public void filterPatientsByWardTest() {
        Ward ward = patientRepository.findById(3L).getWard();
        List<Patient> l1 = getPatientsFromRepository(new long[]{3L,4L,5L});

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),patientService.patientsWardIs(ward));
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // Filtering by ward and bed.
    public void filterPatientsByWardAndBedTest() {
        Ward ward = patientRepository.findById(3L).getWard();
        List<Patient> l1 = getPatientsFromRepository(new long[]{3L});

        Set<Predicate<Patient>> p = new HashSet<>();
        p.add(patientService.patientsWardIs(ward));
        p.add(patientService.patientsBedIs("1"));

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),p);
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // Filtering by doctor.
    public void filterPatientsByDoctorTest() {
        Doctor d = patientRepository.findById(3L).getDoctor();
        List<Patient> l1 = getPatientsFromRepository(new long[]{3L,4L,7L,9L,11L});

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),patientService.patientsDoctorIs(d));
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet2.xml")
    // Filtering by a combination of predicates (conjunctive).
    public void filterPatientsByFirstNameLastNameDoctorTest() {
        Doctor d = patientRepository.findById(10L).getDoctor();
        List<Patient> l1 = getPatientsFromRepository(new long[]{10L});

        Set<Predicate<Patient>> p = new HashSet<>();
        p.add(patientService.patientsFirstNameStartsWith("Jane"));
        p.add(patientService.patientsLastNameStartsWith("Name"));
        p.add(patientService.patientsDoctorIs(d));

        List<Patient> l2 = patientService.filterPatientsBy(patientService.getAllPatients(),p);
        Assert.assertEquals(l1,l2);
    }

    @Test
    // Test that the check digit generated from a valid NHS number is in fact correct.
    public void checkDigitCorrectTest() {
        Patient p = new Patient();
        Long nhsNumber1 = 1328725170L;
        Long nhsNumber2 = 3721005252L;
        Long nhsNumber3 = 0L;
        Long nhsNumber4 = 7665993753L;
        Long nhsNumber5 = 9943197897L;
        Long nhsNumber6 = 27L;

        int cd = p.checkDigit(nhsNumber1);
        Assert.assertEquals(0,cd);

        cd = p.checkDigit(nhsNumber2);
        Assert.assertEquals(2,cd);

        cd = p.checkDigit(nhsNumber3);
        Assert.assertEquals(0,cd);

        cd = p.checkDigit(nhsNumber4);
        Assert.assertEquals(3,cd);

        cd = p.checkDigit(nhsNumber5);
        Assert.assertEquals(7,cd);

        cd = p.checkDigit(nhsNumber6);
        Assert.assertEquals(7,cd);
    }

    // Use this to create a list of patients with a certain sequence of IDs.
    private List<Patient> getPatientsFromRepository(long[] ids) {
        List<Patient> l1 = new ArrayList<>();
        for (long id : ids) {
            l1.add(patientRepository.findById(id));
        }
        return l1;
    }
}