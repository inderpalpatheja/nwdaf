package com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper;




import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobility;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UEMobilityMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        // SliceLoadLevelInformation loadLevel = new SliceLoadLevelInformation();

        UEMobility UEMobility1 = new UEMobility();
        UEMobility1.setSupi(resultSet.getString("supi"));
        UEMobility1.setDurationSec(resultSet.getInt("DurationSec"));
        UEMobility1.setLocationInfo(resultSet.getString("location"));



        return UEMobility1;
    }
}
