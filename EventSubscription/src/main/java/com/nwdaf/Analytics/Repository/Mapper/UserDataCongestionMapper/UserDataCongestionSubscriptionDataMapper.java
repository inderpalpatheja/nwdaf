package com.nwdaf.Analytics.Repository.Mapper.UserDataCongestionMapper;

import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataCongestionSubscriptionDataMapper implements RowMapper<UserDataCongestionSubscriptionData> {

    @Override
    public UserDataCongestionSubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String subscriptionID = resultSet.getString("subscriptionID");
        String supi = resultSet.getString("supi");
        Integer congType = resultSet.getInt("congType");
        Integer congLevelThreshold = resultSet.getInt("congLevelThreshold");

        return new UserDataCongestionSubscriptionData(ID, subscriptionID, supi, congType, congLevelThreshold);
    }
}
