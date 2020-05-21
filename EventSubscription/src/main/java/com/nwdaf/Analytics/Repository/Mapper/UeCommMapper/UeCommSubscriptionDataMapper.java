package com.nwdaf.Analytics.Repository.Mapper.UeCommMapper;

import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommSubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UeCommSubscriptionDataMapper implements RowMapper<UeCommSubscriptionData> {

    @Override
    public UeCommSubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String subscriptionId = resultSet.getString("subscriptionId");
        String supi = resultSet.getString("supi");
        Integer maxAnaEntry = resultSet.getInt("maxAnaEntry");

        return new UeCommSubscriptionData(ID, subscriptionId, supi, maxAnaEntry);
    }
}
