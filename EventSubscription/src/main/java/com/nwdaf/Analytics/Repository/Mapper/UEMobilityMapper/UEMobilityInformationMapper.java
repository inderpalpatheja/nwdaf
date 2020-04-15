package com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper;


import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilityInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UEMobilityInformationMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        // SliceLoadLevelInformation loadLevel = new SliceLoadLevelInformation();

        UEMobilityInformation UEMobility1 = new UEMobilityInformation();

        UEMobility1.setSupi(resultSet.getString("supi"));
        UEMobility1.setDurationSec(resultSet.getInt("DurationSec"));


        return UEMobility1;
    }
}
