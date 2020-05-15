package com.nwdaf.Analytics.Model.TableType.ServiceExperience;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceExperienceSubscriptionTable {

    Integer ID;
    String supi;
    String snssai;
    String subscriptionId;
    String correlationId;
    Integer refCount;

    public ServiceExperienceSubscriptionTable(String supi, String snssai)
    {
        this.supi = supi;
        this.snssai = snssai;
    }

}
