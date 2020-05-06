package com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper;

import com.nwdaf.Analytics.Model.AnalyticsInformation.UserDataCongestionInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataCongestionInfoMapper implements RowMapper {

    final String message = "DATA FOUND";
    final Boolean data_status = Boolean.TRUE;

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        UserDataCongestionInfo userDataCongInfo = new UserDataCongestionInfo();

        userDataCongInfo.setMessage(message);
        userDataCongInfo.setData_status(data_status);

        userDataCongInfo.setSupi(resultSet.getString("supi"));
        userDataCongInfo.setCongType(resultSet.getInt("congType"));
        userDataCongInfo.setTai(resultSet.getString("tai"));
        userDataCongInfo.setCongLevel(resultSet.getInt("congLevel"));

        return userDataCongInfo.getInfo();
    }
}
