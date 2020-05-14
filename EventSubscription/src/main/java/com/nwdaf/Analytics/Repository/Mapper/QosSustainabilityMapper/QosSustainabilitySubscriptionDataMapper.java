package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilitySubscriptionDataMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        QosSustainabilitySubscriptionData qosSustainabilitySubscriptionData = new QosSustainabilitySubscriptionData();

        qosSustainabilitySubscriptionData.setID(resultSet.getInt("ID"));
        qosSustainabilitySubscriptionData.setSubscriptionId(resultSet.getString("subscriptionId"));

        qosSustainabilitySubscriptionData.set_5Qi(resultSet.getInt("5Qi"));
        qosSustainabilitySubscriptionData.setTai(resultSet.getString("tai"));

        qosSustainabilitySubscriptionData.setSnssai(resultSet.getString("snssai"));
        qosSustainabilitySubscriptionData.setRanUeThrouThrd(resultSet.getInt("ranUeThrouThrd"));
        qosSustainabilitySubscriptionData.setQosFlowRetThrd(resultSet.getInt("qosFlowRetThrd"));

        return qosSustainabilitySubscriptionData;

    }
}
