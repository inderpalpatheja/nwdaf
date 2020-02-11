package com.nwdaf.Analytics.NwdafMapper;

import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelInformationModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SliceLoadLevelInformationMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NwdafSliceLoadLevelInformationModel loadLevel = new NwdafSliceLoadLevelInformationModel();

        loadLevel.setSnssais(resultSet.getString("snssais"));
        loadLevel.setCurrentLoadLevel(resultSet.getInt("currentLoadLevel"));

        return loadLevel;
    }
}
