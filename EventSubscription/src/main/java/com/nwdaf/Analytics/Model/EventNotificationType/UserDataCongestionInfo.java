package com.nwdaf.Analytics.Model.EventNotificationType;

import com.nwdaf.Analytics.Model.CustomData.UserDataCongestion.CongestionInfo;
import com.nwdaf.Analytics.Model.NetworkArea.NetworkAreaInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserDataCongestionInfo {

    NetworkAreaInfo networkArea;
    CongestionInfo congestionInfo;

}
