package com.nwdaf.Analytics.Model.CustomData;

public class PlmnID {

    String mcc;
    String mnc;

    public PlmnID(String mcc, String mnc)
    {
        this.mcc = mcc;
        this.mnc = mnc;
    }

    @Override
    public String toString() {
        return mcc + "-" + mnc;
    }
}
