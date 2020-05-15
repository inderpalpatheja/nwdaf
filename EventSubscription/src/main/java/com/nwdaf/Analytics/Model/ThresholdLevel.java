package com.nwdaf.Analytics.Model;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ThresholdLevel {

    Integer congLevel;
    Integer nfLoadLevel;
    Integer nfCpuUsage;
    Integer nfMemoryUsage;
    Integer nfStorageUsage;
}
