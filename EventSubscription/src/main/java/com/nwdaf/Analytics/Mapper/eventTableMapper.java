package com.nwdaf.Analytics.Mapper;

import com.nwdaf.Analytics.NnwdafEventsSubscription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class eventTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();

        nnwdafEventsSubscription.setSubscriptionID(resultSet.getString("subscriptionID"));
        nnwdafEventsSubscription.setSnssais(resultSet.getString("snssais"));


        return nnwdafEventsSubscription;
    }
}