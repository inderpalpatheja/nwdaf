package com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbnormalBehaviourSubscriptionTable {

    Integer ID;
    String supi;
    Integer excepId;
    String subscriptionId;
    String correlationId;
    Integer refCount;
}
