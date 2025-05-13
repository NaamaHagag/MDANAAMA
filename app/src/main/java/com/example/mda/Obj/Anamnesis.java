package com.example.mda.Obj;

public class Anamnesis {
    private ArrivalClass arrival;
    private Background background;
    private Tests final_examinations;
    private Hospitald hospital;
    private Patient details;
    private Tests medical_tests;
    private String KeyID;

    // בנאי ריק
    public Anamnesis() {
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public ArrivalClass getArrival() {
        return arrival;
    }

    public void setArrival(ArrivalClass arrival) {
        this.arrival = arrival;
    }

    public Tests getFinal_examinations() {
        return final_examinations;
    }

    public void setFinal_examinations(Tests final_examinations) {
        this.final_examinations = final_examinations;
    }

    public Hospitald getHospital() {
        return hospital;
    }

    public void setHospital(Hospitald hospital) {
        this.hospital = hospital;
    }

    public Patient getDetails() {
        return details;
    }

    public void setDetails(Patient details) {
        this.details = details;
    }

    public Tests getMedical_tests() {
        return medical_tests;
    }

    public void setMedical_tests(Tests medical_tests) {
        this.medical_tests = medical_tests;
    }

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String keyID) {
        KeyID = keyID;
    }

}
