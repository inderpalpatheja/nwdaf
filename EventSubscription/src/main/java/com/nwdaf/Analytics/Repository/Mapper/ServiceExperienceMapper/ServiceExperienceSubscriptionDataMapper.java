package com.nwdaf.Analytics.Repository.Mapper.ServiceExperienceMapper;

import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceExperienceSubscriptionDataMapper implements RowMapper<ServiceExperienceSubscriptionData> {

    @Override
    public ServiceExperienceSubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String subscriptionId = resultSet.getString("subscriptionId");
        String supi = resultSet.getString("supi");
        String snssai = resultSet.getString("snssai");

        return new ServiceExperienceSubscriptionData(ID, subscriptionId, supi, snssai);
    }
}