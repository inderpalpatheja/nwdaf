package com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.AdditionalData;

import com.nwdaf.Analytics.Model.NetworkArea.NetworkAreaInfo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CircumstanceDescription {

    Float freq;
    String DateTime;
    NetworkAreaInfo locArea;        // ExceptionId: UNEXPECTED_RADIO_LINK_FAILURES or PING_PONG_ACROSS_CELLS
    Integer vol;                    // ExceptionId: TOO_FREQUENT_SERVICE_ACCESS or ABNORMAL_TRAFFIC_VOLUME

}
