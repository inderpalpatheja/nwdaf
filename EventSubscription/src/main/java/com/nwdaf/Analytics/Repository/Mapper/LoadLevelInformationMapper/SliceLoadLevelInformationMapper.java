package com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper;

import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SliceLoadLevelInformationMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        SliceLoadLevelInformation loadLevel = new SliceLoadLevelInformation();

        loadLevel.setSnssai(resultSet.getString("snssai"));
        loadLevel.setCurrentLoadLevel(resultSet.getInt("currentLoadLevel"));

        return loadLevel;
    }
}
