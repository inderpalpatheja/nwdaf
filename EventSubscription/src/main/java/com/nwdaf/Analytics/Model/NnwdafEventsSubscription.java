package com.nwdaf.Analytics.Model;


import lombok.Getter;
import lombok.Setter;

public class NnwdafEventsSubscription {

    @Getter @Setter
    String subscriptionID;

    @Getter @Setter
    Integer eventID;

    @Getter @Setter
    String notificationURI;

    @Getter @Setter
    String snssais;

    @Getter @Setter
    Integer notifMethod;

    @Getter @Setter
    Integer repetitionPeriod;

    @Getter @Setter
    Integer loadLevelThreshold;

    @Getter @Setter
    Boolean anySlice;


    /*****************QOS_SUSTAINABILITY parameters*********************/


    @Getter @Setter
    Integer _5Qi;

    @Getter @Setter
    String mcc;

    @Getter @Setter
    String mnc;

    @Getter @Setter
    String tac;

    @Getter
    String plmnID;

    @Getter @Setter
    Integer ranUeThroughputThreshold;

    @Getter @Setter
    Integer qosFlowRetainThreshold;


    public void setPlmnID(String mcc, String mnc) {
        this.plmnID = mcc + "-" + mnc;
    }



    /*****************UE_MOBILITY parameters****************************/


    @Getter @Setter
    String supi;

}