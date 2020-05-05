package com.nwdaf.Analytics.Model.TableType.NetworkPerformance;


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
}
