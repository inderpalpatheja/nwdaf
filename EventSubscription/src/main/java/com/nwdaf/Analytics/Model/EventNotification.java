package com.nwdaf.Analytics.Model;

import com.nwdaf.Analytics.Model.EventNotificationType.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class EventNotification {

    NwdafEvent event;
    List<NetworkPerfInfo> nwPerfs;
    List<NfLoadLevelInformation> nfLoadLevelInfo;
    List<QosSustainabilityInfo> qosSustainInfos;
    SliceLoadLevelInformation sliceLoadLevelInfo;
    List<ServiceExperienceInfo> svcExps;
    List<UeCommunication> ueComms;
    List<AbnormalBehaviour> abnorBehavrs;
    List<UserDataCongestionInfo> userDataCongInfos;

}
