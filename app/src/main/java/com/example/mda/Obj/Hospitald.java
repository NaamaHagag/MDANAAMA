package com.example.mda.Obj;

/**
 * @file Hospitald.java
 * @brief Data model representing hospital and staff details.
 *
 * This class serves as a Plain Old Java Object (POJO) to encapsulate
 * information about the hospital, the specific department, and the staff
 * member involved in a patient's care or handover.
 *
 * It includes the following fields:
 * <ul>
 *     <li><b>name:</b> The name of the hospital. (שם בית החולים)</li>
 *     <li><b>department:</b> The specific department within the hospital. (מחלקה בבית החולים)</li>
 *     <li><b>staff:</b> The name of the staff member (e.g., receiving staff). (שם איש צוות מקבל)</li>
 * </ul>
 *
 * The class provides two constructors:
 * 1. A default no-argument constructor (בנאי ריק - Empty constructor), which is
 *    essential for frameworks like Firebase Realtime Database for object deserialization.
 * 2. A parameterized constructor (בנאי עם פרמטרים - Constructor with parameters)
 *    to initialize all fields when creating an instance of {@code Hospitald}.
 *
 * Standard getter and setter methods are included for each field (name, department,
 * staff), allowing controlled access and modification of the hospital and staff attributes.
 * This class is used to store these details as part of an
 * {@link com.example.mda.Obj.Anamnesis} record.
 */
// package com.example.mda.Obj; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class Hospitald {
    private String name;
    private String department;
    private String staff;

    /**
     * Default constructor for Hospitald. (בנאי ריק)
     * This is required for Firebase and other frameworks for deserialization.
     */
    public Hospitald() {
    }

    /**
     * Constructs a new Hospitald instance with specified details.
     * (בנאי עם פרמטרים - Constructor with parameters)
     *
     * @param name The name of the hospital.
     * @param department The department in the hospital.
     * @param staff The name of the staff member.
     */
    public Hospitald(String name, String department, String staff) {
        this.name = name;
        this.department = department;
        this.staff = staff;
    }

    /**
     * Gets the name of the hospital.
     * (Getter ו- Setter עבור שם בית החולים - Getter and Setter for hospital name)
     * @return The name of the hospital.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the hospital.
     * @param name The new name of the hospital.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the department in the hospital.
     * (Getter ו- Setter עבור מחלקה בבית החולים - Getter and Setter for hospital department)
     * @return The hospital department.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department in the hospital.
     * @param department The new hospital department.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the name of the staff member.
     * (Getter ו- Setter עבור שם איש צוות מקבל - Getter and Setter for receiving staff member's name)
     * @return The name of the staff member.
     */
    public String getStaff() {
        return staff;
    }

    /**
     * Sets the name of the staff member.
     * @param staff The new name of the staff member.
     */
    public void setStaff(String staff) {
        this.staff = staff;
    }
}
