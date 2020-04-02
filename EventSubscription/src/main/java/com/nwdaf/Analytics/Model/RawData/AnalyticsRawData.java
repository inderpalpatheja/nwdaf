package com.nwdaf.Analytics.Model.RawData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
public class AnalyticsRawData {

    @Getter @Setter
    String eventID;

    @Getter @Setter
    String snssais;

    @Getter @Setter
    String anySlice;

    @Getter @Setter
    String supi;

}
