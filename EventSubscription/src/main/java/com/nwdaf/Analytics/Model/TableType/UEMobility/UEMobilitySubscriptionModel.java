package com.nwdaf.Analytics.Model.TableType.UEMobility;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class UEMobilitySubscriptionModel {


    String subscriptionId;
    Integer event;
    String notificationURI;
    Integer notifMethod;
    Integer repetitionPeriod;
    String supi;


    @Override
    public String toString() {
        return "UE_MobilitySubscriptionModel{" +
                "subscriptionID='" + subscriptionId + '\'' +
                ", eventID=" + event +
                ", notificationURI='" + notificationURI + '\'' +
                ", notifMethod=" + notifMethod +
                ", repetitionPeriod=" + repetitionPeriod +
                ", supi='" + supi + '\'' +
                '}';
    }
}
