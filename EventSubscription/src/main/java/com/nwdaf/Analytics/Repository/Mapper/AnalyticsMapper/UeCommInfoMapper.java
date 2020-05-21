package com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.AdditionalData.FlowDirection;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UeCommInfoMapper implements RowMapper {

    String flowDir[] = {"IN", "OUT"};

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer commDur = resultSet.getInt("commDur");
        String ts = resultSet.getString("ts");
        Integer ulVol = resultSet.getInt("ulVol");
        Integer dlVol = resultSet.getInt(("dlVol"));

        HashMap<Object, Object> ueComm = new HashMap<>();

        ueComm.put("commDur", commDur);
        ueComm.put("ts", ts);

        List<Object> fDescs = new ArrayList<>();

        for(int flowId = 0; flowId < FlowDirection.values().length; flowId++)
        {
            HashMap<Object, Object> fDescs_entry = new HashMap<>();

            HashMap<Object, Object> ipTrafficFilter = new HashMap<>();

            HashMap<Object, Object> flowDescriptions = new HashMap<>();
            flowDescriptions.put("direction", flowDir[flowId]);

            ipTrafficFilter.put("flowId", FlowDirection.values()[flowId].toString());
            ipTrafficFilter.put("flowDescriptions", flowDescriptions);

            fDescs_entry.put("ipTrafficFilter", ipTrafficFilter);
            fDescs.add(fDescs_entry);
        }

        HashMap<Object, Object> trafChar = new HashMap<>();
        trafChar.put("fDescs", fDescs);

        ueComm.put("trafChar", trafChar);
        ueComm.put("ulVol", ulVol);
        ueComm.put("dlVol", dlVol);

        return ueComm;
    }
}
