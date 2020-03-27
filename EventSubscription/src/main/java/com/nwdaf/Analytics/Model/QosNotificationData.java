package com.nwdaf.Analytics.Model;

import com.nwdaf.Analytics.Model.CustomData.QosType;

import java.util.UUID;

public class QosNotificationData {

    UUID subscriptionID;
    Integer ranUeThroughputThreshold;
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

    public Integer getRanUeThroughputThreshold() {
        return ranUeThroughputThreshold;
    }

    public void setRanUeThroughputThreshold(Integer ranUeThroughputThreshold) {
        this.ranUeThroughputThreshold = ranUeThroughputThreshold;
    }

    public Integer getQosFlowRetainThreshold() {
        return qosFlowRetainThreshold;
    }

    public void setQosFlowRetainThreshold(Integer qosFlowRetainThreshold) {
        this.qosFlowRetainThreshold = qosFlowRetainThreshold;
    }
}

