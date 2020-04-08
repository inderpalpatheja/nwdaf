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

    public QosNotificationData(String subscriptionID, Integer threshold, QosType qosType)
    {
        setSubscriptionID(subscriptionID);

        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { setRanUeThroughputThreshold(threshold); }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { setQosFlowRetainThreshold(threshold); }
    }

    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

}

