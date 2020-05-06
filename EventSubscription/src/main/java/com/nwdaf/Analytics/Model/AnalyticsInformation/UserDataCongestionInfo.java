package com.nwdaf.Analytics.Model.AnalyticsInformation;

import com.nwdaf.Analytics.Model.CustomData.UserDataCongestion.CongestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataCongestionInfo {

    String message;
    Boolean data_status;
    String supi;
    Integer congType;
    String tai;
    Integer congLevel;


    public Object getInfo()
    {
        HashMap<Object, Object> userDataCongInfo = new HashMap<>();

        userDataCongInfo.put("message ", message);
        userDataCongInfo.put("status", data_status);

        HashMap<Object, Object> entry = new HashMap<>();

        entry.put("supi", supi);
        entry.put("congType", CongestionType.values()[congType].toString());
        entry.put("congLevel", congLevel);

        HashMap<Object, Object> networkArea = new HashMap<>();
        HashMap<Object, Object> tai_info = new HashMap<>();
        HashMap<Object, Object> plmnID = new HashMap<>();

        String areaInfo[] = tai.split("-");

        plmnID.put("mcc", areaInfo[0]);
        plmnID.put("mnc", areaInfo[1]);

        tai_info.put("plmnId", plmnID);
        tai_info.put("tac", areaInfo[2]);
        networkArea.put("tai", tai_info);

        entry.put("networkArea", networkArea);

        userDataCongInfo.put("userDataCongInfo", entry);

        return userDataCongInfo;
    }

}
