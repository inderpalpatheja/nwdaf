package com.nwdaf.Analytics.Model.RawData;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SubUpdateRawData {

    /******************LOAD_LEVEL_INFORMATION************************/


    Object notifMethod;
    Object repetitionPeriod;
    Object loadLevelThreshold;


    /******************QOS_SUSTAINABILITY************************/

    Object ranUeThroughputThreshold;
    Object qosFlowRetainThreshold;
}
