package com.nwdaf.Analytics.Model.TableType.UEMobility;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class LocationID {

    private Integer ID;

    @NonNull
    private String supi;

    @NonNull
    private Integer locationID;

}
