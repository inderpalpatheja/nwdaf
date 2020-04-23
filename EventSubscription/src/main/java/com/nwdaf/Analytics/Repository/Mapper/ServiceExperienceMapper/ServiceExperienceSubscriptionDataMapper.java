package com.nwdaf.Analytics.Repository.Mapper.ServiceExperienceMapper;

import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceExperienceSubscriptionDataMapper implements RowMapper<ServiceExperienceSubscriptionData> {

    @Override
    public ServiceExperienceSubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String subscriptionID = resultSet.getString("subscriptionID");
        String supi = resultSet.getString("supi");
        String snssais = resultSet.getString("snssais");

        return new ServiceExperienceSubscriptionData(ID, subscriptionID, supi, snssais);
    }
}
