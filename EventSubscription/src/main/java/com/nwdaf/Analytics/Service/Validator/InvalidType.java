package com.nwdaf.Analytics.Service.Validator;

import org.springframework.http.HttpStatus;

public class InvalidType {

    String code;
    String message;
    Object values;
    int error_count;


    public InvalidType(Object rawData, int error_count)
    {
        this.code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        this.message = "Invalid DataType";
        this.values = rawData;
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
