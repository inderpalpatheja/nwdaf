package com.nwdaf.Analytics.Model.TableType.UeComm;


import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UeCommSubscriptionData {

    Integer ID;
    String subscriptionId;
    String supi;
    Integer maxAnaEntry;


    public UeCommSubscriptionData(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.maxAnaEntry = eventSubscription.getMaxAnaEntry();
    }
}
