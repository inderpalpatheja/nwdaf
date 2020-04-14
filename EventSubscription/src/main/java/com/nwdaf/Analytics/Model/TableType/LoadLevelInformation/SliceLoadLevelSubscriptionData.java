package com.nwdaf.Analytics.Model.TableType.LoadLevelInformation;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class SliceLoadLevelSubscriptionData {

    @Getter @Setter
    private Integer ID;

    private UUID subscriptionID;

    @Getter @Setter
    private String snssais;

    @Getter @Setter
    private Integer loadLevelThreshold;


    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

}
