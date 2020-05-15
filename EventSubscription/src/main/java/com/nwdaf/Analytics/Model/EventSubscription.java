package com.nwdaf.Analytics.Model;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.Exception;
import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.ExpectedAnalyticsType;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfRequirement;
import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;
import com.nwdaf.Analytics.Model.CustomData.QosSustainability.QosRequirement;
import com.nwdaf.Analytics.Model.CustomData.QosSustainability.RetainabilityThreshold;
import com.nwdaf.Analytics.Model.CustomData.TargetUeInformation;
import com.nwdaf.Analytics.Model.CustomData.UserDataCongestion.CongestionType;
import com.nwdaf.Analytics.Model.NetworkArea.NetworkAreaInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class EventSubscription {


    Boolean anySlice;
    NwdafEvent event;
    NotificationMethod notificationMethod;
    Integer loadLevelThreshold;
    NetworkAreaInfo networkArea;
    QosRequirement qosRequ;
    Integer repetitionPeriod;
    TargetUeInformation tgtUe;
    List<Integer> qosFlowRetThrds;
    List<Integer> ranUeThrouThrds;
    List<NetworkPerfRequirement> nwPerfRequs;
    List<ThresholdLevel> congThresholds;
    List<Exception> excepRequs;
    ExpectedAnalyticsType exptAnaType;
    List<Snssai> snssais;
    CongestionType congType;

}
