package com.nwdaf.Analytics.Model.TableType.UeComm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UeCommSubscriptionTable {

    Integer ID;
    String supi;
    String subscriptionId;
    String correlationId;
    Integer refCount;

}
