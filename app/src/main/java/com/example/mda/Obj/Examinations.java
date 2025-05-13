package com.example.mda.Obj;

public class Examinations {
    private String respiratoryDistress; // האם יש סימני מצוקה נשימתית
    private String palenessSweating;    // האם יש חיוורון והזעה
    private String nauseaVomiting;      // האם יש בחילות והקאות
    private String orientation;         // האם המטופל מתמצא בזמן ובמרחב
    private String pupilsReaction;      // האם האישונים שווים ומגיבים לאור

    // בנאי ריק (Default Constructor)
    public Examinations() {
    }

    // בנאי עם פרמטרים
    public Examinations(String respiratoryDistress, String palenessSweating, String nauseaVomiting, String orientation, String pupilsReaction) {
        this.respiratoryDistress = respiratoryDistress;
        this.palenessSweating = palenessSweating;
        this.nauseaVomiting = nauseaVomiting;
        this.orientation = orientation;
        this.pupilsReaction = pupilsReaction;
    }

    // Getters ו-Setters
    public String getRespiratoryDistress() {
        return respiratoryDistress;
    }

    public void setRespiratoryDistress(String respiratoryDistress) {
        this.respiratoryDistress = respiratoryDistress;
    }

    public String getPalenessSweating() {
        return palenessSweating;
    }

    public void setPalenessSweating(String palenessSweating) {
        this.palenessSweating = palenessSweating;
    }

    public String getNauseaVomiting() {
        return nauseaVomiting;
    }

    public void setNauseaVomiting(String nauseaVomiting) {
        this.nauseaVomiting = nauseaVomiting;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getPupilsReaction() {
        return pupilsReaction;
    }

    public void setPupilsReaction(String pupilsReaction) {
        this.pupilsReaction = pupilsReaction;
    }
}
