package com.example.mda.Obj;

public class Patient {
    String fullName, gender, id, phoneNumber, age;

    public Patient(String fullName, String id, String phoneNumber, String gender, String age) {
        this.fullName = fullName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.age = age;
    }

    public Patient() {
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber=phoneNumber;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender=gender;
    }


    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age=age;
    }

}
