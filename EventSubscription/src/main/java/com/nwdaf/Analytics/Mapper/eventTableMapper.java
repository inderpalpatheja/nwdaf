package com.nwdaf.Analytics.Mapper;

import com.nwdaf.Analytics.NnwdafEventsSubscription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class eventTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NnwdafEventsSubscription user = new NnwdafEventsSubscription();

        user.setSubscriptionID(resultSet.getString("subscriptionID"));
      //  user.setEventID(resultSet.getInt("eventID"));
       // user.setNotificationURI(resultSet.getString("notificationURI"));
        user.setSnssais(resultSet.getString("snssais"));

      //  user.setLoadLevelThreshold(resultSet.getInt("loadLevelThreshold"));

        return user;
    }
}