package com.nwdaf.Analytics.Model;


public class NnwdafEventsSubscription {

    String subscriptionID;
    Integer eventID;
    String notificationURI;
    String snssais;
    Integer notifMethod;
    Integer repetitionPeriod;
    Integer loadLevelThreshold;
    Boolean anySlice;




    /*****************UE-mobility parameters*********************/
   String supi;

    public String getSupi() {
        return supi;
    }

    public void setSupi(String supi) {
        this.supi = supi;
    }

    /*****************QOS_SUSTAINABILITY parameters*********************/


    Integer _5Qi;
    String mcc;
    String mnc;
    String tac;
    String plmnID;

    Integer ranUeThroughputThreshold;
    Integer qosFlowRetainThreshold;


    public Integer get_5Qi() {
        return _5Qi;
    }

    public void set_5Qi(Integer _5Qi) {
        this._5Qi = _5Qi;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getTac() {
        return tac;
    }

    public void setTac(String tac) {
        this.tac = tac;
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


    public String getPlmnID() {
        return plmnID;
    }

    public void setPlmnID(String mcc, String mnc) {
        this.plmnID = mcc + "-" + mnc;
    }

    /*******************************************************************/




    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public String getNotificationURI() {
        return notificationURI;
    }

    public void setNotificationURI(String notificationURI) {
        this.notificationURI = notificationURI;
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public Integer getNotifMethod() {
        return notifMethod;
    }

    public void setNotifMethod(Integer notifMethod) {
        this.notifMethod = notifMethod;
    }

    public Integer getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(Integer repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    public Integer getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(Integer loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }

    public Boolean getAnySlice() {
        return anySlice;
    }

    public void setAnySlice(Boolean anySlice) {
        this.anySlice = anySlice;
    }

}