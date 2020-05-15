package com.nwdaf.Analytics.Model.NotificationFormat;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class NetworkPerformanceNotification {

    String subscriptionId;
    String notificationURI;
    Integer nwPerfType;
    Integer relativeRatio;
    Integer absoluteNum;
    String mcc;
    String mnc;
    String tac;
}
