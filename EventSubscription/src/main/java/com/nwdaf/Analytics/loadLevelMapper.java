package com.nwdaf.Analytics;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class loadLevelMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NnwdafEventsSubscription load_level_information = new NnwdafEventsSubscription();
        load_level_information.setSnssais(resultSet.getString("snssais"));
        return load_level_information;
    }
}