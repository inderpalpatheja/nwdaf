package com.nwdaf.Analytics.Controller.ConnectionCheck;

import org.springframework.http.HttpStatus;

public class ConnectionStatus {

    String code;
    String message;

    public ConnectionStatus()
    {
        this.code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()));
        this.message = "Data Not Found";
    }

    public ConnectionStatus(HttpStatus code, String message)
    {
        this.code = String.valueOf(HttpStatus.valueOf(code.value()));
        this.message = message;
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
