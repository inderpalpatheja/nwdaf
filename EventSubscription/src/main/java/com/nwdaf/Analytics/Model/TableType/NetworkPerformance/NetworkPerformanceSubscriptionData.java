package com.nwdaf.Analytics.Model.TableType.NetworkPerformance;


import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class NetworkPerformanceSubscriptionData {

    Integer ID;
    String subscriptionID;

    @NonNull
    String supi;

    @NonNull
    Integer nwPerfType;

    Integer relativeRatioThreshold;
    Integer absoluteNumThreshold;
}
