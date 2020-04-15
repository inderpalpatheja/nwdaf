package com.nwdaf.Analytics.Model;

import com.nwdaf.Analytics.Model.CustomData.QosType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class QosNotificationData {

    UUID subscriptionID;

    @Getter @Setter
    Integer ranUeThroughputThreshold;

    @Getter @Setter
    Integer qosFlowRetainThreshold;

    @Getter @Setter
    String tac;

    public QosNotificationData(String subscriptionID, String tac, Integer threshold, QosType qosType)
    {
        this.setSubscriptionID(subscriptionID);
        this.setTac(tac);

        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { this.setRanUeThroughputThreshold(threshold); }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { this.setQosFlowRetainThreshold(threshold); }
    }

    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

}

