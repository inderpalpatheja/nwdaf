package com.nwdaf.Analytics.Model.CustomData.NetworkPerformance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NetworkPerfValue {

    static final Map<NetworkPerfType, NetworkPerfThreshold> nwPerfTypeThreshold;

    static
    {
        Map<NetworkPerfType, NetworkPerfThreshold> map = new HashMap<>();

        map.put(NetworkPerfType.GNB_ACTIVE_RATIO, NetworkPerfThreshold.RELATIVE_RATIO);
        map.put(NetworkPerfType.GNB_COMPUTING_USAGE, NetworkPerfThreshold.ABSOLUTE_NUM);
        map.put(NetworkPerfType.GNB_MEMORY_USAGE,NetworkPerfThreshold.ABSOLUTE_NUM);
        map.put(NetworkPerfType.GNB_DISK_USAGE, NetworkPerfThreshold.ABSOLUTE_NUM);
        map.put(NetworkPerfType.NUMBER_OF_UE, NetworkPerfThreshold.ABSOLUTE_NUM);
        map.put(NetworkPerfType.SESS_SUCC_RATIO, NetworkPerfThreshold.RELATIVE_RATIO);
        map.put(NetworkPerfType.HO_SUCC_RATIO, NetworkPerfThreshold.RELATIVE_RATIO);

        nwPerfTypeThreshold = Collections.unmodifiableMap(map);
    }

    public static NetworkPerfThreshold getThresholdType(NetworkPerfType nwPerfType)
    { return nwPerfTypeThreshold.get(nwPerfType); }
}
