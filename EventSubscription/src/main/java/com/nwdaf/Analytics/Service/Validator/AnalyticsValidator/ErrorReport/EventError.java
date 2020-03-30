package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import org.springframework.http.HttpStatus;

public class EventError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = "Analytics EventID error";

    String eventID;

    public EventError(String eventID)
    { this.eventID = eventID; }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
