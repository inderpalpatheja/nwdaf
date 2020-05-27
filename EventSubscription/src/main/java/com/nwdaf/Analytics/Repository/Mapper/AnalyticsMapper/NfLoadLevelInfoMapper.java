package com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

public class NfLoadLevelInfoMapper implements RowMapper {

    Random random = new Random();

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer nfType = resultSet.getInt("nfType");
        String nfInstanceId = resultSet.getString("nfInstanceId");

        Integer nfCpuUsage = resultSet.getInt("nfCpuUsage");
        Integer nfMemoryUsage = resultSet.getInt("nfMemoryUsage");
        Integer nfStorageUsage = resultSet.getInt("nfStorageUsage");
        Integer nfLoadLevel = resultSet.getInt("nfLoadLevel");


        HashMap<Object, Object> nfLoadLevelInfo = new HashMap<>();

        nfLoadLevelInfo.put("nfType", nfType);
        nfLoadLevelInfo.put("nfInstanceId", nfInstanceId);

        HashMap<Object, Object> nfStatus = new HashMap<>();
        nfStatus.put("statusRegistered", random.nextInt(100));

        nfLoadLevelInfo.put("nfStatus", nfStatus);

        nfLoadLevelInfo.put("nfCpuUsage", nfCpuUsage);
        nfLoadLevelInfo.put("nfMemoryUsage", nfMemoryUsage);
        nfLoadLevelInfo.put("nfStorageUsage", nfStorageUsage);
        nfLoadLevelInfo.put("nfLoadLevel", nfLoadLevel);

        return nfLoadLevelInfo;
    }
}
