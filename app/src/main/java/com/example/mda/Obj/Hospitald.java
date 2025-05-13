package com.example.mda.Obj;

public class Hospitald {
    private String name;
    private String department;
    private String staff;

    // בנאי ריק (נדרש עבור Firebase)
    public Hospitald() {
    }

    // בנאי עם פרמטרים
    public Hospitald(String name, String department, String staff) {
        this.name = name;
        this.department = department;
        this.staff = staff;
    }

    // Getter ו- Setter עבור שם בית החולים
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter ו- Setter עבור מחלקה בבית החולים
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Getter ו- Setter עבור שם איש צוות מקבל
    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }
}
