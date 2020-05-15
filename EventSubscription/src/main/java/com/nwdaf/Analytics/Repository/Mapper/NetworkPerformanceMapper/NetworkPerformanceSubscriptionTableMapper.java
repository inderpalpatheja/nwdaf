package com.nwdaf.Analytics.Repository.Mapper.NetworkPerformanceMapper;

import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceSubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NetworkPerformanceSubscriptionTableMapper implements RowMapper<NetworkPerformanceSubscriptionTable> {

    @Override
    public NetworkPerformanceSubscriptionTable mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        Integer nwPerfType = resultSet.getInt("nwPerfType");
        String subscriptionID = resultSet.getString("subscriptionId");
        String correlationID = resultSet.getString("correlationId");
        Integer refCount = resultSet.getInt("refCount");

        return new NetworkPerformanceSubscriptionTable(ID, supi, nwPerfType, subscriptionID, correlationID, refCount);
    }
}
