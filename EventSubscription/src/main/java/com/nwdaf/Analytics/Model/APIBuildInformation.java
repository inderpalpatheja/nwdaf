package com.nwdaf.Analytics.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class APIBuildInformation {

    private String API_VERSION;
    private String API_NAME;
    private String GROUP_NAME;
    private String API_TIME;


    public APIBuildInformation() {
    }

    public String getAPI_VERSION() {
        return API_VERSION;
    }

    public void setAPI_VERSION(String API_VERSION) {
        this.API_VERSION = API_VERSION;
    }

    public String getAPI_NAME() {
        return API_NAME;
    }

    public void setAPI_NAME(String API_NAME) {
        this.API_NAME = API_NAME;
    }

    public String getGROUP_NAME() {
        return GROUP_NAME;
    }

    public void setGROUP_NAME(String GROUP_NAME) {
        this.GROUP_NAME = GROUP_NAME;
    }

    public String getAPI_TIME() {
        return API_TIME;
    }

    public void setAPI_TIME(String API_TIME) {
        this.API_TIME = API_TIME;
    }

    public APIBuildInformation(String API_VERSION, String API_NAME, String GROUP_NAME, String API_TIME) {
        this.API_VERSION = API_VERSION;
        this.API_NAME = API_NAME;
        this.GROUP_NAME = GROUP_NAME;
        this.API_TIME = API_TIME;
    }
}

