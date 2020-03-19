package com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper;


import com.nwdaf.Analytics.Model.TableType.UEMobility.nwdafUEmobilitySubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class nwdafUEmobilitySubscriptionTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        nwdafUEmobilitySubscriptionTable uEmobilitySubscriptionTableModel = new nwdafUEmobilitySubscriptionTable();

        uEmobilitySubscriptionTableModel.setID(resultSet.getInt("ID"));
        uEmobilitySubscriptionTableModel.setSupi(resultSet.getString("supi"));
        uEmobilitySubscriptionTableModel.setSubscriptionID(UUID.fromString(resultSet.getString("subscriptionID")));
        uEmobilitySubscriptionTableModel.setCorrelationID(UUID.fromString(resultSet.getString("correlationID")));

        return uEmobilitySubscriptionTableModel;
    }
}
