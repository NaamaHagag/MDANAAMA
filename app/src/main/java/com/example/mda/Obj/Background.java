package com.example.mda.Obj;

/**
 * @file Background.java
 * @brief Data model representing a patient's medical background.
 *
 * This class serves as a Plain Old Java Object (POJO) to encapsulate
 * information about a patient's medical history, specifically their
 * pre-existing diseases, known allergies, and current medications.
 *
 * It provides a constructor to initialize these three fields and a default
 * no-argument constructor. The no-argument constructor is often required by
 * libraries like Firebase Realtime Database for deserialization when
 * retrieving data.
 *
 * Standard getter and setter methods are included for each field (diseases,
 * allergies, medications) to allow controlled access and modification of
 * the patient's medical background attributes.
 */
// package com.example.mda.Obj; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class Background {
    public Background(){ // Default constructor
    }
    String diseases,allergies, medications;

    /**
     * Constructs a new Background instance with specified medical details.
     *
     * @param diseases A string describing the patient's pre-existing diseases.
     * @param allergies A string describing the patient's known allergies.
     * @param medications A string describing the patient's current medications.
     */
    public Background(String diseases, String allergies,  String medications){
        this.diseases= diseases;
        this.allergies= allergies;
        this.medications= medications;
    }

    /**
     * Gets the patient's current medications.
     * @return A string listing the medications.
     */
    public String getMedications() {
        return medications;
    }

    /**
     * Sets the patient's current medications.
     * @param medications A string listing the new medications.
     */
    public void setMedications(String medications) {
        this.medications = medications;
    }

    /**
     * Gets the patient's known allergies.
     * @return A string listing the allergies.
     */
    public String getAllergies() {
        return allergies;
    }

    /**
     * Sets the patient's known allergies.
     * @param allergies A string listing the new allergies.
     */
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    /**
     * Gets the patient's pre-existing diseases.
     * @return A string listing the diseases.
     */
    public String getDiseases() {
        return diseases;
    }

    /**
     * Sets the patient's pre-existing diseases.
     * @param diseases A string listing the new diseases.
     */
    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }
}