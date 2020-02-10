package com.nwdaf.Analytics;

import com.nwdaf.Analytics.Mapper.eventTableMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Repository
public class Nnwdaf_repository {

    @Autowired
    JdbcTemplate jdbcTemplate;


    List<NnwdafEventsSubscription> nnwdafEventsSubscriptionList = new ArrayList<>();

    Random random = new Random();


    public Boolean subscribeNF(NnwdafEventsSubscription user) {
        //String query = "INSERT INTO eventTable VALUES('" + id + "', ?, ?, ?, ?, ?);";
        // String query = "INSERT INTO nwdafSubscriptionTable VALUES('" + id + "', ?, ?, ?, ?, ?)";

        String query = "INSERT INTO nwdafSubscriptionTable VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, user.getSubscriptionID());
                preparedStatement.setInt(2, user.getEventID());
                preparedStatement.setString(3, user.getNotificationURI());
                preparedStatement.setString(4, user.getSnssais());
                preparedStatement.setBoolean(5, user.isAnySlice());
                preparedStatement.setInt(6, user.getNotifMethod());
                preparedStatement.setInt(7, user.getRepetitionPeriod());
                preparedStatement.setInt(8, user.getLoadLevelThreshold());

                return preparedStatement.execute();
            }
        });
    }

    public NnwdafEventsSubscription findById(String id) {
        //  String query = "SELECT * FROM eventTable WHERE subscriptionID = ?";
        String query = "select * from nwdafSubscriptionTable where subscriptionID = ?";
        try {
            return (NnwdafEventsSubscription) this.jdbcTemplate.queryForObject(query, new Object[]{id}, new eventTableMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    public Integer updateNF(NnwdafEventsSubscription user, String id) {
        String UPDATE_QUERY = "UPDATE nwdafSubscriptionTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ?, loadLevelThreshold = ? WHERE subscriptionID = ?";

        return jdbcTemplate.update(UPDATE_QUERY, user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), user.getLoadLevelThreshold(), id);


    }


    public Integer unsubscribeNF(String id) {
        return jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionID = ?", id);
    }


    public NnwdafEventsSubscription findDataByuSubId(String subId) {

        String query = "select *from nwdafLoadLevelInformation where subscriptionID= ?";

        try {
            return (NnwdafEventsSubscription) this.jdbcTemplate.queryForObject(query, new Object[]{subId}, new loadLevelMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }


    public List<NnwdafEventsSubscription> getALLSubID(int eventID) {

        //return jdbcTemplate.query("select subscriptionID from eventTable where eventId = ?" + eventID,this );

        return (List<NnwdafEventsSubscription>) this.jdbcTemplate.query("SELECT *from nwdafSubscriptionTable WHERE eventID = ?", new Object[]{eventID}, new eventTableMapper());
    }


    public Boolean addSubscriptionIdToLoadLevelInfo(NnwdafEventsSubscription nnwdafEventsSubscription, UUID subscriptionID, UUID correlationID) {

        String query = "INSERT INTO nwdafLoadLevelInformation (snssais,anySlice,currentLoadLevelInfo,subscriptionID,correlationID) VALUES(?,?," + 0 + ",'" + subscriptionID + "', '" + correlationID + "')";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

              //  preparedStatement.setString(1, nnwdafEventsSubscription.getSnssais());
               // preparedStatement.setBoolean(2, nnwdafEventsSubscription.isAnySlice());

                String s1 = "xyz";
                boolean s2 = true;
                  preparedStatement.setString(1, s1);
                 preparedStatement.setBoolean(2, s2);

                return preparedStatement.execute();
            }
        });
    }


    public List<events_connection> getData(String snssais, Boolean anySlice) {

        String s = "Select * from nwdafLoadLevelInformation  where snssais = '" + snssais + "' AND anySlice =" + anySlice;

        //String s = "SELECT *from nwdafLoadLevelInformation WHERE snssais='" + snssais + "' AND  anySlice =' + anySlice + '";
        System.out.println("\n\n\n\ntest query String is " + s);

        return jdbcTemplate.query(s, new analyticsRowMapper());
    //return jdbcTemplate.query("SELECT *from nwdafLoadLevelInformation WHERE snssais='xyz' AND  anySlice ='1'",new analyticsRowMapper());
    }


    public Boolean saveData(events_connection c) {
        // String query = "INSERT INTO load_level_information (snssais,anySlice,subscriptionID, load_level_info) VALUES(?,?, '" + c.getSubscriptionID() + "', " + String.valueOf(30) + ")";
        String query = "INSERT INTO nwdafLoadLevelInformation (snssais,anySlice,subscriptionID, load_level_info) VALUES(?,?, '" + c.getSubscriptionID() + "', " + String.valueOf(30) + ")";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                //preparedStatement.setInt(1,c.getEvent_id());
                preparedStatement.setString(1, c.getSnssais());
                preparedStatement.setBoolean(2, c.isAnySlice());


                return preparedStatement.execute();
            }
        });
    }



  /*  public Boolean addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(UUID correationId, StringBuffer response) {


        String query = "insert into nwdafIDTable (correlationID,unSubCorrelationID ) values (?,?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, String.valueOf(correationId));
                preparedStatement.setString(2, String.valueOf(response));

                return preparedStatement.execute();
            }
        });

    }
    */


    public Boolean addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(UUID correationId, String unSubCorrelationID) {


        String query = "insert into nwdafIDTable (correlationID,unSubCorrelationID) values (?,?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, String.valueOf(correationId));
                preparedStatement.setString(2, unSubCorrelationID);

                return preparedStatement.execute();
            }
        });

    }



    public Integer deleteDataById(int id) {
        return jdbcTemplate.update("DELETE FROM nwdafLoadLevelInformation WHERE id = ?", id);
    }


    public int updateNwdafIDTableWithunSubCorrealtionId(UUID correlationID, String unSubCorrelationID) {

        String query = "UPDATE nwdafIDTable SET unSubCorrelationID = ? WHERE correlationID = ?";

        Object[] parameters = {correlationID, unSubCorrelationID};
        int[] types = {Types.VARCHAR, Types.VARCHAR};

        return jdbcTemplate.update(query, parameters, types);


    }

    public Boolean addSnssaisAndAnySliceIntoLoadLevelInfo(String snssais, Boolean anySlice, String correlationID) {

        //  String query = "INSERT INTO nwdafLoadLevelInformation (snssais,anySlice, currentLoadLevelInfo ) VALUES(?,?, '" + c.getSubscriptionID() + "', " + String.valueOf(30) + ")";
        //  String query = "insert into nwdafLoadLevelInformation (snssais,anySlice) values ('" + snssais +"',"+ anySlice + ")";

        String query = "INSERT INTO nwdafLoadLevelInformation (snssais,anySlice,currentLoadLevelInfo,subscriptionID,correlationID) VALUES('" + snssais + "'," + anySlice + "," + 0 + ",'" + 0 + "', '" + correlationID + "')";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                //preparedStatement.setInt(1,c.getEvent_id());
                //  preparedStatement.setString(1, c.getSnssais());
                // preparedStatement.setString(4, correlationID);


                return preparedStatement.execute();
            }
        });

    }

   /* public List<UUID> getAlLSubIdsFromLoadLevelTable() {

        String query = "select subscriptionID from nwdafLoadLevelInformation";

      //  return jdbcTemplate.query("SELECT *from nwdafLoadLevelInformation WHERE snssais='" + sn + "' AND  subscriptionID ='" + sub + "'", new analyticsRowMapper());


        return jdbcTemplate.queryForList(
                query, new ArrayList<UUID>);
       //return  (List<UUID>)jdbcTemplate.execute(query);
    }*/





    // New code from Nikhil 10/02/2020
   public Boolean addLoadLevelData(String snssais)
   {
       String query = "INSERT INTO nwdafLoadLevelInformation VALUES(?, ?) ON DUPLICATE KEY UPDATE snssais = snssais;";

       return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

           @Override
           public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

               preparedStatement.setString(1, snssais);
               preparedStatement.setInt(2, 0);

               return preparedStatement.execute();
           }
       });
   }


   public String getSnssais(String correlationID) {

       String query = "SELECT snssais FROM nwdafIDTable WHERE correlationID = '" + correlationID + "';";

       return jdbcTemplate.queryForObject(query, new RowMapper<String>() {

           @Override
           public String mapRow(ResultSet resultSet, int i) throws SQLException {
               return resultSet.getString("snssais");
           }
       });
   }


   public Integer updateLoadLevelBySnssais(String snssais, int updateVal)
   { return jdbcTemplate.update("UPDATE nwdafLoadLevelInformation SET currentLoadLevel = ? WHERE snssais = ?", new Object[] { updateVal, snssais }); }



}
