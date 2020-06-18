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


    Integer ID;
    String subscriptionId;
    Integer event;
    Integer notificationMethod;
    Integer repetitionPeriod;


    public SubscriptionTable(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this.event = eventSubscription.getEvent().ordinal();
        this.notificationMethod = eventSubscription.getNotificationMethod().ordinal();
        this.repetitionPeriod = eventSubscription.getRepetitionPeriod();
    }

}
