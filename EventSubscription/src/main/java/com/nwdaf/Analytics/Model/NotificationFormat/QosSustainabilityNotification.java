package com.nwdaf.Analytics.Model.NotificationFormat;

import com.nwdaf.Analytics.Model.CustomData.QosType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QosSustainabilityNotification {

    String subscriptionID;
    String notificationURI;
    String plmnID;
    String snssais;
    String tac;
    Integer threshold;
    QosType thresholdType;

}
