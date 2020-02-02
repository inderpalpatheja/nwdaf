package com.nwdaf.Analytics.Mapper;

import com.nwdaf.Analytics.NnwdafEventsSubscription;
import com.nwdaf.Analytics.model.SubscrptionIdModel;
import com.sun.java.accessibility.util.EventID;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventIdRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        SubscrptionIdModel subscrptionIdModel = new SubscrptionIdModel();
       subscrptionIdModel.setSubsctiptionId(resultSet.getString("subscriptionID"));

        return subscrptionIdModel;
    }
}
