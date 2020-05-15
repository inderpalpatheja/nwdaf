package com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.AdditionalData;

import com.nwdaf.Analytics.Model.NetworkArea.NetworkAreaInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter @Setter
public class AdditionalMeasurement {

    NetworkAreaInfo unexpLoc;                       // ExceptionId: UNEXPECTED_UE_LOCATION
    FlowDescription unexpFlowTep;                   // ExceptionId: UNEXPECTED_LONG_LIVE_FLOW or UNEXPECTED_LARGE_RATE_FLOW
    String unexpWake;                               // ExceptionId: UNEXPECTED_WAKEUP
    AddressList ddosAttack;                         // ExceptionId: SUSPICION_OF_DDOS_ATTACK
    AddressList wrgDest;                            // ExceptionId: WRONG_DESTINATION_ADDRESS
    List<CircumstanceDescription> circums;          // ExceptionId: TOO_FREQUENT_SERVICE_ACCESS or ABNORMAL_TRAFFIC_VOLUME or UNEXPECTED_RADIO_LINK_FAILURES or PING_PONG_ACROSS_CELLS
}
