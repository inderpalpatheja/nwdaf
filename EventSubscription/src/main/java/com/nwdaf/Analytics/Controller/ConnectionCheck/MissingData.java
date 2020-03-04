package com.nwdaf.Analytics.Controller.ConnectionCheck;

import com.nwdaf.Analytics.Model.RawData;
import org.springframework.http.HttpStatus;

public class MissingData {

    String code;
    String message;
    RawData values;
    int error_count;


    public MissingData(RawData values, int error_count)
    {
        code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        message = "Missing Values";
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

    public void setMessage(String message) {
        this.message = message;
    }


    public RawData getValues() {
        return values;
    }

    public void setValues(RawData values) {
        this.values = values;
    }

    public int getError_count() {
        return error_count;
    }

    public void setError_count(int error_count) {
        this.error_count = error_count;
    }
}
