package com.nwdaf.Analytics.Model.TableType.LoadLevelInformation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SliceLoadLevelSubscriptionTable {


    Integer ID;
    String snssai;
    String subscriptionId;
    String correlationId;
    Integer refCount;
}
