package com.nwdaf.Analytics.Model.RawData;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class SubscriptionRawData {


    @Getter @Setter
    public class TargetUeInformation {

        Object anyUe;
        Object supi;
        Object intGroupId;
    }

    @Getter @Setter
    public class PlmnId {

        Object mcc;
        Object mnc;
    }


    @Getter @Setter
    public class Tai {

        PlmnId plmnId;
        Object tac;
    }





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

    Tai tai;
    Object ranUeThroughputThreshold;
    Object qosFlowRetainThreshold;


    /***************************UE_MOBILITY**********************************************/

    TargetUeInformation tgtUe;


    /***************************NETWORK_PERFORMANCE**********************************************/


    Object nwPerfType;
    Object relativeRatioThreshold;
    Object absoluteNumThreshold;


    /***************************USER_DATA_CONGESTION**********************************************/

    Object congType;
    Object congLevelThreshold;

}
