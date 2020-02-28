package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.TableType.SliceLoadLevelSubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SliceLoadLevelSubscriptionTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        SliceLoadLevelSubscriptionTable subTable = new SliceLoadLevelSubscriptionTable();

        subTable.setID(resultSet.getInt("ID"));
        subTable.setSnssais(resultSet.getString("snssais"));
        subTable.setSubscriptionID(resultSet.getString("subscriptionID"));
        subTable.setCorrelationID(resultSet.getString("correlationID"));

        return subTable;
    }
}
