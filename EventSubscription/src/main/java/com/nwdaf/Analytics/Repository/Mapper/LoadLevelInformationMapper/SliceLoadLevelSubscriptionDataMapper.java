package com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper;

import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SliceLoadLevelSubscriptionDataMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        SliceLoadLevelSubscriptionData subTable = new SliceLoadLevelSubscriptionData();

        subTable.setID(resultSet.getInt("ID"));
        subTable.setSubscriptionID(resultSet.getString("subscriptionID"));
        subTable.setSnssais(resultSet.getString("snssais"));
        subTable.setLoadLevelThreshold(resultSet.getInt("loadLevelThreshold"));

        return subTable;
    }
}