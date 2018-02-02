package com.quantech.entities.patient;

import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.user.UserCore;
import com.quantech.misc.Title;
import com.quantech.entities.ward.Ward;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
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

    // Empty constructor.
    public Patient() {
    }

    public Patient(Doctor doctor,
                   Title title,
                   String firstName,
                   String lastName,
                   Date birthDate,
                   Long nHSNumber,
                   Long hospitalNumber,
                   Ward ward,
                   String bed,
                   Date dateOfAdmission,
                   String relevantHistory,
                   String socialIssues,
                   String risks,
                   String recommendations,
                   String diagnosis) {
        this.doctor = (Doctor) nullCheck(doctor,"doctor");
        this.title = (Title) nullCheck(title,"title");;
        this.firstName = putNameIntoCorrectForm( nameValidityCheck(firstName) );
        this.lastName = putNameIntoCorrectForm( nameValidityCheck(lastName) );
        this.birthDate = birthDateValidityChecker(birthDate);
        this.nHSNumber = NHSNumberValidityCheck(nHSNumber);
        this.hospitalNumber = (Long) nullCheck(hospitalNumber,"hospital number");
        this.ward = (Ward) nullCheck(ward,"ward");
        this.bed = (String) nullCheck(bed,"bed");
        this.dateOfAdmission =  dateOfAdmissionValidityCheck(dateOfAdmission);
        this.relevantHistory = (String) nullCheck(relevantHistory,"relevant history");
        this.socialIssues = (String) nullCheck(socialIssues,"social issues");
        this.risks = (String) nullCheck(risks,"risks");
        this.recommendations = (String) nullCheck(recommendations,"recommendations");
        this.diagnosis = (String) nullCheck(diagnosis,"diagnosis");
    }

    public Long getId() {
        return id;
    }

    // Should this method actually be here?
    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        nullCheck(doctor,"doctor");
        this.doctor = doctor;
    }
    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        nullCheck(title,"title");
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the patient, formatted in the correct way.
     * @param firstName The first name to use.
     * @throws NullPointerException If the given first name is null
     * @throws IllegalArgumentException If the first name takes the form " *".
     */
    public void setFirstName(String firstName) throws NullPointerException, IllegalArgumentException {
        nameValidityCheck(firstName);
        this.firstName = putNameIntoCorrectForm(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the patient, formatted in the correct way.
     * @param lastName The first name to use.
     * @throws NullPointerException If the given first name is null
     * @throws IllegalArgumentException If the first name takes the form " *".
     */
    public void setLastName(String lastName) throws NullPointerException, IllegalArgumentException {
        nameValidityCheck(lastName);
        this.lastName = putNameIntoCorrectForm(lastName);
    }

    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Setter for DOB
     * @param birthDate Date of birth.
     * @throws NullPointerException if null.
     * @throws IllegalArgumentException if birth date after patient's date of admission.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDateValidityChecker(birthDate);
    }

    // Use this to check if birth date is valid before setting an attribute with it.
    private Date birthDateValidityChecker(Date birthDate) {
        nullCheck(birthDate,"date of birth");
        if (this.dateOfAdmission != null && dateOfAdmission.before(birthDate))
            throw new IllegalArgumentException("Error: patient's date of admission cannot be before birth date");
        return birthDate;
    }

    public Long getNHSNumber() {
        return nHSNumber;
    }

    /**
     * Sets the NHS number of the patient.
     * @param NHSNumber The NHS number to provide the patient.
     * @throws NullPointerException If the NHS number provided is null.
     * @throws IllegalArgumentException If either the number doesn't have 10 digits, or the checksum doesn't match.
     */
    public void setNHSNumber (Long NHSNumber) throws NullPointerException, IllegalArgumentException {
        this.nHSNumber = NHSNumberValidityCheck(NHSNumber);
    }

    // Used to check that an NHS number is valid before setting an attribute to be equal to it.
    private Long NHSNumberValidityCheck(Long NHSNumber) {
        nullCheck(NHSNumber,"NHS number");

        int digits = NHSNumber.toString().length();

        if (digits > 10)
            throw new IllegalArgumentException("Error: NHS number has too many digits.");

        // Check that the checksum is correct.
        if ( !checksumCorrect(NHSNumber) )
            throw new IllegalArgumentException("Error: NHS number is not valid (checksum does not match)");

        return NHSNumber;
    }

    // Checks that the checksum of the NHS number is correct.
    public int checkDigit(Long n) {
        String digits = n.toString();

        if (digits.length() < 10) {
            StringBuilder zeros = new StringBuilder();
            for (int i = 0; i < 10 - digits.length(); i++) {
                zeros.append('0');
            }
            digits = zeros.toString() + digits;
        }

        // Applying Modulus 11 algorithm:
        // Source: http://www.datadictionary.nhs.uk/data_dictionary/attributes/n/nhs/nhs_number_de.asp?shownav=1
        // 1- Apply factors:
        int sum = 0; int factor = 10;
        for (int i = 0; i < 9; i++) {
            int dig = Character.getNumericValue(digits.charAt(i));
            sum += dig*factor;
            factor--;
        }
        // 2- Find the remainder of dividing by 11.
        int r = sum % 11;
        // 3- Take the remainder away from 11 to get the check digit.
        int checkDigit = 11 - r;
        // 4- If the value is 10 then the check digit used is 0. If it is 0, then the number is invalid.
        if (checkDigit == 10)
            return -1;
        if (checkDigit == 11)
            checkDigit = 0;
        // 5- Check the remainder matches the check digit.
        return checkDigit;
    }

    // Checks if the check digit generated from an NHS number matches the checksum provided in the number.
    private boolean checksumCorrect(Long n) {
        String digits = n.toString();
        int checkSum = Character.getNumericValue(digits.charAt(digits.length()-1));
        int checkDigit = checkDigit(n);
        return (checkSum == checkDigit && checkDigit != -1);
    }

    public Long getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(Long hospitalNumber) {
        nullCheck(hospitalNumber,"hospital number");
        this.hospitalNumber = hospitalNumber;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        nullCheck(ward,"ward");
        this.ward = ward;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        nullCheck(bed,"bed");
        this.bed = bed;
    }

    public Date getDateOfAdmission() {
        return dateOfAdmission;
    }

    /**
     * Setter for DOA.
     * @param dateOfAdmission The patient's date of admission.
     * @throws NullPointerException if the value is null.
     * @throws IllegalArgumentException if the date of admission is before the patient's date of birth.
     */
    public void setDateOfAdmission(Date dateOfAdmission) throws NullPointerException, IllegalArgumentException {
        this.dateOfAdmission = dateOfAdmissionValidityCheck(dateOfAdmission);
    }

    private Date dateOfAdmissionValidityCheck(Date dateOfAdmission) {
        nullCheck(dateOfAdmission, "date of admission");
        if (birthDate != null && birthDate.after(dateOfAdmission))
            throw new IllegalArgumentException("Error: date of admission cannot be after patient's date of birth.");
        return dateOfAdmission;
    }

    public String getRelevantHistory() {
        return relevantHistory;
    }

    public void setRelevantHistory(String relevantHistory) {
        nullCheck(relevantHistory,"relevantHistory");
        this.relevantHistory = relevantHistory;
    }

    public String getSocialIssues() {
        return socialIssues;
    }

    public void setSocialIssues(String socialIssues) {
        nullCheck(socialIssues,"socialIssues");
        this.socialIssues = socialIssues;
    }

    public String getRisks() {
        return risks;
    }

    public void setRisks(String risks) {
        nullCheck(risks,"risks");
        this.risks = risks;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        nullCheck(recommendations,"recommendations");
        this.recommendations = recommendations;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        nullCheck(diagnosis,"diagnosis");
        this.diagnosis = diagnosis;
    }

    // Throws NullPointerException with custom error message.
    private Object nullCheck(Object obj, String name) throws NullPointerException {
        if (obj == null)
            throw new NullPointerException("Error: " + name + " in patient cannot be assigned null value.");
        return obj;
    }

    // Checks if a given name is valid.
    private String nameValidityCheck(String name) {
        nullCheck(name,"name");
        if (name.matches(" *")) {
            throw new IllegalArgumentException("Error: Name cannot take the given form (empty string)");
        }
        return name;
    }

    // Places the given name into the correct form
    // e.g. nuha tumia -> Nuha Tumia
    // e.g. hARRy o'doNNel -> Harry O'Donnel etc..
    private String putNameIntoCorrectForm(String name) {
        // Look at spaces.
        name = name.toLowerCase().trim();
        return formatIntoNameAroundString(formatIntoNameAroundString(formatIntoNameAroundString(name," "),"-"),"'");
    }

    private String formatIntoNameAroundString(String name, String regex) {
        String[] s = name.split(regex+"+");
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].trim();
            String x = s[i].substring(0,1).toUpperCase();
            s[i] = x + s[i].substring(1);

            if (i < s.length - 1) {
                s[i] = s[i] + regex;
            }
        }
        return Arrays.stream(s).reduce("", String::concat);
    }
}
