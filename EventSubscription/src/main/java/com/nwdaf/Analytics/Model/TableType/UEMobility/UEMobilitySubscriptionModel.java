package com.nwdaf.Analytics.Model.TableType.UEMobility;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class UEMobilitySubscriptionModel {


    String subscriptionID;
    Integer eventID;
    String notificationURI;
    Integer notifMethod;
    Integer repetitionPeriod;
    String supi;


    @Override
    public String toString() {
        return "UE_MobilitySubscriptionModel{" +
                "subscriptionID='" + subscriptionID + '\'' +
                ", eventID=" + eventID +
                ", notificationURI='" + notificationURI + '\'' +
                ", notifMethod=" + notifMethod +
                ", repetitionPeriod=" + repetitionPeriod +
                ", supi='" + supi + '\'' +
                '}';
    }
}
