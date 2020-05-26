package com.nwdaf.Analytics.Repository.Mapper.NfLoadMapper;

import com.nwdaf.Analytics.Model.TableType.NfLoad.NfLoadInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NfLoadInformationMapper implements RowMapper<NfLoadInformation> {

    @Override
    public NfLoadInformation mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        Integer nfType = resultSet.getInt("nfType");
        String nfInstanceId = resultSet.getString("nfInstanceId");

        Integer nfLoadLevel = resultSet.getInt("nfLoadLevel");
        Integer nfCpuUsage = resultSet.getInt("nfCpuUsage");
        Integer nfMemoryUsage = resultSet.getInt("nfMemoryUsage");
        Integer nfStorageUsage = resultSet.getInt("nfStorageUsage");

        return new NfLoadInformation(ID, nfType, nfInstanceId, nfLoadLevel, nfCpuUsage, nfMemoryUsage, nfStorageUsage);
    }
}
