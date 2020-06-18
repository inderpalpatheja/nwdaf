package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.TableType.SubscriptionID;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionIDMapper implements RowMapper<SubscriptionID> {

    @Override
    public SubscriptionID mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SubscriptionID(resultSet.getString("subscriptionId"), resultSet.getString("notificationURI"));
    }
}
