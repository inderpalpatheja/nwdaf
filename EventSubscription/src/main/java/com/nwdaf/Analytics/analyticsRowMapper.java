package com.nwdaf.Analytics;

import com.nwdaf.Analytics.model.analytics;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class analyticsRowMapper implements RowMapper{

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        /*analytics analytic = new analytics();

        analytic.setEventId(resultSet.getInt("eventId"));
        analytic.setLoad_level_info(resultSet.getInt("load_level_info"));
        analytic.setSnssais(resultSet.getString("snssais"));

       
        return analytic;*/
        events_connection connect=new events_connection();

        connect.setSnssais(resultSet.getString("snssais"));
        connect.setAnySlice(resultSet.getBoolean("anySlice"));
        connect.setSubscriptionID(resultSet.getString("subscriptionID"));

        return connect;
    }
}
