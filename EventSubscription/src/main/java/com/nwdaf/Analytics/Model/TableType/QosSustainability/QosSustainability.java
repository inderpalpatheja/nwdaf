package com.nwdaf.Analytics.Model.TableType.QosSustainability;

import com.nwdaf.Analytics.Model.CustomData.QosSustainabilityData.PlmnID;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class QosSustainability {

    @Getter @Setter
    private Integer ID;

    private UUID subscriptionID;

    @Getter @Setter
    private Integer _5Qi;

    private PlmnID plmnID;

    @Getter @Setter
    private String tac;



    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

    public String getPlmnID() {
        return plmnID.toString();
    }

    public void setPlmnID(String plmnID) {

        String param[] = plmnID.split("-");
        this.plmnID = new PlmnID(param[0], param[1]);
    }

}
