package com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE;

import lombok.Getter;
import lombok.Setter;



@Getter @Setter
public class UEmobilitySubscriptionTable {

    Integer ID;
    String supi;
    String subscriptionId;
    String correlationId;
    Integer refCount;
}
