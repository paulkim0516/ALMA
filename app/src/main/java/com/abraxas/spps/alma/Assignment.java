package com.abraxas.spps.alma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Assignment implements Serializable{
    private GregorianCalendar dueDate;
    private String taskTitle;
    private String taskCategoryTitle;
    private double weight;
    private Grade taskGrade;
    private String statusMsg;
    private String taskDetail;
    private List<String> file = new ArrayList<>();

    public Assignment(){

    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDueDate(GregorianCalendar dueDate) {
        this.dueDate = dueDate;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public void setTaskCategory(String taskCategoryTitle) {
        this.taskCategoryTitle = taskCategoryTitle;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public void setTaskGrade(Grade grade) {
        this.taskGrade = grade;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public GregorianCalendar getDueDate() {
        return this.dueDate;
    }

    public String getTaskTitle(){
        return this.taskTitle;
    }

    public String getTaskCategory() {
        return this.taskCategoryTitle;
    }

    public String getStatusMsg(){
        return this.statusMsg;
    }

    public String getTaskDetail(){
        return this.taskDetail;
    }

    public List<String> getFile(){
        return this.file;
    }

    public Grade getTaskGrade(){
        return this.taskGrade;
    }

    public double getWeight() {
        return weight;
    }
}