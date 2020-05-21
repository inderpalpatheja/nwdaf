package com.nwdaf.Analytics.Repository.Mapper.UeCommMapper;

import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommSubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UeCommSubscriptionTableMapper implements RowMapper<UeCommSubscriptionTable> {

    @Override
    public UeCommSubscriptionTable mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        String subscriptionId = resultSet.getString("subscriptionId");
        String correlationId = resultSet.getString("correlationId");
        Integer refCount = resultSet.getInt("refCount");

        return new UeCommSubscriptionTable(ID, supi, subscriptionId, correlationId, refCount);
    }
}
