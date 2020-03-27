package com.nwdaf.Analytics.Model.TableType.QosSustainability;

import com.nwdaf.Analytics.Model.CustomData.QosSustainabilityData.PlmnID;

import java.util.UUID;

public class QosSustainability {

    private Integer ID;
    private UUID subscriptionID;
    private Integer _5Qi;
    private PlmnID plmnID;
    private String tac;


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

    public Integer get_5Qi() {
        return _5Qi;
    }

    public void set_5Qi(Integer _5Qi) {
        this._5Qi = _5Qi;
    }

    public String getPlmnID() {
        return plmnID.toString();
    }

    public void setPlmnID(String plmnID) {

        String param[] = plmnID.split("-");
        this.plmnID = new PlmnID(param[0], param[1]);
    }

    public String getTac() {
        return tac;
    }

    public void setTac(String tac) {
        this.tac = tac;
    }
}
