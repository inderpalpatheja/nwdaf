package com.nwdaf.Analytics.Model.CustomData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class TargetUeInformation {

    Boolean anyUe = Boolean.FALSE;
    String supi;
    String intGroupId;
}
