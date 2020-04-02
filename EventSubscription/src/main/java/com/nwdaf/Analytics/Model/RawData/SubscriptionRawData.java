package com.nwdaf.Analytics.Model.RawData;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class SubscriptionRawData {

    @Getter @Setter
    Object eventID;

    @Getter @Setter
    Object notificationURI;


    /**********************LOAD_LEVEL_INFORMATION*************************************************/

    @Getter @Setter
    Object snssais;

    @Getter @Setter
    Object notifMethod;

    @Getter @Setter
    Object repetitionPeriod;

    @Getter @Setter
    Object loadLevelThreshold;

    @Getter @Setter
    Object anySlice;




    /**********************QOS_SUSTAINABILITY*************************************************/


    @JsonProperty("5Qi")
    @Getter @Setter
    Object _5Qi;

    @Getter @Setter
    Object mcc;

    @Getter @Setter
    Object mnc;

    @Getter @Setter
    Object tac;

    @Getter @Setter
    Object ranUeThroughputThreshold;

    @Getter @Setter
    Object qosFlowRetainThreshold;




    /***************************UE_MOBILITY**********************************************/


    @Getter @Setter
    Object supi;



}
