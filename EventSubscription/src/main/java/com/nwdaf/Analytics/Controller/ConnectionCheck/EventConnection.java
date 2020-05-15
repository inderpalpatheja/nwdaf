package com.nwdaf.Analytics.Controller.ConnectionCheck;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class EventConnection {

    private Boolean DataStatus;
    private String Message;
    private String snssai;
    private int currentLoadLevelInfo;

}