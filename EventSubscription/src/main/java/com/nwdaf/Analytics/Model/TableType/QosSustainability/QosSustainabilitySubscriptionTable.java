package com.nwdaf.Analytics.Model.TableType.QosSustainability;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QosSustainabilitySubscriptionTable {

    Integer ID;
    String snssai;
    String tai;
    String subscriptionId;
    String correlationId;
    Integer refCount;

}
