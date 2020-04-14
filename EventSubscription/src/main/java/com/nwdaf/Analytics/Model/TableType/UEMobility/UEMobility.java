package com.nwdaf.Analytics.Model.TableType.UEMobility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UEMobility {


    String supi;
    Date ts;
    Integer DurationSec;
    String locationInfo;


    public UEMobility(String supi, Integer DurationSec, String locationInfo)
    {
        this.supi = supi;
        this.DurationSec = DurationSec;
        this.locationInfo = locationInfo;
    }
}
