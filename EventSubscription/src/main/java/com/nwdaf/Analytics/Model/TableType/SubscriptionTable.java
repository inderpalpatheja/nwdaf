package com.nwdaf.Analytics.Model.TableType;

import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;
import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionTable {


    String subscriptionId;
    Integer event;
    String notificationURI;
    Integer notificationMethod;
    Integer repetitionPeriod;


    public SubscriptionTable(EventSubscription eventSubscription, String subscriptionId, String notificationURI)
    {
        this.subscriptionId = subscriptionId;
        this.event = eventSubscription.getEvent().ordinal();
        this.notificationURI = notificationURI;
        this.notificationMethod = eventSubscription.getNotificationMethod().ordinal();
        this.repetitionPeriod = (notificationMethod == NotificationMethod.PERIODIC.ordinal()) ? eventSubscription.getRepetitionPeriod() : 0;
    }

}
