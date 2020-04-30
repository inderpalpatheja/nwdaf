package com.nwdaf.Analytics.Model.TableType.NetworkPerformance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NetworkPerformanceSubscriptionTable {

    Integer ID;
    String supi;
    Integer nwPerfType;
    String subscriptionID;
    String correlationID;
    Integer refCount;
}
