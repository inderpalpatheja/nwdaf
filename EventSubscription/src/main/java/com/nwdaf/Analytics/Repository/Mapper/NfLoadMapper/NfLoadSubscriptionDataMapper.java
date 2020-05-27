package com.nwdaf.Analytics.Repository.Mapper.NfLoadMapper;

import com.nwdaf.Analytics.Model.TableType.NfLoad.NfLoadSubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NfLoadSubscriptionDataMapper implements RowMapper<NfLoadSubscriptionData> {

    @Override
    public NfLoadSubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String subscriptionId = resultSet.getString("subscriptionId");
        Integer nfType = resultSet.getInt("nfType");
        String nfInstanceId = resultSet.getString("nfInstanceId");
        String supi = resultSet.getString("supi");
        String snssai = resultSet.getString("snssai");

        Integer nfLoadLevelThrd = resultSet.getInt("nfLoadLevelThrd");
        Integer nfCpuUsageThrd = resultSet.getInt("nfCpuUsageThrd");
        Integer nfMemoryUsageThrd = resultSet.getInt("nfMemoryUsageThrd");
        Integer nfStorageUsageThrd = resultSet.getInt("nfStorageUsageThrd");

        return new NfLoadSubscriptionData(ID, subscriptionId, nfType, nfInstanceId, supi, snssai, nfLoadLevelThrd, nfCpuUsageThrd, nfMemoryUsageThrd, nfStorageUsageThrd);
    }
}
