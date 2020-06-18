package com.nwdaf.Analytics.Model.EventNotificationType;

import com.nwdaf.Analytics.Model.CustomData.NfLoad.NFType;
import com.nwdaf.Analytics.Model.CustomData.NfLoad.NfStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class NfLoadLevelInformation {

    NFType nfType;
    String nfInstanceId;
    NfStatus nfStatus;
    Integer nfCpuUsage;
    Integer nfMemoryUsage;
    Integer nfStorageUsage;
    Integer nfLoadLevel;
    Integer nfLoadLevelPeak;
}
