package com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@AllArgsConstructor
public class QosSustainabilityInfoMapper implements RowMapper {

    String tai;

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ranUeThrouThrd = resultSet.getInt("ranUeThrouThrd");
        Integer qosFlowRetThrd = resultSet.getInt("qosFlowRetThrd");
        String relTimeUnit = resultSet.getString("relTimeUnit");

        HashMap<Object, Object> qosSustainInfos = new HashMap<>();

        String areaInfo[] = tai.split("-");

        HashMap<Object, Object> area = new HashMap<>();
        List<Object> tais = new ArrayList<>();
        HashMap<Object, Object> tai = new HashMap<>();

        HashMap<Object, Object> plmnId = new HashMap<>();
        plmnId.put("mcc", areaInfo[0]);
        plmnId.put("mnc", areaInfo[1]);

        tai.put("plmnId", plmnId);
        tai.put("tac", areaInfo[2]);

        tais.add(tai);
        area.put("tais", tais);

        qosSustainInfos.put("areaInfo", area);

        if(qosFlowRetThrd != null)
        {
            HashMap<Object, Object> qosFlowRetThd = new HashMap<>();
            qosFlowRetThd.put("relFlowNum", qosFlowRetThrd);
            qosFlowRetThd.put("relTimeUnit", relTimeUnit);

            qosSustainInfos.put("qosFlowRetThd", qosFlowRetThd);
        }

        if(ranUeThrouThrd != null)
        { qosSustainInfos.put("ranUeThrouThd", ranUeThrouThrd); }

        return qosSustainInfos;
    }
}
