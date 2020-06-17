package com.nwdaf.Analytics.Model.EventNotificationType;

import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.NetworkArea.NetworkAreaInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class NetworkPerfInfo {

    NetworkAreaInfo networkArea;
    NetworkPerfType nwPerfType;
    Integer relativeRatio;
    Integer absoluteNum;
    
}
