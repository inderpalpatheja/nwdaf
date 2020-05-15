package com.nwdaf.Analytics.Model.TableType.UserDataCongestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDataCongestionSubscriptionTable {

    Integer ID;
    String supi;
    Integer congType;
    String tai;
    String subscriptionId;
    String correlationId;
    Integer refCount;
}
