package com.nwdaf.Analytics.Model.TableType.QosSustainability;

import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QosSustainabilitySubscriptionData {

    Integer ID;
    String subscriptionId;
    Integer _5Qi;
    String tai;
    String snssai;
    Integer ranUeThrouThrd;
    Integer qosFlowRetThrd;
    String relTimeUnit;



    public QosSustainabilitySubscriptionData(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this._5Qi = eventSubscription.getQosRequ().get_5Qi();
        this.tai = eventSubscription.getNetworkArea().getTais().get(0).toString();
        this.snssai = eventSubscription.getSnssais().get(0).toString();


        if(eventSubscription.getRanUeThrouThrds() != null && !eventSubscription.getRanUeThrouThrds().isEmpty())
        { this.ranUeThrouThrd = eventSubscription.getRanUeThrouThrds().get(0); }

        if(eventSubscription.getQosFlowRetThrds() != null && !eventSubscription.getQosFlowRetThrds().isEmpty())
        {
            this.qosFlowRetThrd = eventSubscription.getQosFlowRetThrds().get(0).getRelFlowNum();
            this.relTimeUnit = eventSubscription.getQosFlowRetThrds().get(0).getRelTimeUnit().toString();
        }
    }

}
