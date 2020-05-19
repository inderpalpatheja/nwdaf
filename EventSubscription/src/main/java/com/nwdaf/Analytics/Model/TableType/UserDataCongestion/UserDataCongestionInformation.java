package com.nwdaf.Analytics.Model.TableType.UserDataCongestion;

import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataCongestionInformation {

    Integer ID;
    String supi;
    Integer congType;
    String tai;
    Integer congLevel;


    public UserDataCongestionInformation(EventSubscription eventSubscription, String tai)
    {
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.congType = eventSubscription.getCongType().ordinal();
        this.tai = tai;
    }

}
