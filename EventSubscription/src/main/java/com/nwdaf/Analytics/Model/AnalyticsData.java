package com.nwdaf.Analytics.Model;

import com.nwdaf.Analytics.Model.EventNotificationType.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class AnalyticsData {

    List<SliceLoadLevelInformation> sliceLoadLevelInfos;
    List<NetworkPerfInfo> nwPerfs;
    List<NfLoadLevelInformation> nfLoadLevelInfo;
    List<QosSustainabilityInfo> qosSustainInfos;
    List<UeCommunication> ueComms;
    List<UserDataCongestionInfo> userDataCongInfos;
    List<ServiceExperienceInfo> svcExps;
    List<AbnormalBehaviour> abnorBehavrs;

}
