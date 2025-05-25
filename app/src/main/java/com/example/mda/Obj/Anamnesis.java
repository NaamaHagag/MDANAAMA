package com.example.mda.Obj;

/**
 * @file Anamnesis.java
 * @brief Data model representing the complete anamnesis (medical history) for a patient event.
 *
 * This class acts as a comprehensive container for all information related to a single
 * patient encounter or event. It aggregates various pieces of data including:
 * <ul>
 *     <li>Patient details (via {@link com.example.mda.Obj.Patient})</li>
 *     <li>Arrival information (via {@link com.example.mda.Obj.ArrivalClass})</li>
 *     <li>Medical background (via {@link com.example.mda.Obj.Background})</li>
 *     <li>Initial medical tests performed (via {@link com.example.mda.Obj.Tests})</li>
 *     <li>Hospitalization details (via {@link com.example.mda.Obj.Hospitald})</li>
 *     <li>Final examinations or conclusions (via {@link com.example.mda.Obj.Tests})</li>
 * </ul>
 *
 * It also includes a `KeyID`, which typically serves as a unique identifier for this
 * specific anamnesis record, often timestamp-based.
 *
 * The class provides a default no-argument constructor (essential for frameworks like
 * Firebase for deserialization) and standard getter and setter methods for each aggregated
 * object and the `KeyID`. This allows for structured storage and retrieval of a complete
 * patient event's data.
 */
// package com.example.mda.Obj; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class Anamnesis {
    private ArrivalClass arrival;
    private Background background;
    private Tests final_examinations;
    private Hospitald hospital;
    private Patient details;
    private Tests medical_tests;
    private String KeyID;

    /**
     * Default constructor for the Anamnesis class.
     * Required for Firebase Realtime Database deserialization and other
     * frameworks that may instantiate objects via reflection.
     * The comment "בנאי ריק" means "empty constructor".
     */
    public Anamnesis() {
    }

    /**
     * Gets the patient's medical background information.
     * @return The {@link Background} object containing medical history.
     */
    public Background getBackground() {
        return background;
    }

    /**
     * Sets the patient's medical background information.
     * @param background The {@link Background} object to set.
     */
    public void setBackground(Background background) {
        this.background = background;
    }

    /**
     * Gets the arrival details for this event.
     * @return The {@link ArrivalClass} object containing arrival information.
     */
    public ArrivalClass getArrival() {
        return arrival;
    }

    /**
     * Sets the arrival details for this event.
     * @param arrival The {@link ArrivalClass} object to set.
     */
    public void setArrival(ArrivalClass arrival) {
        this.arrival = arrival;
    }

    /**
     * Gets the final examinations or conclusions for this event.
     * @return The {@link Tests} object containing final examination data.
     */
    public Tests getFinal_examinations() {
        return final_examinations;
    }

    /**
     * Sets the final examinations or conclusions for this event.
     * @param final_examinations The {@link Tests} object to set for final examinations.
     */
    public void setFinal_examinations(Tests final_examinations) {
        this.final_examinations = final_examinations;
    }

    /**
     * Gets the hospitalization details related to this event.
     * @return The {@link Hospitald} object containing hospitalization information.
     */
    public Hospitald getHospital() {
        return hospital;
    }

    /**
     * Sets the hospitalization details related to this event.
     * @param hospital The {@link Hospitald} object to set.
     */
    public void setHospital(Hospitald hospital) {
        this.hospital = hospital;
    }

    /**
     * Gets the core patient details associated with this event.
     * @return The {@link Patient} object containing patient identification and demographics.
     */
    public Patient getDetails() {
        return details;
    }

    /**
     * Sets the core patient details associated with this event.
     * @param details The {@link Patient} object to set.
     */
    public void setDetails(Patient details) {
        this.details = details;
    }

    /**
     * Gets the initial medical tests performed during this event.
     * @return The {@link Tests} object containing data from initial medical tests.
     */
    public Tests getMedical_tests() {
        return medical_tests;
    }

    /**
     * Sets the initial medical tests performed during this event.
     * @param medical_tests The {@link Tests} object to set for initial medical tests.
     */
    public void setMedical_tests(Tests medical_tests) {
        this.medical_tests = medical_tests;
    }

    /**
     * Gets the unique key identifier for this Anamnesis record.
     * This is often a timestamp-based ID.
     * @return The string KeyID.
     */
    public String getKeyID() {
        return KeyID;
    }

    /**
     * Sets the unique key identifier for this Anamnesis record.
     * @param keyID The string KeyID to set.
     */
    public void setKeyID(String keyID) {
        KeyID = keyID;
    }
}