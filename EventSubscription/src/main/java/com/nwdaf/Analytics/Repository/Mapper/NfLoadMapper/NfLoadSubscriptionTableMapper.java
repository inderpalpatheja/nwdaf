package com.nwdaf.Analytics.Repository.Mapper.NfLoadMapper;

import com.nwdaf.Analytics.Model.TableType.NfLoad.NfLoadSubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NfLoadSubscriptionTableMapper implements RowMapper<NfLoadSubscriptionTable> {

    @Override
    public NfLoadSubscriptionTable mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        Integer nfType = resultSet.getInt("nfType");
        String nfInstanceId = resultSet.getString("nfInstanceId");

        String subscriptionId = resultSet.getString("subscriptionId");
        String correlationId = resultSet.getString("correlationId");
        Integer refCount = resultSet.getInt("refCount");

        return new NfLoadSubscriptionTable(ID, nfType, nfInstanceId, subscriptionId, correlationId, refCount);
    }
}
