package com.nwdaf.Analytics.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class APIBuildInformation {


    // Artifact's name from the pom.xml file
    private String API_NAME;

    // Artifact version
    private String API_VERSION;

    // Date  of the build
    private LocalDate BUILD_DATE;

    // Time of the build
    private LocalTime BUILD_TIME;


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

    public LocalDate getBUILD_DATE() {
        return BUILD_DATE;
    }

    public void setBUILD_DATE(LocalDate BUILD_DATE) {
        this.BUILD_DATE = BUILD_DATE;
    }

    public LocalTime getBUILD_TIME() {
        return BUILD_TIME;
    }

    public void setBUILD_TIME(LocalTime BUILD_TIME) {
        this.BUILD_TIME = BUILD_TIME;
    }

    public APIBuildInformation(String API_NAME, String API_VERSION, LocalDate BUILD_DATE, LocalTime BUILD_TIME) {
        this.API_NAME = API_NAME;
        this.API_VERSION = API_VERSION;
        this.BUILD_DATE = BUILD_DATE;
        this.BUILD_TIME = BUILD_TIME;
    }
}

