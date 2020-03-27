package com.nwdaf.Analytics.Model.RawData;

public class SubUpdateRawData {

    Object eventID;
    Object notifMethod;
    Object repetitionPeriod;
    Object loadLevelThreshold;


    /******************QOS_SUSTAINABILITY************************/

    Object ranUeThroughputThreshold;
    Object qosFlowRetainThreshold;


    public Object getRanUeThroughputThreshold() {
        return ranUeThroughputThreshold;
    }

    public void setRanUeThroughputThreshold(Object ranUeThroughputThreshold) {
        this.ranUeThroughputThreshold = ranUeThroughputThreshold;
    }

    public Object getQosFlowRetainThreshold() {
        return qosFlowRetainThreshold;
    }

    public void setQosFlowRetainThreshold(Object qosFlowRetainThreshold) {
        this.qosFlowRetainThreshold = qosFlowRetainThreshold;
    }

    /***********************************************************/


    public Object getEventID() {
        return eventID;
    }

    public void setEventID(Object eventID) {
        this.eventID = eventID;
    }

    public Object getNotifMethod() {
        return notifMethod;
    }

    public void setNotifMethod(Object notifMethod) {
        this.notifMethod = notifMethod;
    }

    public Object getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(Object repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    public Object getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(Object loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }
}
