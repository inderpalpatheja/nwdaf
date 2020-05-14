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

        String tai = resultSet.getString("tai");
        String snssai = resultSet.getString("snssai");
        Integer qosFlowRet = resultSet.getInt("qosFlowRet");
        Integer ranUeThrou = resultSet.getInt("ranUeThrou");

        return new QosSustainabilityInfo(message, data_status, tai, snssai, qosFlowRet, ranUeThrou);
    }
}
