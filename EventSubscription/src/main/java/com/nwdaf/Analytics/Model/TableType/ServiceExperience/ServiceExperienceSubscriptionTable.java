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
    String snssais;
    String subscriptionID;
    String correlationID;
    Integer refCount;

    public ServiceExperienceSubscriptionTable(String supi, String snssais)
    {
        this.supi = supi;
        this.snssais = snssais;
    }

}
