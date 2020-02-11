package com.nwdaf.Analytics.NwdafMapper;

import com.nwdaf.Analytics.NwdafModel.NwdafSubscriptionTableModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NwdafSubscriptionTableModel subTable = new NwdafSubscriptionTableModel();

        subTable.setSubscriptionID(resultSet.getString("subscriptionID"));
        subTable.setEventID(resultSet.getInt("eventID"));
        subTable.setNotificationURI(resultSet.getString("notificationURI"));
        subTable.setNotifMethod(resultSet.getInt("notifMethod"));
        subTable.setRepetitionPeriod(resultSet.getInt("repetitionPeriod"));

        return subTable;
    }
}