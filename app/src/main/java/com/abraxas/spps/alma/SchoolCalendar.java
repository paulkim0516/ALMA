package com.abraxas.spps.alma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SchoolCalendar implements Serializable{

    Map<Integer, List<SchoolEvent>> schoolCalendar;

    public SchoolCalendar(){
        schoolCalendar = new HashMap<>();
    }

    public void addEvent(SchoolEvent event){
        int month = event.getStartDate().get(GregorianCalendar.MONTH);
        if(schoolCalendar.get(month)!=null)
            schoolCalendar.get(month).add(event);
        else{
            List<SchoolEvent> eventList = new ArrayList<>();
            eventList.add(event);
            schoolCalendar.put(month, eventList);
        }
    }

    public List<SchoolEvent> getEventList(int month){
        return schoolCalendar.get(month);
    }

    public List<SchoolEvent> getEventList(int month, String eventType){
        List<SchoolEvent> eventList = new ArrayList<>();
        List<SchoolEvent> monthList = schoolCalendar.get(month);
        for(SchoolEvent event: monthList)
            if(event.getEventType().equals(eventType))
                eventList.add(event);

        return eventList;
    }

    public List<SchoolEvent> getEventList(String eventType){
        Set<Integer> keySet = schoolCalendar.keySet();
        List<SchoolEvent> eventList = new ArrayList<>();
        for (Integer i: keySet) {
            eventList.addAll(getEventList(i,eventType));
        }

        return eventList;
    }
}
