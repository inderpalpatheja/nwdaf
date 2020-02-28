package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.TableType.SliceLoadLevelInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SliceLoadLevelInformationMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        SliceLoadLevelInformation loadLevel = new SliceLoadLevelInformation();

        loadLevel.setSnssais(resultSet.getString("snssais"));
        loadLevel.setCurrentLoadLevel(resultSet.getInt("currentLoadLevel"));

        return loadLevel;
    }
}
