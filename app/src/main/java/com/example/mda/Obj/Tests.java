package com.example.mda.Obj;

/**
 * @file Tests.java
 * @brief Data model representing a set of medical test results or observations.
 *
 * This class serves as a Plain Old Java Object (POJO) to store the results
 * from a series of medical tests or physical examinations. It includes fields for:
 * <ul>
 *     <li>Pulse rate</li>
 *     <li>Blood pressure</li>
 *     <li>Oxygen saturation</li>
 *     <li>Respirations (breaths per minute)</li>
 *     <li>Pupil equality/status</li>
 * </ul>
 *
 * The class provides two constructors:
 * 1. A default no-argument constructor, which is often required by libraries
 *    like Firebase Realtime Database for deserialization.
 * 2. A parameterized constructor to initialize all test result fields at once.
 *
 * Standard getter and setter methods are included for each field, allowing
 * controlled access and modification of the test result values. This class
 * can be used to represent initial medical assessments as well as final
 * examinations within an {@link com.example.mda.Obj.Anamnesis} record.
 */
// package com.example.mda.Obj; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class Tests {
    /**
     * Default constructor for the Tests class.
     * Required for Firebase Realtime Database deserialization and other
     * frameworks that may instantiate objects via reflection.
     */
    public Tests() {
    }

    String pulse, bloodPressure, oxygenSaturation, respirations, pupilEquality;

    /**
     * Constructs a new Tests instance with specified test results.
     *
     * @param pulse The patient's pulse rate.
     * @param bloodPressure The patient's blood pressure reading.
     * @param oxygenSaturation The patient's oxygen saturation level.
     * @param respirations The patient's respiration rate.
     * @param pupilEquality A string describing the state of the patient's pupils.
     */
    public Tests(String pulse, String bloodPressure, String oxygenSaturation, String respirations, String pupilEquality) {
        this.pulse = pulse;
        this.bloodPressure = bloodPressure;
        this.oxygenSaturation = oxygenSaturation;
        this.respirations = respirations;
        this.pupilEquality = pupilEquality;
    }

    /**
     * Gets the patient's pulse rate.
     * @return The pulse rate.
     */
    public String getPulse() {
        return pulse;
    }

    /**
     * Sets the patient's pulse rate.
     * @param pulse The new pulse rate.
     */
    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    /**
     * Gets the patient's blood pressure reading.
     * @return The blood pressure reading.
     */
    public String getBloodPressure() {
        return bloodPressure;
    }

    /**
     * Sets the patient's blood pressure reading.
     * @param bloodPressure The new blood pressure reading.
     */
    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    /**
     * Gets the patient's oxygen saturation level.
     * @return The oxygen saturation level.
     */
    public String getOxygenSaturation() {
        return oxygenSaturation;
    }

    /**
     * Sets the patient's oxygen saturation level.
     * @param oxygenSaturation The new oxygen saturation level.
     */
    public void setOxygenSaturation(String oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
    }

    /**
     * Gets the patient's respiration rate (breaths per minute).
     * @return The respiration rate.
     */
    public String getRespirations() {
        return respirations;
    }

    /**
     * Sets the patient's respiration rate.
     * @param respirations The new respiration rate.
     */
    public void setRespirations(String respirations) {
        this.respirations = respirations;
    }

    /**
     * Gets the description of the patient's pupil equality/status.
     * @return A string describing pupil status.
     */
    public String getPupilEquality() {
        return pupilEquality;
    }

    /**
     * Sets the description of the patient's pupil equality/status.
     * @param pupilEquality A string describing the new pupil status.
     */
    public void setPupilEquality(String pupilEquality) {
        this.pupilEquality = pupilEquality;
    }
}