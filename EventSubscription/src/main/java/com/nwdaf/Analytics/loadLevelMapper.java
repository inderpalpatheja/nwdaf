package com.nwdaf.Analytics;

import com.nwdaf.Analytics.model.LOAD_LEVEL_INFORMATION;
import com.nwdaf.Analytics.model.analytics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class loadLevelMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

       // LOAD_LEVEL_INFORMATION load_level_information =  new LOAD_LEVEL_INFORMATION();

        NnwdafEventsSubscription load_level_information = new NnwdafEventsSubscription();

        load_level_information.setSnssais(resultSet.getString("snssais"));
//        load_level_information.setLoadLevelThreshold(resultSet.getInt("currentLoadLevelInfo"));
  //      load_level_information.setSubscriptionID(resultSet.getString("subscriptionID"));

        return load_level_information;
    }
}