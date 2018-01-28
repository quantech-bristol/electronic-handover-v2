package com.quantech;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorRepository;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.patient.Patient;
import com.quantech.entities.patient.PatientRepository;
import com.quantech.entities.patient.PatientService;
import com.quantech.entities.user.UserCore;
import com.quantech.entities.user.UserRepository;
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
public class DoctorTest {
    @Autowired
    DoctorService doctorService;
    @Autowired
    DoctorRepository doctorRepository;

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure the service sorts the list of doctors properly; most recently renewed first.:
    public void sortByMostRecentlyRenewedFirstTest() {
        List<Doctor> l1 = getDoctorsFromRepository(new long[]{5L,3L,4L,1L,2L});
        List<Doctor> l2 = doctorService.sortDoctorsByDateRenewedMostRecentFirst(doctorService.getAllDoctors());
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure the service sorts the list of doctors properly; least recently renewed first.:
    public void sortByLeastRecentlyRenewedFirstTest() {
        List<Doctor> l1 = getDoctorsFromRepository(new long[]{2L,1L,4L,3L,5L});
        List<Doctor> l2 = doctorService.sortDoctorsByDateRenewedMostRecentLast(doctorService.getAllDoctors());
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure ordering of sort doesn't matter when applying:
    public void sortByLeastRecentlyRenewedThenMostRecentlyRenewedFirstTest() {
        List<Doctor> l1 = getDoctorsFromRepository(new long[]{5L,3L,4L,1L,2L});
        List<Doctor> l2 = doctorService.sortDoctorsByDateRenewedMostRecentLast(doctorService.getAllDoctors());
        l2 = doctorService.sortDoctorsByDateRenewedMostRecentFirst(l2);
        Assert.assertEquals(l1,l2);

        l1 = getDoctorsFromRepository(new long[]{2L,1L,4L,3L,5L});
        l2 = doctorService.sortDoctorsByDateRenewedMostRecentLast(l2);
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure the service sorts the list of doctors properly; alphabetically by first name.
    public void sortByFirstNameTest() {
        List<Doctor> l1 = getDoctorsFromRepository(new long[]{1L,3L,4L,2L,5L});
        List<Doctor> l2 = doctorService.sortDoctorsByFirstName(doctorService.getAllDoctors());
        Assert.assertEquals(l1,l2);
    }

    @Test
    @DatabaseSetup("/dataSet3.xml")
    // Making sure the service sorts the list of doctors properly; alphabetically by last name.
    public void sortByLastNameTest() {
        List<Doctor> l1 = getDoctorsFromRepository(new long[]{5L,2L,3L,4L,1L});
        List<Doctor> l2 = doctorService.sortDoctorsByFirstName(doctorService.getAllDoctors());
        Assert.assertEquals(l1,l2);
    }

    // Use this to create a list of patients with a certain sequence of IDs.
    private List<Doctor> getDoctorsFromRepository(long[] ids) {
        List<Doctor> l1 = new ArrayList<>();
        for (long id : ids) {
            l1.add(doctorRepository.findById(id));
        }
        return l1;
    }
}