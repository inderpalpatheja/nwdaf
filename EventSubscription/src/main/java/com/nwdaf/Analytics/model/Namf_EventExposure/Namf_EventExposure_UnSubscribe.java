package com.nwdaf.Analytics.model.Namf_EventExposure;

public class Namf_EventExposure_UnSubscribe {

    private int subscriptionCorrelationId;

    public Namf_EventExposure_UnSubscribe(int subscriptionCorrelationId) {
        this.subscriptionCorrelationId = subscriptionCorrelationId;
    }

    public int getSubscriptionCorrelationId() {
        return subscriptionCorrelationId;
    }

    public void setSubscriptionCorrelationId(int subscriptionCorrelationId) {
        this.subscriptionCorrelationId = subscriptionCorrelationId;
    }
}
