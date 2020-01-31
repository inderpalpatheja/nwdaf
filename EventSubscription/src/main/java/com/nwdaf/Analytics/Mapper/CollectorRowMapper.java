package com.nwdaf.Analytics.Mapper;


import com.nwdaf.Analytics.model.CollectorDataModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CollectorRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        CollectorDataModel collectorDataModel = new CollectorDataModel();

        collectorDataModel.setSnSaai(resultSet.getString("snSaai"));
        collectorDataModel.setLOAD_LEVEL_INFORMATION(resultSet.getInt("load_level_info"));
        collectorDataModel.setEventId(resultSet.getInt("event_id"));

        return collectorDataModel;
    }
}
