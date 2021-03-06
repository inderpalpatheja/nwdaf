package com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper;


import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class nwdafUEmobilitySubscriptionTableMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        UEMobilitySubscriptionTable uEmobilitySubscriptionTableModel = new UEMobilitySubscriptionTable();

        uEmobilitySubscriptionTableModel.setID(resultSet.getInt("ID"));
        uEmobilitySubscriptionTableModel.setSupi(resultSet.getString("supi"));
        uEmobilitySubscriptionTableModel.setSubscriptionId(resultSet.getString("subscriptionId"));
        uEmobilitySubscriptionTableModel.setCorrelationId(resultSet.getString("correlationId"));

        return uEmobilitySubscriptionTableModel;
    }
}
