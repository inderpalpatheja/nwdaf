package com.nwdaf.Analytics.Model.AnalyticsInformation;

import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class NetworkPerformanceInfo {

    String message;
    Boolean data_status;
    String supi;
    String nwPerfType;
    Integer relativeRatio;
    Integer absoluteNum;
    NetworkPerfThreshold threshold_type;


    public Object getInfo()
    {
        HashMap<Object, Object> nwPerfInfo = new HashMap<>();

        nwPerfInfo.put("message", message);
        nwPerfInfo.put("status", data_status);

        HashMap<Object, Object> entry = new HashMap<>();
        entry.put("supi", supi);
        entry.put("nwPerfType", nwPerfType);

        switch(threshold_type)
        {
            case RELATIVE_RATIO: entry.put("relativeRatio", relativeRatio);
                                 break;

            case ABSOLUTE_NUM: entry.put("absoluteNum", absoluteNum);
                               break;
        }

        nwPerfInfo.put("nwPerfInfo", entry);

        return nwPerfInfo;
    }

}
