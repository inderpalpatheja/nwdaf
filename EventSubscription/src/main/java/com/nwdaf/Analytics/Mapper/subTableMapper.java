package com.nwdaf.Analytics.Mapper;

import com.nwdaf.Analytics.NnwdafEventsSubscription;

import com.nwdaf.Analytics.model.SubTableDataModel;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class subTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        // LOAD_LEVEL_INFORMATION load_level_information =  new LOAD_LEVEL_INFORMATION();

      //  NnwdafEventsSubscription load_level_information = new NnwdafEventsSubscription();
        SubTableDataModel subTableDataModel = new SubTableDataModel();


        subTableDataModel.setCorrelationID(resultSet.getString("subscriptionID"));
        subTableDataModel.setCorrelationID(resultSet.getString("correlationID"));
        subTableDataModel.setCorrelationID(resultSet.getString("unSubCorrelationID"));
        // load_level_information.setEventID(resultSet.getInt("eventId"));
        // load_level_information.setSnssais(resultSet.getString("snssai"));
        //load_level_information.setAnySlice(resultSet.getBoolean("anySlice"));
        // load_level_information.setEventID(resultSet.getInt("id"));
       // load_level_information.setLoadLevelThreshold(resultSet.getInt("currentLoadLevelInfo"));
       // load_level_information.setSubscriptionID(resultSet.getString("subscriptionID"));

        return subTableDataModel;
    }
}
