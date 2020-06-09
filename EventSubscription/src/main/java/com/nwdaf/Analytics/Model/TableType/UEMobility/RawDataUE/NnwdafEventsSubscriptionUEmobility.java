package com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class NnwdafEventsSubscriptionUEmobility {


    String subscriptionId;
    Integer event;
    String notificationURI;
    String supi;
    Integer notifMethod;
    Integer repetitionPeriod;
}