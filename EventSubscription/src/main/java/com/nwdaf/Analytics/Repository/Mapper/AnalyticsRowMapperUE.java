package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnectionUE;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalyticsRowMapperUE implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {


        EventConnectionUE connectUE = new EventConnectionUE();
        connectUE.setMessage("Data Found");
        connectUE.setDataStatus(Boolean.TRUE);
        connectUE.setSupi(resultSet.getString("supi"));
        connectUE.setTs(resultSet.getDate("ts"));
        connectUE.setDurationSec((resultSet.getInt("DurationSec")));
        connectUE.setLocation(resultSet.getString("location"));


        return connectUE;

    }
}
