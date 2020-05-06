package com.nwdaf.Analytics.Model.TableType.UserDataCongestion;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataCongestionSubscriptionData {

    Integer ID;
    String subscriptionID;
    String supi;
    Integer congType;
    Integer congLevelThreshold;

}
