package com.nwdaf.Analytics.Repository.Mapper.AbnormalBehaviourMapper;

import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourSubscriptionTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AbnormalBehaviourSubscriptionTableMapper implements RowMapper<AbnormalBehaviourSubscriptionTable> {

    @Override
    public AbnormalBehaviourSubscriptionTable mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        Integer excepId = resultSet.getInt("excepId");
        String subscriptionId = resultSet.getString("subscriptionId");
        String correlationId = resultSet.getString("correlationId");
        Integer refCount = resultSet.getInt("refCount");

        return new AbnormalBehaviourSubscriptionTable(ID, supi, excepId, subscriptionId, correlationId, refCount);
    }
}
