package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.CustomData.NfLoad.NfThresholdType;
import com.nwdaf.Analytics.Model.TableType.NfLoad.NfLoadSubscriptionData;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


@AllArgsConstructor
public class NfLoadCrossedThresholdMapper implements RowMapper<NfLoadSubscriptionData> {

    NfThresholdType thresholdType;

    @Override
    public NfLoadSubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        NfLoadSubscriptionData nfLoadSubscriptionData = new NfLoadSubscriptionData();

        nfLoadSubscriptionData.setSubscriptionId(resultSet.getString("subscriptionId"));

        switch(thresholdType)
        {
            case CPU_USAGE: nfLoadSubscriptionData.setNfCpuUsageThrd(resultSet.getInt("nfCpuUsageThrd"));
                            break;

            case MEMORY_USAGE: nfLoadSubscriptionData.setNfMemoryUsageThrd(resultSet.getInt("nfMemoryUsageThrd"));
                               break;

            case STORAGE_USAGE: nfLoadSubscriptionData.setNfStorageUsageThrd(resultSet.getInt("nfStorageUsageThrd"));
                                break;

            case LOAD_LEVEL: nfLoadSubscriptionData.setNfLoadLevelThrd(resultSet.getInt("nfLoadLevelThrd"));
                             break;
        }

        return nfLoadSubscriptionData;
    }
}
