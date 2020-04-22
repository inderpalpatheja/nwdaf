package com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper;

import com.nwdaf.Analytics.Model.AnalyticsInformation.QosSustainabilityInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilityInfoMapper implements RowMapper<QosSustainabilityInfo> {

    final String message = "DATA FOUND";
    final Boolean data_status = Boolean.TRUE;

    @Override
    public QosSustainabilityInfo mapRow(ResultSet resultSet, int i) throws SQLException {

        String plmnID = resultSet.getString("plmnID");
        String snssais = resultSet.getString("snssais");
        Integer qosFlowRetain = resultSet.getInt("qosFlowRetain");
        Integer ranUeThroughput = resultSet.getInt("ranUeThroughput");

        return new QosSustainabilityInfo(message, data_status, plmnID, snssais, qosFlowRetain, ranUeThroughput);
    }
}
