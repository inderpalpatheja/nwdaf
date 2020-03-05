package com.nwdaf.Analytics.Controller.ConnectionCheck;

import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import org.springframework.http.HttpStatus;

public class MissingData {

    String code;
    final String message = "Missing Values";
    Object values;
    int error_count;


    public MissingData(Object values, int error_count)
    {
        code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        this.values = values;
        this.error_count = error_count;
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

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }

    public int getError_count() {
        return error_count;
    }

    public void setError_count(int error_count) {
        this.error_count = error_count;
    }
}
