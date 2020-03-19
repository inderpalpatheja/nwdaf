package com.nwdaf.Analytics.Model.RawData;


import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionRawData {

    Object eventID;
    Object notificationURI;
    Object snssais;
    Object notifMethod;
    Object repetitionPeriod;
    Object loadLevelThreshold;
    Object anySlice;





    /**********************Qos_Sustainability*************************************************/


    @JsonProperty("5Qi")
    Object _5Qi;

    Object mcc;
    Object mnc;
    Object tac;

    Object ranUeThroughputThreshold;
    Object qosFlowRetainThreshold;


    public Object get_5Qi() {
        return _5Qi;
    }

    public void set_5Qi(Object _5Qi) {
        this._5Qi = _5Qi;
    }

    public Object getMcc() {
        return mcc;
    }

    public void setMcc(Object mcc) {
        this.mcc = mcc;
    }

    public Object getMnc() {
        return mnc;
    }

    public void setMnc(Object mnc) {
        this.mnc = mnc;
    }

    public Object getTac() {
        return tac;
    }

    public void setTac(Object tac) {
        this.tac = tac;
    }

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


    /*******************************************************************************************/





    public Object getEventID() {
        return eventID;
    }

    public void setEventID(Object eventID) {
        this.eventID = eventID;
    }

    public Object getNotificationURI() {
        return notificationURI;
    }

    public void setNotificationURI(Object notificationURI) {
        this.notificationURI = notificationURI;
    }

    public Object getSnssais() {
        return snssais;
    }

    public void setSnssais(Object snssais) {
        this.snssais = snssais;
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

    public Object getAnySlice() {
        return anySlice;
    }

    public void setAnySlice(Object anySlice) {
        this.anySlice = anySlice;
    }

}
