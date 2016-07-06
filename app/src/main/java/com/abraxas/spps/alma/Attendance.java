package com.abraxas.spps.alma;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.IllegalFormatException;

public class Attendance implements Serializable {

    public static final Integer PRESENT = 0, PARTIAL = 1, ABSENT=2, NOT_TAKEN = 3, PARTIAL_EXCUSED=4, ABSENT_EXCUSED=5, NO_SCHOOL=6;

    private HashMap<Calendar,HashMap<String,Integer>> attendance_list = new HashMap<>();

    public Attendance(){}

    public void add(Calendar date, HashMap<String,Integer> classList){
        attendance_list.put(date,classList);
    }

    public Integer[] getAttendanceTotal(){
        int present=0,late=0,absent=0,na=0;
        Calendar[] enteredDates = (Calendar[]) attendance_list.keySet().toArray();
        for (Calendar enteredDate : enteredDates) {
            HashMap<String,Integer> day = attendance_list.get(enteredDate);
            SchoolClass[] classes = (SchoolClass[]) day.keySet().toArray();
            int len = classes.length;
            for(int i=0;i<len;i++){
                int code = day.get(classes[i].getClassTitle());
                switch (code){
                    case 0:
                    case 1:
                    case 4:
                        if(i==0)
                            present++;
                        else
                            switch (day.get(classes[i-1].getClassTitle())){
                                case 2:
                                case 5:
                                    late++;
                            }
                        break;
                    case 2:
                    case 5:
                        if(i==len-1)
                            absent++;
                        break;
                    case 3:
                        if(i==len-1)
                            na++;
                        break;
                    default:
                        na++;
                        break;
                }
            }
        }

        return new Integer[]{present,late,absent,na};
    }

    public Integer[] getAttendanceClass(SchoolClass sc) throws IllegalFormatException {
        int present=0,late=0,absent=0,na=0;
        Calendar[] enteredDates = new Calendar[attendance_list.size()];
        attendance_list.keySet().toArray(enteredDates);
        for(Calendar enteredDate : enteredDates){
            switch(attendance_list.get(enteredDate).get(sc.getClassTitle())){
                case 0:
                    present++;
                    break;
                case 1:
                case 4:
                    late++;
                    break;
                case 2:
                case 5:
                    absent++;
                    break;
                case 3:
                    na++;
                    break;
                default:
                    break;
            }
        }

        return new Integer[]{present,late,absent,na};
    }

    public int get(Calendar calendar){
        HashMap<String, Integer> map = attendance_list.get(calendar);
        String[] key = (String[]) map.keySet().toArray();
        for(int i=0; i<key.length; i++) {
            switch (map.get(key[i])) {
                case 0:
                case 1:
                case 4:
                    return 0;
                case 2:
                case 5:
                    return 1;
                case 3:
                    i++;
                    continue;
                case 6:
                    return 6;
                default:
                    return -1;
            }
        }
        return -1;
    }

}
