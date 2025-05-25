package com.example.mda.Obj;

/**
 * @file Patient.java
 * @brief Data model representing a patient.
 *
 * This class serves as a Plain Old Java Object (POJO) to encapsulate the
 * essential information pertaining to a patient. It includes fields for
 * the patient's full name, identification number, phone number, gender,
 * and age.
 *
 * It provides a constructor to initialize all fields and a default
 * no-argument constructor, which is often required by libraries like
 * Firebase Realtime Database for deserialization. Standard getter and
 * setter methods are included for each field to allow access and
 * modification of the patient's attributes.
 */
// package com.example.mda.Obj; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class Patient {
    String fullName, gender, id, phoneNumber, age;

    /**
     * Constructs a new Patient instance with specified details.
     *
     * @param fullName The full name of the patient.
     * @param id The identification number of the patient.
     * @param phoneNumber The phone number of the patient.
     * @param gender The gender of the patient.
     * @param age The age of the patient.
     */
    public Patient(String fullName, String id, String phoneNumber, String gender, String age) {
        this.fullName = fullName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.age = age;
    }

    /**
     * Default constructor for the Patient class.
     * Required for Firebase Realtime Database deserialization and other
     * frameworks that may instantiate objects via reflection.
     */
    public Patient() {
    }

    /**
     * Gets the full name of the patient.
     * @return The patient's full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the patient.
     * @param fullName The new full name for the patient.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the identification number of the patient.
     * @return The patient's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the identification number of the patient.
     * @param id The new ID for the patient.
     */
    public void setId(String id) {
        this.id=id;
    }

    /**
     * Gets the phone number of the patient.
     * @return The patient's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the patient.
     * @param phoneNumber The new phone number for the patient.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber=phoneNumber;
    }

    /**
     * Gets the gender of the patient.
     * @return The patient's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the patient.
     * @param gender The new gender for the patient.
     */
    public void setGender(String gender) {
        this.gender=gender;
    }

    /**
     * Gets the age of the patient.
     * @return The patient's age.
     */
    public String getAge() {
        return age;
    }

    /**
     * Sets the age of the patient.
     * @param age The new age for the patient.
     */
    public void setAge(String age) {
        this.age=age;
    }
}