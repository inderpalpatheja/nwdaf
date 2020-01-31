package com.nwdaf.Analytics.Repository;


import com.nwdaf.Analytics.Mapper.CollectorRowMapper;
import com.nwdaf.Analytics.model.CollectorDataModel;

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
import java.util.UUID;

@Repository
public class CollectorRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<CollectorDataModel> collectorDetails() {

        // Working Code 1
        return jdbcTemplate.query("SELECT snSaai, load_level_info, event_id FROM AnalyticsInfo", new CollectorRowMapper());

    }

    public CollectorDataModel findByEventId(Integer eventId)
    {
        String query = "SELECT * FROM AnalyticsInfo WHERE event_id = ?";

        try
        { return (CollectorDataModel) this.jdbcTemplate.queryForObject(query, new Object[] { eventId }, new CollectorRowMapper()); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public Boolean saveInfo(String subscriptionId, UUID correlationId )
    {
       /* String query = "INSERT INTO AnalyticsInfo VALUES(?, ?, ?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, collectorDataModel.getSnSaai());
                preparedStatement.setInt(2, collectorDataModel.getLOAD_LEVEL_INFORMATION());
                preparedStatement.setInt(3, collectorDataModel.getEventId());
                return preparedStatement.execute();
            }
        });*/

       //INSERT INTO Student (ROLL_NO, NAME, Age) VALUES (‘5′,’PRATIK’,’19’);
        
       String query = "insert into subTable (subscriptionId, correlationId ) values (?,?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, subscriptionId);
                preparedStatement.setString(2, String.valueOf(correlationId));

               // preparedStatement.setInt(2, unCorrId);
                return preparedStatement.execute();
            }
        });
    }


    public Integer updateCollectorDetails(CollectorDataModel collectorDataModel)
    {
        String query = "UPDATE AnalyticsInfo SET snSaai = ?, load_level_info = ? WHERE event_id = ?";

        Object[] parameters = { collectorDataModel.getSnSaai(), collectorDataModel.getLOAD_LEVEL_INFORMATION(), collectorDataModel.getEventId()  };
        int[] types = {Types.VARCHAR, Types.INTEGER, Types.INTEGER };

        return jdbcTemplate.update(query, parameters, types);
    }


    public Integer deleteCollectorByEventId(Integer id)
    { return jdbcTemplate.update("DELETE FROM AnalyticsInfo WHERE event_id = ?", id); }


    public int updatesubTableWithunSubCorrealtionId(String response, UUID correlationId) {

        String query = "UPDATE subTable SET unSubCorrelationId = ? WHERE correlationId = ?";

        Object[] parameters = { response,correlationId };
        int[] types = {Types.VARCHAR, Types.VARCHAR };

        return jdbcTemplate.update(query, parameters, types);


    }

    public void unSubscribeEventViaUnSubCorrelationId(UUID unSubCorrelationId) {

        String query = "delete from subTable where unSubCorrelationId=?";
        Object[] parameters = { unSubCorrelationId };
        int[] types = {Types.VARCHAR };
        jdbcTemplate.update(query, parameters, types);

    }
}
