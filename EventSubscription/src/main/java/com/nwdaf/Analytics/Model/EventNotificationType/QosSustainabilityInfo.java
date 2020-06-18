package com.nwdaf.Analytics.Model.EventNotificationType;

import com.nwdaf.Analytics.Model.CustomData.QosSustainability.RetainabilityThreshold;
import com.nwdaf.Analytics.Model.NetworkArea.NetworkAreaInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class QosSustainabilityInfo {

    NetworkAreaInfo areaInfo;
    RetainabilityThreshold qosFlowRetThd;
    Integer ranUeThrouThd;

}
