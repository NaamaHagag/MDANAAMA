package com.example.mda.Obj;

/**
 * @file ArrivalClass.java
 * @brief Data model representing details about a patient's arrival and informant.
 *
 * This class serves as a Plain Old Java Object (POJO) to encapsulate
 * information related to a patient's condition and circumstances upon arrival,
 * as well as details about the informant who provided the initial information.
 *
 * It includes the following fields:
 * <ul>
 *     <li><b>condition:</b> Describes how the patient was found or their state upon arrival. (כיצד נמצא המטופל)</li>
 *     <li><b>location:</b> Specifies where the patient was found or the location of the incident. (איפה נמצא המטופל)</li>
 *     <li><b>consciousness:</b> Describes the patient's level of consciousness. (מצב ההכרה של המטופל)</li>
 *     <li><b>informantName:</b> The name of the person who provided information about the patient. (מי סיפר על המטופל)</li>
 *     <li><b>informantDetails:</b> The actual information or story provided by the informant. (מה סיפר על המטופל)</li>
 * </ul>
 *
 * The class provides two constructors:
 * 1. A default no-argument constructor, essential for frameworks like Firebase
 *    Realtime Database for deserialization. (בנאי ריק)
 * 2. A parameterized constructor to initialize all fields when creating an instance.
 *
 * Standard getter and setter methods are included for each field, allowing
 * controlled access and modification of the arrival and informant attributes.
 */
// package com.example.mda.Obj; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class ArrivalClass {

    private String condition;      // כיצד נמצא המטופל (How the patient was found)
    private String location;       // איפה נמצא המטופל (Where the patient was found)
    private String consciousness;  // מצב ההכרה של המטופל (Patient's state of consciousness)
    private String informantName;  // מי סיפר על המטופל (Who told about the patient)
    private String informantDetails; // מה סיפר על המטופל (What was told about the patient)

    /**
     * Default constructor for ArrivalClass. (בנאי ריק - Empty constructor)
     * This is often required by frameworks like Firebase for object deserialization.
     */
    public ArrivalClass() {
    }

    /**
     * Constructs a new ArrivalClass instance with specified details.
     * (בנאי עם פרמטרים - Constructor with parameters)
     *
     * @param condition The patient's condition upon arrival.
     * @param location The location where the patient was found or the incident occurred.
     * @param consciousness The patient's level of consciousness.
     * @param informantName The name of the informant.
     * @param informantDetails The details provided by the informant.
     */
    public ArrivalClass(String condition, String location, String consciousness, String informantName, String informantDetails) {
        this.condition = condition;
        this.location = location;
        this.consciousness = consciousness;
        this.informantName = informantName;
        this.informantDetails = informantDetails;
    }

    // Getters ו-Setters (Getters and Setters)

    /**
     * Gets the patient's condition upon arrival.
     * @return The condition string.
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the patient's condition upon arrival.
     * @param condition The new condition string.
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the location where the patient was found or the incident occurred.
     * @return The location string.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location where the patient was found or the incident occurred.
     * @param location The new location string.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the patient's level of consciousness.
     * @return The consciousness state string.
     */
    public String getConsciousness() {
        return consciousness;
    }

    /**
     * Sets the patient's level of consciousness.
     * @param consciousness The new consciousness state string.
     */
    public void setConsciousness(String consciousness) {
        this.consciousness = consciousness;
    }

    /**
     * Gets the name of the informant.
     * @return The informant's name string.
     */
    public String getInformantName() {
        return informantName;
    }

    /**
     * Sets the name of the informant.
     * @param informantName The new informant's name string.
     */
    public void setInformantName(String informantName) {
        this.informantName = informantName;
    }

    /**
     * Gets the details provided by the informant.
     * @return The informant's details string.
     */
    public String getInformantDetails() {
        return informantDetails;
    }

    /**
     * Sets the details provided by the informant.
     * @param informantDetails The new informant's details string.
     */
    public void setInformantDetails(String informantDetails) {
        this.informantDetails = informantDetails;
    }
}