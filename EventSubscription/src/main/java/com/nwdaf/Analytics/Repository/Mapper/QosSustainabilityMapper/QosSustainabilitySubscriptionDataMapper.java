package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilitySubscriptionDataMapper implements RowMapper<QosSustainabilitySubscriptionData> {

    @Override
    public QosSustainabilitySubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String subscriptionId = resultSet.getString("subscriptionId");
        Integer _5Qi = resultSet.getInt("5Qi");
        String tai = resultSet.getString("tai");
        String snssai = resultSet.getString("snssai");
        Integer ranUeThrouThrd = resultSet.getInt("ranUeThrouThrd");
        Integer qosFlowRetThrd = resultSet.getInt("qosFlowRetThrd");
        String relTimeUnit = resultSet.getString("relTimeUnit");

        return new QosSustainabilitySubscriptionData(ID, subscriptionId, _5Qi, tai, snssai, ranUeThrouThrd, qosFlowRetThrd, relTimeUnit);
    }
}
