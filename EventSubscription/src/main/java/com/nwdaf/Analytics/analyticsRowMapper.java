package com.nwdaf.Analytics;

import com.nwdaf.Analytics.model.analytics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class analyticsRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {


        events_connection connect = new events_connection();
        connect.setSnssais(resultSet.getString("snssais"));
        connect.setAnySlice(resultSet.getBoolean("anySlice"));
        connect.setSubscriptionID(resultSet.getString("subscriptionID"));
        connect.setCurrentLoadLevelInfo(resultSet.getInt("currentLoadLevelInfo"));

        return connect;

    }
}
