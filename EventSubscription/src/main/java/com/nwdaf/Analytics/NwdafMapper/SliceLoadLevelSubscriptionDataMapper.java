package com.nwdaf.Analytics.NwdafMapper;

import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelSubscriptionDataModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SliceLoadLevelSubscriptionDataMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NwdafSliceLoadLevelSubscriptionDataModel subTable = new NwdafSliceLoadLevelSubscriptionDataModel();

        subTable.setID(resultSet.getInt("ID"));
        subTable.setSubscriptionID(resultSet.getString("subscriptionID"));
        subTable.setSnssais(resultSet.getString("snssais"));
        subTable.setLoadLevelThreshold(resultSet.getInt("loadLevelThreshold"));

        return subTable;
    }
}