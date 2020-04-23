package com.nwdaf.Analytics.Model.AnalyticsInformation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class QosSustainabilityInfo {

    String message;
    Boolean data_status;
    String plmnID;
    String snssais;
    Integer qosFlowRetain;
    Integer ranUeThroughput;

}
