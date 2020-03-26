package com.nwdaf.Analytics.Model.CustomData.QosSustainabilityData;

import com.nwdaf.Analytics.Model.CustomData.QosType;


public class QosSustainabilityInfo {


    NetworkAreaInfo areaInfo;
    Integer crossedQosFlowRetainThreshold;
    Integer crossedRanUeThroughputThreshold;

    public QosSustainabilityInfo(String plmnID, String tac, Integer threshold, QosType qosType)
    {
        this.areaInfo = new NetworkAreaInfo();

        String networkInfo[] = plmnID.split("-");

        this.areaInfo.setPlmnID(new PlmnID(networkInfo[0], networkInfo[1]));
        this.areaInfo.setTac(tac);

        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { this.crossedRanUeThroughputThreshold = threshold; }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { this.crossedQosFlowRetainThreshold = threshold; }
    }


    public NetworkAreaInfo getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(NetworkAreaInfo areaInfo) {
        this.areaInfo = areaInfo;
    }

    public Integer getCrossedQosFlowRetainThreshold() {
        return crossedQosFlowRetainThreshold;
    }

    public void setCrossedQosFlowRetainThreshold(Integer crossedQosFlowRetainThreshold) {
        this.crossedQosFlowRetainThreshold = crossedQosFlowRetainThreshold;
    }

    public Integer getCrossedRanUeThroughputThreshold() {
        return crossedRanUeThroughputThreshold;
    }

    public void setCrossedRanUeThroughputThreshold(Integer crossedRanUeThroughputThreshold) {
        this.crossedRanUeThroughputThreshold = crossedRanUeThroughputThreshold;
    }

}
