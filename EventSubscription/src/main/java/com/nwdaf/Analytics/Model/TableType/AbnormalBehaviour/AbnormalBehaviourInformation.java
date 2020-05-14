package com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour;

import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbnormalBehaviourInformation {

    Integer ID;
    String supi;
    Integer excepId;
    Integer excepLevel;


    public AbnormalBehaviourInformation(EventSubscription eventSubscription)
    {
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.excepId = eventSubscription.getExcepRequs().get(0).getExcepId().ordinal();
        this.excepLevel = eventSubscription.getExcepRequs().get(0).getExcepLevel();
    }
}
