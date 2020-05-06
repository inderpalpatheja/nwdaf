package com.nwdaf.Analytics.Repository.Mapper.UserDataCongestionMapper;

import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataCongestionInformationMapper implements RowMapper<UserDataCongestionInformation> {

    @Override
    public UserDataCongestionInformation mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        Integer congType = resultSet.getInt("congType");
        String tai = resultSet.getString("tai");
        Integer congLevel = resultSet.getInt("congLevel");

        return new UserDataCongestionInformation(ID, supi, congType, tai, congLevel);
    }
}
