package com.nwdaf.Analytics.Model.TableType.ServiceExperience;

import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ServiceExperienceSubscriptionData {

    Integer ID;
    String subscriptionId;

    @NonNull
    String supi;

    @NonNull
    String snssai;


    public ServiceExperienceSubscriptionData(EventSubscription eventSubscription, String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.snssai = eventSubscription.getSnssais().get(0).toString();
    }
}
