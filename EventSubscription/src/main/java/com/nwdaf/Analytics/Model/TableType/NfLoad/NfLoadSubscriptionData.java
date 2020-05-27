package com.nwdaf.Analytics.Model.TableType.NfLoad;

import com.nwdaf.Analytics.Model.EventSubscription;
import com.nwdaf.Analytics.Model.ThresholdLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NfLoadSubscriptionData {

    Integer ID;
    String subscriptionId;
    Integer nfType;
    String nfInstanceId;
    String supi;
    String snssai;

    Integer nfLoadLevelThrd;
    Integer nfCpuUsageThrd;
    Integer nfMemoryUsageThrd;
    Integer nfStorageUsageThrd;


    public NfLoadSubscriptionData(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;

        ThresholdLevel thresholds = eventSubscription.getNfLoadLvlThds().get(0);

        this.nfLoadLevelThrd = thresholds.getNfLoadLevel();
        this.nfCpuUsageThrd = thresholds.getNfCpuUsage();
        this.nfMemoryUsageThrd = thresholds.getNfMemoryUsage();
        this.nfStorageUsageThrd = thresholds.getNfStorageUsage();
    }

    public void subscribeThruNFType(Integer nfType, String nfInstanceId)
    {
        this.nfType = nfType;
        this.nfInstanceId = nfInstanceId;
    }

    public void subscribeThruSupi(String supi, String snssai)
    {
        this.supi = supi;
        this.snssai = snssai;
    }


}
