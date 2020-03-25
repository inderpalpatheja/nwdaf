package com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper;

import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnectionForUEMobility;
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.EventConnectionUE;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalyticsRowMapperForUEMobility implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {


        EventConnectionUE connect = new EventConnectionUE();
        connect.setMessage("Data Found");
        connect.setDataStatus(Boolean.TRUE);
        connect.setSupi(resultSet.getString("supi"));
        connect.setTs(resultSet.getDate("ts"));
        connect.setDurationSec(resultSet.getInt("DurationSec"));
        connect.setLocation(resultSet.getString("location"));

        return connect;

    }
}
