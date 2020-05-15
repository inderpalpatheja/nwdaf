package com.nwdaf.Analytics.Model.TableType.NetworkPerformance;


import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfValue;
import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class NetworkPerformanceSubscriptionData {

    Integer ID;
    String subscriptionId;

    @NonNull
    String supi;

    @NonNull
    Integer nwPerfType;

    Integer relativeRatioThrd;
    Integer absoluteNumThrd;


    public NetworkPerformanceSubscriptionData(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.nwPerfType = eventSubscription.getNwPerfRequs().get(0).getNwPerfType().ordinal();

        NetworkPerfType networkType = NetworkPerfType.values()[nwPerfType];

        if(NetworkPerfValue.getThresholdType(networkType) == NetworkPerfThreshold.ABSOLUTE_NUM)
        { this.absoluteNumThrd = eventSubscription.getNwPerfRequs().get(0).getAbsoluteNum(); }

        else
        { this.relativeRatioThrd = eventSubscription.getNwPerfRequs().get(0).getRelativeRatio(); }
    }

}
