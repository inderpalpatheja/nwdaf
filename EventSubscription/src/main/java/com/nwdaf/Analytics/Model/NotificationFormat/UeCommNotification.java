package com.nwdaf.Analytics.Model.NotificationFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UeCommNotification {

    String subscriptionId;
    String notificationURI;

    Integer commDur;
    String ts;
    Integer ulVol;
    Integer dlVol;
}
