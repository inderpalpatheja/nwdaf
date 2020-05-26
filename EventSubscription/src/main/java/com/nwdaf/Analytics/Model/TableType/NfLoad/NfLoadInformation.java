package com.nwdaf.Analytics.Model.TableType.NfLoad;

import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NfLoadInformation {

    Integer ID;
    Integer nfType;
    String nfInstanceId;

    Integer nfLoadLevel;
    Integer nfCpuUsage;
    Integer nfMemoryUsage;
    Integer nfStorageUsage;


    public NfLoadInformation(Integer nfType, String nfInstanceId)
    {
        this.nfType = nfType;
        this.nfInstanceId = nfInstanceId;
    }

}
