package com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class EventConnectionUE {

    private Boolean DataStatus;
    private String Message;
    private String supi;
    private Date ts;
    private int DurationSec;
    private String location;


    @Override
    public String toString() {
        return "EventConnectionUE{" +
                "DataStatus=" + DataStatus +
                ", Message='" + Message + '\'' +
                ", supi='" + supi + '\'' +
                ", ts=" + ts +
                ", DurationSec=" + DurationSec +
                ", location='" + location + '\'' +
                '}';
    }
}