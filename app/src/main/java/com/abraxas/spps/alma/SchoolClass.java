package com.abraxas.spps.alma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchoolClass implements Serializable{
    private String _classTitle;
    private int _classPeriod;
    private Name _teacherName;
    private Grade _overallGrade = new Grade(0,null,null);
    private List<Category> _category = new ArrayList<>();
    private List<Assignment> _assignmentList = new ArrayList<>();
    private String _roomNumber;
    private String _classTime;
    private String _classId;

    public SchoolClass(String classTitle, int classPeriod, String classTime, Name teacherName,String roomNumber, Grade overallGrade, List<Category> category, List<Assignment> assignmentList, String classId){
        this._assignmentList = assignmentList;
        this._classTime = classTime;
        this._category = category;
        this._classPeriod = classPeriod;
        this._classTitle = classTitle;
        this._overallGrade = overallGrade;
        this._teacherName = teacherName;
        this._roomNumber = roomNumber;
        this._classId = classId;
    }

    public SchoolClass(){

    }

    public void setAssignmentList(List<Assignment> _assignmentList) {
        this._assignmentList = _assignmentList;
    }

    public void setCategory(List<Category> _category) {
        this._category = _category;
    }

    public void setClassPeriod(int _classPeriod) {
        this._classPeriod = _classPeriod;
    }

    public void setClassTime(String _classTime) {
        this._classTime = _classTime;
    }

    public void setClassTitle(String _classTitle) {
        this._classTitle = _classTitle;
    }

    public void setOverallGrade(Grade _overallGrade) {
        this._overallGrade = _overallGrade;
    }

    public void setTeacherName(Name _teacherName) {
        this._teacherName = _teacherName;
    }

    public void setRoomNumber(String _roomNumber) {
        this._roomNumber = _roomNumber;
    }

    public void setClassId(String _classId) {
        this._classId = _classId;
    }

    public String getClassId() {
        return _classId;
    }

    public Grade getOverallGrade() {
        return _overallGrade;
    }

    public int getClassPeriod() {
        return _classPeriod;
    }

    public String getClassTime() {
        return _classTime;
    }

    public List<Assignment> getAssignmentList() {
        return _assignmentList;
    }

    public List<Category> getCategory() {
        return _category;
    }

    public Name getTeacherName() {
        return _teacherName;
    }

    public String getClassTitle() {
        return _classTitle;
    }

    public String getRoomNumber() {
        return _roomNumber;
    }
}

