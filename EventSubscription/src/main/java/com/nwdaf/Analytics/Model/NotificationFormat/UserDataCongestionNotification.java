package com.nwdaf.Analytics.Model.NotificationFormat;

import com.nwdaf.Analytics.Model.CustomData.Tai;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataCongestionNotification {

    String notificationURI;
    String subscriptionID;
    Tai tai;
    Integer congType;
    Integer congLevel;

}
