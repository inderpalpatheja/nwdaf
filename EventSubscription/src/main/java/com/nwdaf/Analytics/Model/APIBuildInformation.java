package com.nwdaf.Analytics.Model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class APIBuildInformation {


    private String API_NAME;
    private String API_VERSION;
    private Instant API_BUILD_TIME;

    public APIBuildInformation() {
    }


    public String getAPI_NAME() {
        return API_NAME;
    }

    public void setAPI_NAME(String API_NAME) {
        this.API_NAME = API_NAME;
    }

    public String getAPI_VERSION() {
        return API_VERSION;
    }

    public void setAPI_VERSION(String API_VERSION) {
        this.API_VERSION = API_VERSION;
    }

    public Instant getAPI_BUILD_TIME() {
        return API_BUILD_TIME;
    }

    public void setAPI_BUILD_TIME(Instant API_BUILD_TIME) {
        this.API_BUILD_TIME = API_BUILD_TIME;
    }

    public APIBuildInformation(String API_NAME, String API_VERSION, Instant API_BUILD_TIME) {

        this.API_NAME = API_NAME;
        this.API_VERSION = API_VERSION;
        this.API_BUILD_TIME = API_BUILD_TIME;
    }
}

