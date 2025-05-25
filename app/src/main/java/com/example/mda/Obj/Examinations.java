package com.example.mda.Obj;

/**
 * @file Examinations.java
 * @brief Data model representing findings from a physical examination.
 *
 * This class serves as a Plain Old Java Object (POJO) to store observations
 * made during a physical examination of a patient. The findings are typically
 * qualitative descriptions rather than quantitative measurements.
 *
 * It includes the following fields, each representing a specific observation:
 * <ul>
 *     <li><b>respiratoryDistress:</b> Description of any signs of respiratory distress. (האם יש סימני מצוקה נשימתית)</li>
 *     <li><b>palenessSweating:</b> Description of paleness or sweating. (האם יש חיוורון והזעה)</li>
 *     <li><b>nauseaVomiting:</b> Description of nausea or vomiting. (האם יש בחילות והקאות)</li>
 *     <li><b>orientation:</b> Description of the patient's orientation to time and space. (האם המטופל מתמצא בזמן ובמרחב)</li>
 *     <li><b>pupilsReaction:</b> Description of pupil equality and reaction to light. (האם האישונים שווים ומגיבים לאור)</li>
 * </ul>
 *
 * The class provides two constructors:
 * 1. A default no-argument constructor (בנאי ריק - Default Constructor), which is
 *    often required by frameworks like Firebase Realtime Database for deserialization.
 * 2. A parameterized constructor (בנאי עם פרמטרים - Constructor with parameters) to
 *    initialize all examination findings at once.
 *
 * Standard getter and setter methods are included for each field, allowing
 * controlled access and modification of the examination findings. This class
 * is used to store the results of the final physical examinations within an
 * {@link com.example.mda.Obj.Anamnesis} record, often using descriptive strings
 * determined by the state of UI switches in an activity like
 * {@link com.example.mda.Activities.FinalExaminations}.
 */
// package com.example.mda.Obj; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class Examinations {
    private String respiratoryDistress; // האם יש סימני מצוקה נשימתית
    private String palenessSweating;    // האם יש חיוורון והזעה
    private String nauseaVomiting;      // האם יש בחילות והקאות
    private String orientation;         // האם המטופל מתמצא בזמן ובמרחב
    private String pupilsReaction;      // האם האישונים שווים ומגיבים לאור

    /**
     * Default constructor for the Examinations class. (בנאי ריק - Default Constructor)
     * Required for frameworks like Firebase Realtime Database for object deserialization.
     */
    public Examinations() {
    }

    /**
     * Constructs a new Examinations instance with specified findings.
     * (בנאי עם פרמטרים - Constructor with parameters)
     *
     * @param respiratoryDistress Description of respiratory distress signs.
     * @param palenessSweating Description of paleness and sweating.
     * @param nauseaVomiting Description of nausea and vomiting.
     * @param orientation Description of patient's orientation.
     * @param pupilsReaction Description of pupil reaction.
     */
    public Examinations(String respiratoryDistress, String palenessSweating, String nauseaVomiting, String orientation, String pupilsReaction) {
        this.respiratoryDistress = respiratoryDistress;
        this.palenessSweating = palenessSweating;
        this.nauseaVomiting = nauseaVomiting;
        this.orientation = orientation;
        this.pupilsReaction = pupilsReaction;
    }

    // Getters ו-Setters (Getters and Setters)

    /**
     * Gets the description of respiratory distress.
     * @return The respiratory distress string.
     */
    public String getRespiratoryDistress() {
        return respiratoryDistress;
    }

    /**
     * Sets the description of respiratory distress.
     * @param respiratoryDistress The new respiratory distress string.
     */
    public void setRespiratoryDistress(String respiratoryDistress) {
        this.respiratoryDistress = respiratoryDistress;
    }

    /**
     * Gets the description of paleness and sweating.
     * @return The paleness and sweating string.
     */
    public String getPalenessSweating() {
        return palenessSweating;
    }

    /**
     * Sets the description of paleness and sweating.
     * @param palenessSweating The new paleness and sweating string.
     */
    public void setPalenessSweating(String palenessSweating) {
        this.palenessSweating = palenessSweating;
    }

    /**
     * Gets the description of nausea and vomiting.
     * @return The nausea and vomiting string.
     */
    public String getNauseaVomiting() {
        return nauseaVomiting;
    }

    /**
     * Sets the description of nausea and vomiting.
     * @param nauseaVomiting The new nausea and vomiting string.
     */
    public void setNauseaVomiting(String nauseaVomiting) {
        this.nauseaVomiting = nauseaVomiting;
    }

    /**
     * Gets the description of the patient's orientation.
     * @return The orientation string.
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Sets the description of the patient's orientation.
     * @param orientation The new orientation string.
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * Gets the description of pupil reaction.
     * @return The pupil reaction string.
     */
    public String getPupilsReaction() {
        return pupilsReaction;
    }

    /**
     * Sets the description of pupil reaction.
     * @param pupilsReaction The new pupil reaction string.
     */
    public void setPupilsReaction(String pupilsReaction) {
        this.pupilsReaction = pupilsReaction;
    }
}