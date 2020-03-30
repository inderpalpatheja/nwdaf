package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport;

import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import org.springframework.http.HttpStatus;

public class EssentialsError {


    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = "Essential Values error";

    Object eventID;
    Object notificationURI;


    public EssentialsError(SubscriptionRawData rawData)
    {
        this.eventID = rawData.getEventID();
        this.notificationURI = rawData.getNotificationURI();
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getEventID() {
        return eventID;
    }

    public void setEventID(Object eventID) {
        this.eventID = eventID;
    }

    public Object getNotificationURI() {
        return notificationURI;
    }

    public void setNotificationURI(Object notificationURI) {
        this.notificationURI = notificationURI;
    }
}
