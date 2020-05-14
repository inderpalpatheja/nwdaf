package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.NotificationData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationDataMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        return new NotificationData(resultSet.getString("subscriptionId"), resultSet.getInt("loadLevelThreshold"));
    }
}
