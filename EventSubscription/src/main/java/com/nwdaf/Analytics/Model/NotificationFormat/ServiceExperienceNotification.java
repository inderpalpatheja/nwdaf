package com.nwdaf.Analytics.Model.NotificationFormat;

import com.nwdaf.Analytics.Model.CustomData.ServiceExperience.SvcExperience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceExperienceNotification {

    String notificationURI;
    String subscriptionID;
    SvcExperience svcExpInfo;
    String supi;
    String snssais;

}
