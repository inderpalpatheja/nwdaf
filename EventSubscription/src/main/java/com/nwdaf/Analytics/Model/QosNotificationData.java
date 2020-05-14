package com.nwdaf.Analytics.Model;

import com.nwdaf.Analytics.Model.CustomData.QosType;
import lombok.Getter;
import lombok.Setter;



@Getter @Setter
public class QosNotificationData {

    String subscriptionId;
    Integer ranUeThrouThrd;
    Integer qosFlowRetThrd;
    String tai;

    public QosNotificationData(String subscriptionId, String tai, Integer threshold, QosType qosType)
    {
        this.setSubscriptionId(subscriptionId);
        this.setTai(tai);

        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { this.setRanUeThrouThrd(threshold); }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { this.setQosFlowRetThrd(threshold); }
    }

}

