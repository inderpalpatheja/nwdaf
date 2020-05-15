package com.nwdaf.Analytics.Model.TableType.UEMobility;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class UEMobilitySubscriptionTable {


    private Integer ID;
    private String supi;
    private String subscriptionId;
    private String correlationId;
    private Integer refCount;

}
