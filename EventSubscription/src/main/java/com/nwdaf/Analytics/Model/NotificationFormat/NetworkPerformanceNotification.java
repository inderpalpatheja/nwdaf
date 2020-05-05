package com.nwdaf.Analytics.Model.NotificationFormat;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class NetworkPerformanceNotification {

    String subscriptionID;
    String notificationURI;
    Integer nwPerfType;
    Integer relativeRatio;
    Integer absoluteSum;
    String mcc;
    String mnc;
    String tac;
}
