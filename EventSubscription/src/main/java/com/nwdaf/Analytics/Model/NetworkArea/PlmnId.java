package com.nwdaf.Analytics.Model.NetworkArea;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PlmnId {

    String mcc;
    String mnc;


    @Override
    public String toString() {
        return mcc + "-" + mnc;
    }
}