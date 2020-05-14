package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilitySubscriptionTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        QosSustainabilitySubscriptionTable qosSustainabilitySubscriptionTable = new QosSustainabilitySubscriptionTable();

        qosSustainabilitySubscriptionTable.setID(resultSet.getInt("ID"));
        qosSustainabilitySubscriptionTable.setSnssai(resultSet.getString("snssai"));
        qosSustainabilitySubscriptionTable.setSubscriptionId(resultSet.getString("subscriptionId"));
        qosSustainabilitySubscriptionTable.setCorrelationId(resultSet.getString("correlationId"));
        qosSustainabilitySubscriptionTable.setRefCount(resultSet.getInt("refCount"));

        return qosSustainabilitySubscriptionTable;
    }
}
