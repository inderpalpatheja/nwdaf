package com.nwdaf.Analytics.Model.TableType.UeComm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UeCommInformation {

    Integer ID;
    String supi;
    Integer commDur;
    String ts;
    Integer ulVol;
    Integer dlVol;
}
