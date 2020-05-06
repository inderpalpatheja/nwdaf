package com.nwdaf.Analytics.Repository.Mapper.UserDataCongestionMapper;

import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataCongestionSubscriptionTableMapper implements RowMapper<UserDataCongestionSubscriptionTable> {

    @Override
    public UserDataCongestionSubscriptionTable mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        Integer congType = resultSet.getInt("congType");
        String tai = resultSet.getString("tai");
        String subscriptionID = resultSet.getString("subscriptionID");
        String correlationID = resultSet.getString("correlationID");
        Integer refCount = resultSet.getInt("refCount");

        return new UserDataCongestionSubscriptionTable(ID, supi, congType, tai, subscriptionID, correlationID, refCount);
    }
}
