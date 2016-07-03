package com.abraxas.spps.alma;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Grade implements Serializable {
    private double _percent;
    private String _letterGrade;
    private String _feedback;
    private GregorianCalendar _updatedDate;

    public Grade(double percent, String feedback, GregorianCalendar updatedDate){
        this._percent = percent;
        this._letterGrade = convertGrade(percent);
        this._feedback = feedback;
        this._updatedDate = updatedDate;
    }

    public Grade(){

    }

    public void setGrade(double percent){
        this._percent = percent;
        this._letterGrade = convertGrade(percent);
    }

    public void setFeedback(String feedback){
        this._feedback = feedback;
    }

    public void setUpdatedDate(GregorianCalendar updatedDate){
        this._updatedDate = updatedDate;
    }

    public String convertGrade(double percent) {
        String letterGrade;
        if(percent>=97){
            letterGrade = "A+";
        } else if(percent>=93){
            letterGrade = "A";
        } else if(percent>=90){
            letterGrade = "A-";
        } else if(percent>=87){
            letterGrade = "B+";
        } else if(percent>=83){
            letterGrade = "B";
        } else if(percent>=80){
            letterGrade = "B-";
        } else if(percent>=77){
            letterGrade = "C+";
        } else if(percent>=73){
            letterGrade = "C";
        } else if(percent>=70){
            letterGrade = "C-";
        } else if(percent>=67){
            letterGrade = "D+";
        } else if(percent>=63){
            letterGrade = "D";
        } else if(percent>=60){
            letterGrade = "D-";
        } else if(percent==0){
            letterGrade = "-";
        } else {
            letterGrade = "F";
        }
            return letterGrade;
    }

    public String getLetterGrade(){
        return this._letterGrade;
    }

    public String getFeedback(){
        return this._feedback;
    }

    public double getPercent(){
        return this._percent;
    }

    public GregorianCalendar getUpdatedDate(){
        return this._updatedDate;
    }
}
