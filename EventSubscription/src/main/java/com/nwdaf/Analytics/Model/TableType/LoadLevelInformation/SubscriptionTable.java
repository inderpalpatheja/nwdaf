package com.nwdaf.Analytics.Model.TableType.LoadLevelInformation;

import java.util.UUID;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;
import lombok.Getter;
import lombok.Setter;


public class SubscriptionTable {


    private UUID subscriptionID;

    private EventID eventID;

    @Getter @Setter
    private String notificationURI;

    private NotificationMethod notifMethod;

    @Getter @Setter
    private Integer repetitionPeriod;


    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

    public Integer getEventID() {
        return eventID != null ? eventID.ordinal() : null;
    }

    public void setEventID(Integer ID) {
        this.eventID = (ID != null) ? EventID.values()[ID] : null;
    }

    public Integer getNotifMethod() {
        return notifMethod != null ? notifMethod.ordinal() : null;
    }

    public void setNotifMethod(Integer method_no) {
        this.notifMethod = (method_no != null) ? notifMethod.values()[method_no] : null;
    }

}
