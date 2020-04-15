package com.nwdaf.Analytics.Model.TableType.UEMobility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UEMobilityInformation {


    String supi;
    Date ts;
    Integer DurationSec;


    public UEMobilityInformation(String supi, Integer DurationSec)
    {
        this.supi = supi;
        this.DurationSec = DurationSec;
    }
}
