package com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.AdditionalData;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class EthFlowDescription {

    String destMacAddr;
    String ethType;
    String fDesc; // 2 values IN or OUT
    FlowDirection fDir; // UPLINK or DOWNLINK
    String sourceMacAddr;
    List<String> vlanTags;
    String srcMacAddrEnd;
    String destMacAddrEnd;

}
