package com.nwdaf.Analytics.Model;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NnwdafEventsSubscription {


    String subscriptionID;
    Integer eventID;
    String notificationURI;
    String snssais;
    Integer notifMethod;
    Integer repetitionPeriod;
    Integer loadLevelThreshold;
    Boolean anySlice;


    /*****************QOS_SUSTAINABILITY parameters*********************/



    Integer _5Qi;
    String mcc;
    String mnc;
    String tac;
    String plmnID;
    Integer ranUeThroughputThreshold;
    Integer qosFlowRetainThreshold;


    public void setPlmnID(String mcc, String mnc) {
        this.plmnID = mcc + "-" + mnc;
    }



    /*****************UE_MOBILITY parameters****************************/



    String supi;


    /*****************SERVICE_EXPERIENCE parameters****************************/


    Boolean anyUE;


    /*****************SERVICE_EXPERIENCE parameters****************************/


    Integer nwPerfType;
    Integer relativeRatioThreshold;
    Integer absoluteNumThreshold;



    /*****************USER_DATA_CONGESTION parameters****************************/


    Integer congType;
    Integer congLevelThreshold;
    String tai;

    public void setTai(String plmnID, String tac) {
        this.tai = plmnID + "-" + tac;
    }
}