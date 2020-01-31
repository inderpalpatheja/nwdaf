package com.nwdaf.Analytics.model.Nsmf_EventExposure;

public class Nsmf_EventExposure_UnSubscribe {

    private String subscriptionCorrelationID;

    public Nsmf_EventExposure_UnSubscribe() {
    }

    public String getSubscriptionCorrelationID() {
        return subscriptionCorrelationID;
    }

    public void setSubscriptionCorrelationID(String subscriptionCorrelationID) {
        this.subscriptionCorrelationID = subscriptionCorrelationID;
    }

    public Nsmf_EventExposure_UnSubscribe(String subscriptionCorrelationID) {
        this.subscriptionCorrelationID = subscriptionCorrelationID;
    }
}
