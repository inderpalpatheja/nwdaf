package com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NnwdafEventsSubscriptionUEmobility {


    String subscriptionID;
    Integer eventID;
    String notificationURI;
    String supi;
    Integer notifMethod;
    Integer repetitionPeriod;
}