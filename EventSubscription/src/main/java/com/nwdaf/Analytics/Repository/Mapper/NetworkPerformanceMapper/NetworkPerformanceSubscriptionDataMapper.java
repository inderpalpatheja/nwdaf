package com.nwdaf.Analytics.Repository.Mapper.NetworkPerformanceMapper;

import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceSubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NetworkPerformanceSubscriptionDataMapper implements RowMapper<NetworkPerformanceSubscriptionData> {

    @Override
    public NetworkPerformanceSubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String subscriptionID = resultSet.getString("subscriptionID");
        String supi = resultSet.getString("supi");
        Integer nwPerfType = resultSet.getInt("nwPerfType");
        Integer relativeRatioThreshold = resultSet.getInt("relativeRatioThreshold");
        Integer absoluteNumThreshold = resultSet.getInt("absoluteNumThreshold");

        return new NetworkPerformanceSubscriptionData(ID, subscriptionID, supi, nwPerfType, relativeRatioThreshold, absoluteNumThreshold);
    }
}
