package com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper;




import com.nwdaf.Analytics.Model.TableType.UEMobility.nwdafUEmobility;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UEMobilityMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        // SliceLoadLevelInformation loadLevel = new SliceLoadLevelInformation();

        nwdafUEmobility nwdafUEmobility1 = new nwdafUEmobility();
        nwdafUEmobility1.setSupi(resultSet.getString("supi"));
        nwdafUEmobility1.setDurationSec(resultSet.getInt("DurationSec"));
        nwdafUEmobility1.setLocationInfo(resultSet.getString("location"));

        return nwdafUEmobility1;
    }
}
