package com.nwdaf.Analytics;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class analyticsRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {


        EventConnection connect = new EventConnection();
        connect.setMessage("Data Found");
        connect.setDataStatus(Boolean.TRUE);
        connect.setSnssais(resultSet.getString("snssais"));
        connect.setCurrentLoadLevelInfo(resultSet.getInt("currentLoadLevel"));

        return connect;

    }
}
