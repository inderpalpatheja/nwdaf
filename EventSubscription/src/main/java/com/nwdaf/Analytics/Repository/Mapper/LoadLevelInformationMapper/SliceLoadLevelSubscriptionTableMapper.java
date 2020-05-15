package com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper;

import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SliceLoadLevelSubscriptionTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        SliceLoadLevelSubscriptionTable subTable = new SliceLoadLevelSubscriptionTable();

        subTable.setID(resultSet.getInt("ID"));
        subTable.setSnssai(resultSet.getString("snssai"));
        subTable.setSubscriptionId(resultSet.getString("subscriptionId"));
        subTable.setCorrelationId(resultSet.getString("correlationId"));

        return subTable;
    }
}
