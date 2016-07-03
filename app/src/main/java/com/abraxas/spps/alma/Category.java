package com.abraxas.spps.alma;

import java.io.Serializable;

public class Category implements Serializable {
    private double _percent;
    private String _categoryTitle;
    private Grade _grade;

    public Category(String categoryTitle, Grade grade, double percent){
        this._categoryTitle = categoryTitle;
        this._grade = grade;
        this._percent = percent;
    }

    public void setPercent(double percent){
        this._percent = percent;
    }

    public void setCategoryTitle(String _categoryTitle) {
        this._categoryTitle = _categoryTitle;
    }

    public void setGrade(Grade _grade) {
        this._grade = _grade;
    }

    public String getCategoryTitle(){
        return this._categoryTitle;
    }

    public Grade getGrade(){
        return this._grade;
    }

    public double getPercent(){
        return this._percent;
    }
}
