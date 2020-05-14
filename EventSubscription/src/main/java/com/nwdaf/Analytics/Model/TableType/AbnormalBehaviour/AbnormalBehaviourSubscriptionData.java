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


    public AbnormalBehaviourSubscriptionData(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.excepId = eventSubscription.getExcepRequs().get(0).getExcepId().ordinal();
        this.excepLevelThrd = eventSubscription.getExcepRequs().get(0).getExcepLevel();
    }

}
