package com.nwdaf.Analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

@Repository
public class Nnwdaf_repository implements RowMapper {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public Boolean subscribeNF(NnwdafEventsSubscription user)
    {
        String query = "INSERT INTO eventTable VALUES('" + UUID.randomUUID() + "', ?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setInt(1, user.getEventID());
                preparedStatement.setString(2, user.getNotificationURI());
                preparedStatement.setInt(3, user.getNotifMethod());
                preparedStatement.setInt(4, user.getRepetitionPeriod());
                preparedStatement.setInt(5, user.getLoadLevelThreshold());

                return preparedStatement.execute();
            }
        });
    }



    public NnwdafEventsSubscription findById(String id)
    {
        String query = "SELECT * FROM eventTable WHERE subscriptionID = ?";

        try
        { return (NnwdafEventsSubscription) this.jdbcTemplate.queryForObject(query, new Object[] { id }, this::mapRow); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }



    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NnwdafEventsSubscription user = new NnwdafEventsSubscription();

        user.setSubscriptionID(resultSet.getString("subscriptionID"));
        user.setEventID(resultSet.getInt("eventID"));
        user.setNotificationURI(resultSet.getString("notificationURI"));
        user.setNotifMethod(resultSet.getInt("notifMethod"));
        user.setRepetitionPeriod(resultSet.getInt("repetitionPeriod"));
        user.setLoadLevelThreshold(resultSet.getInt("loadLevelThreshold"));

        return user;
    }



    public Integer updateNF(NnwdafEventsSubscription user, String id)
    {
        String UPDATE_QUERY = "UPDATE eventTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ?, loadLevelThreshold = ? WHERE subscriptionID = ?";

        return jdbcTemplate.update(UPDATE_QUERY, user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), user.getLoadLevelThreshold(), id);

        //String query = "UPDATE eventTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ?, loadLevelThreshold = ? WHERE subscriptionID = '" + id + "'";


    //    Object[] parameters = { user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), user.getLoadLevelThreshold() };
      //  int[] types = { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };

      //  return jdbcTemplate.update(query, parameters, types);
    }


    public Integer unsubscribeNF(String id)
    { return jdbcTemplate.update("DELETE FROM eventTable WHERE subscriptionID = ?", id); }
}
