package com.nwdaf.Analytics.Model.CustomData.UserDataCongestion;

import com.nwdaf.Analytics.Model.ThresholdLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CongestionInfo {

    CongestionType congType;
    TimeWindow timeIntev;
    ThresholdLevel nsi;
}
