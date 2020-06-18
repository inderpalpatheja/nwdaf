package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.TableType.SubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        SubscriptionTable subTable = new SubscriptionTable();

        subTable.setID(resultSet.getInt("ID"));
        subTable.setSubscriptionId(resultSet.getString("subscriptionId"));
        subTable.setEvent(resultSet.getInt("event"));
        subTable.setNotificationMethod(resultSet.getInt("notificationMethod"));
        subTable.setRepetitionPeriod(resultSet.getInt("repetitionPeriod"));

        return subTable;
    }
}