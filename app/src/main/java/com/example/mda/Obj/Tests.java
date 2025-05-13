package com.example.mda.Obj;

public class Tests {
    public Tests() {
    }
    String pulse, bloodPressure, oxygenSaturation, respirations, pupilEquality;
    public Tests(String pulse, String bloodPressure, String oxygenSaturation, String respirations, String pupilEquality) {
        this.pulse = pulse;
        this.bloodPressure = bloodPressure;
        this.oxygenSaturation = oxygenSaturation;
        this.respirations = respirations;
        this.pupilEquality = pupilEquality;
    }
    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getOxygenSaturation() {
        return oxygenSaturation;
    }

    public void setOxygenSaturation(String oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
    }

    public String getRespirations() {
        return respirations;
    }

    public void setRespirations(String respirations) {
        this.respirations = respirations;
    }

    public String getPupilEquality() {
        return pupilEquality;
    }

    public void setPupilEquality(String pupilEquality) {
        this.pupilEquality = pupilEquality;
    }
}

