package com.nwdaf.Analytics.Repository.Mapper.UeCommMapper;

import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UeCommInformationMapper implements RowMapper<UeCommInformation> {

    @Override
    public UeCommInformation mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        Integer commDur = resultSet.getInt("commDur");
        String ts = resultSet.getString("ts");
        Integer ulVol = resultSet.getInt("ulVol");
        Integer dlVol = resultSet.getInt("dlVol");


        return new UeCommInformation(ID, supi, commDur, ts, ulVol, dlVol);
    }
}
