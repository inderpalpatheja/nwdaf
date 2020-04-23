package com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper;

import com.nwdaf.Analytics.Model.AnalyticsInformation.ServiceExperienceInfo;
import com.nwdaf.Analytics.Model.CustomData.ServiceExperience.SvcExperience;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceExperienceInfoMapper implements RowMapper<ServiceExperienceInfo> {

    final String message = "DATA FOUND";
    final Boolean data_status = Boolean.TRUE;

    @Override
    public ServiceExperienceInfo mapRow(ResultSet resultSet, int i) throws SQLException {

        SvcExperience svcExp = new SvcExperience();

        svcExp.setMos(resultSet.getFloat("mos"));
        svcExp.setUpperRange(resultSet.getFloat("upperRange"));
        svcExp.setLowerRange(resultSet.getFloat("lowerRange"));

        String supi = resultSet.getString("supi");
        String snssais = resultSet.getString("snssais");

        return new ServiceExperienceInfo(message, data_status, supi, snssais, svcExp);
    }
}
