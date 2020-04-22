package com.nwdaf.Analytics.Model.TableType.ServiceExperience;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ServiceExperienceSubscriptionData {

    Integer ID;
    String subscriptionID;

    @NonNull
    String supi;

    @NonNull
    String snssais;
}
