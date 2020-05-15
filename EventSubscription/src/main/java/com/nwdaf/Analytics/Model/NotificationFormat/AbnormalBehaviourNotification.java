package com.nwdaf.Analytics.Model.NotificationFormat;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.AdditionalData.AdditionalMeasurement;
import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.Exception;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class AbnormalBehaviourNotification {

    String subscriptionId;
    String notificationURI;

    List<String> supi;
    Exception exceps;
    Integer ratio;
    Integer confidence;
    AdditionalMeasurement addtMeasInfo;

    public AbnormalBehaviourNotification()
    {
        supi = new ArrayList<>();
        exceps = new Exception();
    }
}
