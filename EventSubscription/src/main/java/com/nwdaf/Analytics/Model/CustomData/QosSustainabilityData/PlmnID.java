package com.nwdaf.Analytics.Model.CustomData.QosSustainabilityData;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter @Setter
public class PlmnID {

    String mcc;
    String mnc;


    @Override
    public String toString() {
        return mcc + "-" + mnc;
    }
}
