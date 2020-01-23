package com.nwdaf.Collector.Repository;


import com.nwdaf.Collector.Mapper.CollectorRowMapper;
import com.nwdaf.Collector.Mapper.SubscriptionRowMapper;
import com.nwdaf.Collector.model.CollectorDataModel;
import com.nwdaf.Collector.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class SubscriptionRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Namf_EventExposure_Subscribe> subscriptionDetails() {

        return jdbcTemplate.query("SELECT nfId, eventId, notificationTargetAddress, correlationId, unSubCorrelationId FROM subscriptionTable", new SubscriptionRowMapper());

    }

    public Namf_EventExposure_Subscribe findByEventId(Integer eventId) {
        String query = "SELECT * FROM subscriptionTable WHERE eventId = ?";

        try {
            return (Namf_EventExposure_Subscribe) this.jdbcTemplate.queryForObject(query, new Object[]{eventId}
                    , new SubscriptionRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    public Boolean saveInfo(Namf_EventExposure_Subscribe namf_eventExposure_subscribe) {
        String query = "INSERT INTO subscriptionTable VALUES(?, ?, ?, ?,?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setInt(1, namf_eventExposure_subscribe.getNfId());
                preparedStatement.setInt(2, namf_eventExposure_subscribe.getEventId());
                preparedStatement.setString(3, namf_eventExposure_subscribe.getNotificationTargetAddress());
                preparedStatement.setInt(4, namf_eventExposure_subscribe.getCorrelationId());
                preparedStatement.setInt(5, namf_eventExposure_subscribe.getUnSubCorrelationId());
                return preparedStatement.execute();
            }
        });
    }


    public Namf_EventExposure_Subscribe findUnSubCorrelationId(int id) {

        String query = "SELECT * FROM subscriptionTable WHERE unSubCorrelationId = ?";

        try {
            return (Namf_EventExposure_Subscribe) this.jdbcTemplate.queryForObject(query, new Object[]{id}
                    , new SubscriptionRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public int deleteSubByUnSubCorrelationId(int id) {
        //public Integer deleteCollectorByEventId(Integer id)
        return jdbcTemplate.update("DELETE FROM subscriptionTable WHERE unSubCorrelationId=?", id);


    }
}
