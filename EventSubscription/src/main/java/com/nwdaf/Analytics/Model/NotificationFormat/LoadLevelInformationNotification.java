package com.nwdaf.Analytics.Model.NotificationFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoadLevelInformationNotification {

    String subscriptionId;
    String notificationURI;
    String snssai;
    Integer currentLoadLevel;

}
