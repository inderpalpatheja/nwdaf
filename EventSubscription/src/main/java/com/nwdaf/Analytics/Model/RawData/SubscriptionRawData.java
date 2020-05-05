package com.nwdaf.Analytics.Model.RawData;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class SubscriptionRawData {

    Object eventID;
    Object notificationURI;


    /**********************LOAD_LEVEL_INFORMATION*************************************************/

    Object snssais;
    Object notifMethod;
    Object repetitionPeriod;
    Object loadLevelThreshold;
    Object anySlice;


    /**********************QOS_SUSTAINABILITY*************************************************/


    @JsonProperty("5Qi")
    Object _5Qi;

    Object mcc;
    Object mnc;
    Object tac;
    Object ranUeThroughputThreshold;
    Object qosFlowRetainThreshold;


    /***************************UE_MOBILITY**********************************************/

    Object supi;


    /***************************NETWORK_PERFORMANCE**********************************************/


    Object nwPerfType;
    Object relativeRatioThreshold;
    Object absoluteNumThreshold;

}
