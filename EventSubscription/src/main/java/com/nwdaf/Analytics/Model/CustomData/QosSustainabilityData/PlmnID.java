package com.nwdaf.Analytics.Model.CustomData.QosSustainabilityData;


public class PlmnID {

    String mcc;
    String mnc;

    public PlmnID(String mcc, String mnc)
    {
        this.mcc = mcc;
        this.mnc = mnc;
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

}
