package com.nwdaf.Analytics.Model;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.ExceptionId;
import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.ExpectedAnalyticsType;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.CustomData.NfLoad.NFType;
import com.nwdaf.Analytics.Model.CustomData.QosSustainability.QosRequirement;
import com.nwdaf.Analytics.Model.NetworkArea.NetworkAreaInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class EventFilter {

    Boolean anySlice = Boolean.FALSE;
    List<Snssai> snssais;
    List<String> nfInstanceIds;
    List<NFType> nfTypes;
    NetworkAreaInfo networkArea;
    List<NetworkPerfType> nwPerfTypes;
    Integer maxAnaEntry;
    QosRequirement qosRequ;
    List<ExceptionId> excepIds;
    ExpectedAnalyticsType exptAnaType;

}
