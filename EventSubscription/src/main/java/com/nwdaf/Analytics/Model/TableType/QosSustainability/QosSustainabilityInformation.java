package com.nwdaf.Analytics.Model.TableType.QosSustainability;

import com.nwdaf.Analytics.Model.EventSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QosSustainabilityInformation {


    Integer ID;
    String snssai;
    String tai;
    Integer ranUeThrou;
    Integer qosFlowRet;


    public QosSustainabilityInformation(EventSubscription eventSubscription)
    {
        this.snssai = eventSubscription.getSnssais().get(0).toString();
        this.tai = eventSubscription.getNetworkArea().getTais().get(0).toString();
    }

}
