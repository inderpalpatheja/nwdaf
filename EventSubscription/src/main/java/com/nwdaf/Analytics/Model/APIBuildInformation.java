package com.nwdaf.Analytics.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class APIBuildInformation {

    private String API_NAME;
    private String API_VERSION;
    private Instant API_BUILD_TIME;

}

