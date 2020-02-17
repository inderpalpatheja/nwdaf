package com.nwdaf.Analytics.Model;

public class SubTableDataModel {

    public String suscriptionID;
    public String  correlationID;
    public String unsubCorrelationID;


    public SubTableDataModel() {
    }

    public SubTableDataModel(String suscriptionID, String correlationID, String unsubCorrelationID) {
        this.suscriptionID = suscriptionID;
        this.correlationID = correlationID;
        this.unsubCorrelationID = unsubCorrelationID;
    }

    public String getSuscriptionID() {
        return suscriptionID;
    }

    public void setSuscriptionID(String suscriptionID) {
        this.suscriptionID = suscriptionID;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public String getUnsubCorrelationID() {
        return unsubCorrelationID;
    }

    public void setUnsubCorrelationID(String unsubCorrelationID) {
        this.unsubCorrelationID = unsubCorrelationID;
    }
}
