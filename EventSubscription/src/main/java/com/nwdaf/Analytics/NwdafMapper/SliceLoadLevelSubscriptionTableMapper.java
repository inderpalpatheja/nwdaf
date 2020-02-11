package com.nwdaf.Analytics.NwdafMapper;

import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelSubscriptionTableModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SliceLoadLevelSubscriptionTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NwdafSliceLoadLevelSubscriptionTableModel subTable = new NwdafSliceLoadLevelSubscriptionTableModel();

        subTable.setID(resultSet.getInt("ID"));
        subTable.setSnssais(resultSet.getString("snssais"));
        subTable.setSubscriptionID(resultSet.getString("subscriptionID"));
        subTable.setCorrelationID(resultSet.getString("correlationID"));

        return subTable;
    }
}
