package com.nwdaf.Analytics.Repository.Mapper.ServiceExperienceMapper;

import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ServiceExperienceSubscriptionTableMapper implements RowMapper<ServiceExperienceSubscriptionTable> {

    @Override
    public ServiceExperienceSubscriptionTable mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        String snssais = resultSet.getString("snssais");
        String subscriptionID = resultSet.getString("subscriptionID");
        String correlationID = resultSet.getString("correlationID");
        Integer refCount = resultSet.getInt("refCount");

        return new ServiceExperienceSubscriptionTable(ID, supi, snssais, subscriptionID, correlationID, refCount);
    }
}
