package com.nwdaf.Analytics.Model.TableType.UserDataCongestion;


import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataCongestionSubscriptionData {

    Integer ID;
    String subscriptionId;
    String supi;
    Integer congType;
    Integer congThreshold;


    public UserDataCongestionSubscriptionData(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.congType = eventSubscription.getCongType().ordinal();
        this.congThreshold = eventSubscription.getCongThresholds().get(0).getCongLevel();
    }
}
