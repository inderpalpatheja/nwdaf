package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Controller.ConnectionCheck.QosAnalyticsInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosAnalyticsMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        QosAnalyticsInfo qosInfo = new QosAnalyticsInfo();

        qosInfo.setSnssais(resultSet.getString("snssais"));
        qosInfo.setRanUeThroughput(resultSet.getInt("ranUeThroughput"));
        qosInfo.setQosFlowRetain(resultSet.getInt("qosFlowRetain"));
        qosInfo.setMessage("Data found for " + qosInfo.getSnssais());
        qosInfo.setDataStatus(Boolean.TRUE);

        return qosInfo;
    }
}
