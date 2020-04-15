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
        qosSustainabilitySubscriptionData.setSubscriptionID(resultSet.getString("subscriptionID"));

        qosSustainabilitySubscriptionData.set_5Qi(resultSet.getInt("5Qi"));
        qosSustainabilitySubscriptionData.setPlmnID(resultSet.getString("plmnID"));
        qosSustainabilitySubscriptionData.setTac(resultSet.getString("tac"));

        qosSustainabilitySubscriptionData.setSnssais(resultSet.getString("snssais"));
        qosSustainabilitySubscriptionData.setRanUeThroughputThreshold(resultSet.getInt("ranUeThroughputThreshold"));
        qosSustainabilitySubscriptionData.setQosFlowRetainThreshold(resultSet.getInt("qosFlowRetainThreshold"));

        return qosSustainabilitySubscriptionData;

    }
}
