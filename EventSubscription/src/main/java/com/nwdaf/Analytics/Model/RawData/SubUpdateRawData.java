package com.nwdaf.Analytics.Model.RawData;

import lombok.Getter;
import lombok.Setter;

public class SubUpdateRawData {

    /******************LOAD_LEVEL_INFORMATION************************/

    @Getter @Setter
    Object notifMethod;

    @Getter @Setter
    Object repetitionPeriod;

    @Getter @Setter
    Object loadLevelThreshold;


    /******************QOS_SUSTAINABILITY************************/

    @Getter @Setter
    Object ranUeThroughputThreshold;

    @Getter @Setter
    Object qosFlowRetainThreshold;


}
