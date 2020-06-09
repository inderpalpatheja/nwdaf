package com.nwdaf.Analytics.Model.NotificationFormat;

import com.nwdaf.Analytics.Model.CustomData.QosSustainability.QosThresholdType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QosSustainabilityNotification {

    String subscriptionId;
    String notificationURI;

    String tai;
    String snssai;
    Integer ranUeThrou;

    Integer qosFlowRet;
    String relTimeUnit;
}
