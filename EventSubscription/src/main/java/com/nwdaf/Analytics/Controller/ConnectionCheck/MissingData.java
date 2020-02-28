package com.nwdaf.Analytics.Controller.ConnectionCheck;

import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import org.springframework.http.HttpStatus;

public class MissingData {

    String code;
    String message;
    NnwdafEventsSubscription subscription;


    public MissingData(NnwdafEventsSubscription subscription)
    {
        code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        message = "Missing Values";
        this.subscription = subscription;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NnwdafEventsSubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(NnwdafEventsSubscription subscription) {
        this.subscription = subscription;
    }

}
