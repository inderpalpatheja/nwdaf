package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.QosNotificationData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosNotificationDataMapper implements RowMapper {

    QosType qosType;

    public QosNotificationDataMapper(QosType qosType)
    { this.qosType = qosType; }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { return new QosNotificationData(resultSet.getString("subscriptionID"), resultSet.getInt("ranUeThroughputThreshold"), QosType.RAN_UE_THROUGHPUT); }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { return new QosNotificationData(resultSet.getString("subscriptionID"), resultSet.getInt("qosFlowRetainThreshold"), QosType.QOS_FLOW_RETAIN); }

        return null;
    }
}
