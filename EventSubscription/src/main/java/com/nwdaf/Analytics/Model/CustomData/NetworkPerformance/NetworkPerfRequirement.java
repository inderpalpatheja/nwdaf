package com.nwdaf.Analytics.Model.CustomData.NetworkPerformance;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NetworkPerfRequirement {

    NetworkPerfType nwPerfType;
    Integer relativeRatio;
    Integer absoluteNum;
}
