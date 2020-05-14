package com.nwdaf.Analytics.Model.TableType.NetworkPerformance;


import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NetworkPerformanceInformation {

    Integer ID;
    String supi;
    Integer nwPerfType;
    Integer relativeRatio;
    Integer absoluteNum;

    public NetworkPerformanceInformation(EventSubscription eventSubscription)
    {
        this.supi = eventSubscription.getTgtUe().getSupi();
        this.nwPerfType = eventSubscription.getNwPerfRequs().get(0).getNwPerfType().ordinal();
    }

}
