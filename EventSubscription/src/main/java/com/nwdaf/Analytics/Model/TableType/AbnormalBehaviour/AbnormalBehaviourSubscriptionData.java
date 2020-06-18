package com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour;

import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbnormalBehaviourSubscriptionData {

    Integer ID;
    String subscriptionId;
    String supi;
    Integer excepId;
    Integer excepLevelThrd;


    //When excepRequs is passed
    public AbnormalBehaviourSubscriptionData(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.excepId = eventSubscription.getExcepRequs().get(0).getExcepId().ordinal();
        this.excepLevelThrd = eventSubscription.getExcepRequs().get(0).getExcepLevel();
    }


    // When ExpectedAnalyticsType is passed
    public AbnormalBehaviourSubscriptionData(String supi, Integer excepId, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this.supi = supi;
        this.excepId = excepId;
        this.excepLevelThrd = 0;
    }

}
