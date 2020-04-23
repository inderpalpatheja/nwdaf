package com.nwdaf.Analytics.Repository.Mapper.ServiceExperienceMapper;

import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceExperienceInformationMapper implements RowMapper<ServiceExperienceInformation> {

    @Override
    public ServiceExperienceInformation mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        String snssais = resultSet.getString("snssais");
        Float mos = resultSet.getFloat("mos");
        Float upperRange = resultSet.getFloat("upperRange");
        Float lowerRange = resultSet.getFloat("lowerRange");

        return new ServiceExperienceInformation(ID, supi, snssais, mos, upperRange, lowerRange);
    }
}
