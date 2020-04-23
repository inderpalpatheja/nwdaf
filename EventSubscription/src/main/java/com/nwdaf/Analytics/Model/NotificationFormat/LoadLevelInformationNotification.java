package com.nwdaf.Analytics.Model.NotificationFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoadLevelInformationNotification {

    String subscriptionID;
    String notificationURI;
    String snssais;
    Integer currentLoadLevel;

}
