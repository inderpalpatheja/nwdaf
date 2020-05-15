package com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE;

import lombok.Getter;
import lombok.Setter;



@Getter @Setter
public class UEmobilitySubscriptionTable {

    Integer Id;
    String supi;
    String subscriptionId;
    String correlationId;
    Integer refCount;
}
