package com.example.mda.Obj;

public class Background {
    public Background(){
    }
    String diseases,allergies, medications;
    public Background(String diseases, String allergies,  String medications){
        this.diseases= diseases;
        this.allergies= allergies;
        this.medications= medications;
    }


    public String getMedications() {
        return medications;
    }
    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getAllergies() {
        return allergies;
    }
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getDiseases() {
        return diseases;
    }
    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }
}
