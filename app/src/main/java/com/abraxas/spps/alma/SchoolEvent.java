package com.abraxas.spps.alma;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by Paul on 4/13/16.
 */
public class SchoolEvent implements Serializable {

    GregorianCalendar startDate;
    GregorianCalendar endDate;
    String eventName;
    String eventType;

    public SchoolEvent(String eventName, String eventType, GregorianCalendar startDate, GregorianCalendar endDate){
        this.eventName = eventName;
        this.eventType = eventType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventName() {
        return eventName;
    }

}
