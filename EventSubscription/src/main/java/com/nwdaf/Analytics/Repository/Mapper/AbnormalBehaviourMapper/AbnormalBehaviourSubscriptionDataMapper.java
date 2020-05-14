package com.nwdaf.Analytics.Repository.Mapper.AbnormalBehaviourMapper;

import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourSubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AbnormalBehaviourSubscriptionDataMapper implements RowMapper<AbnormalBehaviourSubscriptionData> {

    @Override
    public AbnormalBehaviourSubscriptionData mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String subscriptionId = resultSet.getString("subscriptionId");
        String supi = resultSet.getString("supi");
        Integer excepId = resultSet.getInt("excepId");
        Integer excepLevelThrd = resultSet.getInt("excepLevelThrd");

        return new AbnormalBehaviourSubscriptionData(ID, subscriptionId, supi, excepId, excepLevelThrd);
    }
}
