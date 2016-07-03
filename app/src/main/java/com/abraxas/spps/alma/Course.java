package com.abraxas.spps.alma;

import java.io.Serializable;

public class Course implements Serializable {
    SchoolClass fsem, ssem;
    public final static int FIRST_SEMESTER = 0;
    public final static int SECOND_SEMESTER = 1;

    public Course(){

    }

    public Course(SchoolClass firstSemester, SchoolClass secondSemester){
        fsem = firstSemester;
        ssem = secondSemester;
    }

    public void setFirstSemester(SchoolClass sc){
        fsem = sc;
    }

    public void setSecondSemester(SchoolClass sc){
        ssem = sc;
    }

    public SchoolClass getFirstSemester(){
        return fsem;
    }

    public SchoolClass getSecondSemester(){
        return ssem;
    }
}
