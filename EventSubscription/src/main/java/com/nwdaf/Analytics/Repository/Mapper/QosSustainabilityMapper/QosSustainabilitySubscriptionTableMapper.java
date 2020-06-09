package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilitySubscriptionTableMapper implements RowMapper<QosSustainabilitySubscriptionTable> {

    @Override
    public QosSustainabilitySubscriptionTable mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String snssai = resultSet.getString("snssai");
        String tai = resultSet.getString("tai");
        String subscriptionId = resultSet.getString("subscriptionId");
        String correlationId = resultSet.getString("correlationId");
        Integer refCount = resultSet.getInt("refCount");

        return new QosSustainabilitySubscriptionTable(ID, snssai, tai, subscriptionId, correlationId, refCount);
    }
}
