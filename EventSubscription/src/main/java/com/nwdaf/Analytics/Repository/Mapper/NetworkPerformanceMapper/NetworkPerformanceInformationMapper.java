package com.nwdaf.Analytics.Repository.Mapper.NetworkPerformanceMapper;

import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NetworkPerformanceInformationMapper implements RowMapper<NetworkPerformanceInformation> {

    @Override
    public NetworkPerformanceInformation mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        Integer nwPerfType = resultSet.getInt("nwPerfType");
        Integer relativeRatio = resultSet.getInt("relativeRatio");
        Integer absoluteNum = resultSet.getInt("absoluteNum");

        return new NetworkPerformanceInformation(ID, supi, nwPerfType, relativeRatio, absoluteNum);
    }
}
