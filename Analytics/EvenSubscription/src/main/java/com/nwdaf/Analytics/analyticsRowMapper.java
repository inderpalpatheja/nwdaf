package com.nwdaf.Analytics;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class analyticsRowMapper implements RowMapper{

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        analytics analytic = new analytics();

        analytic.setEventId(resultSet.getInt("eventId"));
        analytic.setLoad_level_info(resultSet.getInt("load_level_info"));
        analytic.setSnssais(resultSet.getString("snssais"));
       
        return analytic;
    }
}
