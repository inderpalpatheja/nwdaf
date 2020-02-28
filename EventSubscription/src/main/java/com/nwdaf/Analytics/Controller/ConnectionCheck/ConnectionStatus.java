package com.nwdaf.Analytics.Controller.ConnectionCheck;

import org.springframework.http.HttpStatus;

public class ConnectionStatus {

    String code;
    String message;

    public ConnectionStatus()
    {
        this.code = "Data Not Found";
        this.message = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()));
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
}
