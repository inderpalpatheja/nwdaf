package com.nwdaf.Analytics.Model.CustomData.NfLoad;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class NfStatus {

    Integer statusRegistered;
    Integer statusUnregistered;
    Integer statusUndiscoverable;
}
