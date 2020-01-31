package com.nwdaf.Analytics.model;

public class SubTableDataModel {

    public int correlationId;
    public int unsubCorrelationId;


    public SubTableDataModel() {
    }

    public int getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(int correlationId) {
        this.correlationId = correlationId;
    }

    public int getUnsubCorrelationId() {
        return unsubCorrelationId;
    }

    public void setUnsubCorrelationId(int unsubCorrelationId) {
        this.unsubCorrelationId = unsubCorrelationId;
    }

    public SubTableDataModel(int correlationId, int unsubCorrelationId) {
        this.correlationId = correlationId;
        this.unsubCorrelationId = unsubCorrelationId;
    }
}
