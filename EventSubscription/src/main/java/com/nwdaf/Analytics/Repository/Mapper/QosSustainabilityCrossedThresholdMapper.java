package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.CustomData.QosSustainability.QosThresholdType;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionData;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


@AllArgsConstructor
public class QosSustainabilityCrossedThresholdMapper implements RowMapper<QosSustainabilitySubscriptionData> {

    QosThresholdType qosThresholdType;

    @Override
    public QosSustainabilitySubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        QosSustainabilitySubscriptionData qosSustainabilitySubscriptionData = new QosSustainabilitySubscriptionData();

        qosSustainabilitySubscriptionData.setSubscriptionId(resultSet.getString("subscriptionId"));

        switch(qosThresholdType)
        {
            case QOS_FLOW_RETAIN: qosSustainabilitySubscriptionData.setQosFlowRetThrd(resultSet.getInt("qosFlowRetThrd"));
                                  qosSustainabilitySubscriptionData.setRelTimeUnit(resultSet.getString("relTimeUnit"));
                                  break;


            case RAN_UE_THROUGHPUT: qosSustainabilitySubscriptionData.setRanUeThrouThrd(resultSet.getInt("ranUeThrouThrd"));
                                    break;
        }

        return qosSustainabilitySubscriptionData;
    }
}
