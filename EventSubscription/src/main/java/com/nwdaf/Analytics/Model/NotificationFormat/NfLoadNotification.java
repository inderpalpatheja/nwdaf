package com.nwdaf.Analytics.Model.NotificationFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class NfLoadNotification {

    String subscriptionId;
    String notificationURI;

    Integer nfType;
    String nfInstanceId;

    Integer nfCpuUsage;
    Integer nfMemoryUsage;
    Integer nfStorageUsage;
    Integer nfLoadLevel;

}
