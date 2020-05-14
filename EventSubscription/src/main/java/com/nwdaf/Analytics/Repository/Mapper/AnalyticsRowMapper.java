package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalyticsRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {


        EventConnection connect = new EventConnection();
        connect.setMessage("Data Found");
        connect.setDataStatus(Boolean.TRUE);
        connect.setSnssai(resultSet.getString("snssai"));
        connect.setCurrentLoadLevelInfo(resultSet.getInt("currentLoadLevel"));

        return connect;

    }
}
