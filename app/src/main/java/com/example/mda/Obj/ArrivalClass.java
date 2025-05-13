package com.example.mda.Obj;

public class ArrivalClass {

    private String condition;      // כיצד נמצא המטופל
    private String location;       // איפה נמצא המטופל
    private String consciousness;  // מצב ההכרה של המטופל
    private String informantName;  // מי סיפר על המטופל
    private String informantDetails; // מה סיפר על המטופל

    // בנאי ריק
    public ArrivalClass() {
    }

    // בנאי עם פרמטרים
    public ArrivalClass(String condition, String location, String consciousness, String informantName, String informantDetails) {
        this.condition = condition;
        this.location = location;
        this.consciousness = consciousness;
        this.informantName = informantName;
        this.informantDetails = informantDetails;
    }

    // Getters ו-Setters
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getConsciousness() {
        return consciousness;
    }

    public void setConsciousness(String consciousness) {
        this.consciousness = consciousness;
    }

    public String getInformantName() {
        return informantName;
    }

    public void setInformantName(String informantName) {
        this.informantName = informantName;
    }

    public String getInformantDetails() {
        return informantDetails;
    }

    public void setInformantDetails(String informantDetails) {
        this.informantDetails = informantDetails;
    }
}
