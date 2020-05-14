package com.nwdaf.Analytics.Model.TableType.ServiceExperience;

import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceExperienceInformation {

    Integer ID;
    String supi;
    String snssai;
    Float mos;
    Float upperRange;
    Float lowerRange;

    public ServiceExperienceInformation(EventSubscription eventSubscription)
    {
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.snssai = eventSubscription.getSnssais().get(0).toString();
    }

}
