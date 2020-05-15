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
        String snssai = resultSet.getString("snssai");
        String subscriptionId = resultSet.getString("subscriptionId");
        String correlationId = resultSet.getString("correlationId");
        Integer refCount = resultSet.getInt("refCount");

        return new ServiceExperienceSubscriptionTable(ID, supi, snssai, subscriptionId, correlationId, refCount);
    }
}
