package com.nwdaf.Analytics.Model.CustomData.QosSustainability;

import com.nwdaf.Analytics.Model.TimeUnit;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RetainabilityThreshold {

    Integer relFlowNum;
    TimeUnit relTimeUnit;
    Integer relFlowRatio;
}
