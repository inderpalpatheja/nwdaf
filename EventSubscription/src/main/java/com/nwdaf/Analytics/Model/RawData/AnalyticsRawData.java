package com.nwdaf.Analytics.Model.RawData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter @Setter
public class AnalyticsRawData {

    Integer event;
    String snssai;
    Boolean anySlice;
    String supi;
    String tai;
    Boolean anyUe;
    Integer nwPerfType;
    Integer congType;
    Integer excepId;
    Integer maxAnaEntry;
}
