package com.nwdaf.Analytics;

import com.nwdaf.Analytics.Mapper.Events_connectionRowMapper;
import com.nwdaf.Analytics.Mapper.eventTableMapper;
import com.nwdaf.Analytics.model.LOAD_LEVEL_INFORMATION;
import com.nwdaf.Analytics.model.SubscrptionIdModel;
import com.nwdaf.Analytics.model.analytics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<NnwdafEventsSubscription> getAllNFs() {
        return jdbcTemplate.query("SELECT subscriptionID, eventID, notificationURI, notifMethod, repetitionPeriod, loadLevelThreshold FROM nwdafSubscriptionTable", new eventTableMapper());
    }


    public Boolean subscribeNF(NnwdafEventsSubscription user, UUID id) {
        //String query = "INSERT INTO eventTable VALUES('" + id + "', ?, ?, ?, ?, ?);";
        String query = "INSERT INTO nwdafSubscriptionTable VALUES('" + id + "', ?, ?, ?, ?, ?,?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setInt(1, user.getEventID());
                preparedStatement.setString(2, user.getNotificationURI());
                preparedStatement.setInt(3, user.getNotifMethod());
                preparedStatement.setInt(4, user.getRepetitionPeriod());
                preparedStatement.setInt(5, user.getLoadLevelThreshold());
                preparedStatement.setInt(6, 1);

                return preparedStatement.execute();
            }
        });
    }


    public Integer addEventDataID(int eventDataID, UUID subscriptionID) {

        return jdbcTemplate.update("UPDATE nwdafSubscriptionTable SET eventDataID = ? WHERE subscriptionID = ?", new Object[]{eventDataID, String.valueOf(subscriptionID)});

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

        //String query = "UPDATE eventTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ?, loadLevelThreshold = ? WHERE subscriptionID = '" + id + "'";


        //    Object[] parameters = { user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), user.getLoadLevelThreshold() };
        //  int[] types = { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };

        //  return jdbcTemplate.update(query, parameters, types);
    }


    public Integer unsubscribeNF(String id) {
        return jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionID = ?", id);
    }



/* NIKHIL'S CODE JAN 30, 2020

    public List<NnwdafEventsSubscription> getAllNFs() {
        return jdbcTemplate.query("SELECT subscriptionID, eventID, notificationURI, notifMethod, repetitionPeriod, loadLevelThreshold FROM eventTable", this);
    }


    public Boolean subscribeNF(NnwdafEventsSubscription user, UUID id) {
        String query = "INSERT INTO eventTable VALUES('" + id + "', ?, ?, ?, ?, ?, null);";

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


    public Integer addCorrelationData(UUID subscriptionID, EventID eventID, UUID correlation_id) {
        return jdbcTemplate.update("UPDATE eventTable SET correlationID = ? WHERE subscriptionID = ?", new Object[]{String.valueOf(correlation_id), String.valueOf(subscriptionID)});

    }


    public NnwdafEventsSubscription findById(String id) {
        String query = "SELECT * FROM eventTable WHERE subscriptionID = ?";

        try {
            return (NnwdafEventsSubscription) this.jdbcTemplate.queryForObject(query, new Object[]{id}, this);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
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


    public Integer updateNF(NnwdafEventsSubscription user, String id) {
        String UPDATE_QUERY = "UPDATE eventTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ?, loadLevelThreshold = ? WHERE subscriptionID = ?";

        return jdbcTemplate.update(UPDATE_QUERY, user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), user.getLoadLevelThreshold(), id);

        //String query = "UPDATE eventTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ?, loadLevelThreshold = ? WHERE subscriptionID = '" + id + "'";


        //    Object[] parameters = { user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), user.getLoadLevelThreshold() };
        //  int[] types = { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };

        //  return jdbcTemplate.update(query, parameters, types);
    }


    public Integer unsubscribeNF(String id) {
        return jdbcTemplate.update("DELETE FROM eventTable WHERE subscriptionID = ?", id);
    }


    public UUID getCorrelationID(UUID subscriptionID, EventID eventID) {
        return UUID.randomUUID();
    }
    */

    /////Sadaf's Code//////

    public List<analytics> getUser() {
        //return jdbcTemplate.query("SELECT snssais, load_level_info, eventId from t1;", new UserRowMapper());
        return jdbcTemplate.query("SELECT snssais, load_level_info, eventId from t2", new analyticsRowMapper());
    }


    public analytics findById(Integer id) {
        String query = "SELECT * FROM t2 WHERE eventId = ?";

        try {
            return (analytics) this.jdbcTemplate.queryForObject(query, new Object[]{id}, new analyticsRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    public int getLoadLevelInformation(String subId) {

        return jdbcTemplate.update("select currentLoadLevelInfo from nwdafLoadLevelInformation where subscriptionID = ", subId);

    }

    public NnwdafEventsSubscription findDataByuSubId(String subId) {

        String query = "select *from nwdafLoadLevelInformation where subscriptionID= ?";

        try {
            return (NnwdafEventsSubscription) this.jdbcTemplate.queryForObject(query, new Object[]{subId}, new loadLevelMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }


    public NnwdafEventsSubscription findNFURI(String subId) {

        String query = "select *from nwdafSubscriptionTable where subscriptionID= ?";

        try {
            return (NnwdafEventsSubscription) this.jdbcTemplate.queryForObject(query, new Object[]{subId}, new eventTableMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }


    public List<NnwdafEventsSubscription> getALLSubID(int eventID) {

        //return jdbcTemplate.query("select subscriptionID from eventTable where eventId = ?" + eventID,this );

        return (List<NnwdafEventsSubscription>) this.jdbcTemplate.query("SELECT *from nwdafSubscriptionTable WHERE eventID = ?", new Object[]{eventID}, new eventTableMapper());
    }


    public Boolean addSubscriptionIdToLoadLevelInfo(NnwdafEventsSubscription nnwdafEventsSubscription, UUID subscriptionID) {

        //   String query = "INSERT INTO load_level_information VALUES(?,?,?,?,?,'" + subscriptionID + "')";

        // String query = "INSERT INTO load_level_information (snssais,anySlice,subscriptionID, load_level_info) VALUES(?,?, '" + subscriptionID + "', " + 199 + ")";

        String query = "INSERT INTO nwdafLoadLevelInformation (snssais,anySlice,currentLoadLevelInfo,subscriptionID) VALUES(?,?," + 0 + ",'" + subscriptionID + "')";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, nnwdafEventsSubscription.getSnssais());
                preparedStatement.setBoolean(2, nnwdafEventsSubscription.isAnySlice());


                return preparedStatement.execute();
            }
        });
    }

// sadaf 4 feb start
    /*
    public List<events_connection> getData(String sn, String sub) {

        return jdbcTemplate.query("SELECT * from nwdafLoadLevelInformation WHERE snssais='" + sn + "' AND  subscriptionID ='" + sub + "'", new analyticsRowMapper());
    }
sadaf 4 feb ends
*/
public List<events_connection> getData(String sn, String sub) {

    return jdbcTemplate.query("SELECT *from nwdafLoadLevelInformation WHERE snssais='" + sn + "' AND  subscriptionID ='" + sub + "'", new analyticsRowMapper());
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


	     /*public Integer deleteUserById(Integer event_id)
	      { return jdbctemplate.update("DELETE FROM load_level_information WHERE event_id = ?", event_id); }*/

    public Integer deleteDataById(int id) {
        return jdbcTemplate.update("DELETE FROM nwdafLoadLevelInformation WHERE id = ?", id);
    }


    //this update function is not working currently!
	    /* public Integer updateUser(events_connection c)
	     {
	         String query = "UPDATE load_level_information SET snssais = ?, anySlice = ? where event_id=?";
	         Object[] parameters = {c.getSnssais(), c.isAnySlice()};
	         int[] types = {Types.VARCHAR(45),Types.BOOLEAN,};
	         return jdbctemplate.update(query, parameters, types);
	     }*/


    // END OF LOAD_LEVEL_INFORMATION
    //
    public events_connection findBySnssais(String snssais) {

        String query = "SELECT *FROM nwdafLoadLevelInformation WHERE snssais= ?";

        try {
            return (events_connection) this.jdbcTemplate.queryForObject(query, new Object[]{snssais}, new Events_connectionRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }


    }


    public void getSubscriptionCount() {

//        List<String> list = jdbcTemplate.execute("select subscriptionID from nwdafSubscriptionTable",ne);

        /*
        nnwdafEventsSubscriptionList = jdbcTemplate.query("SELECT subscriptionID, eventID, notificationURI, notifMethod, repetitionPeriod, loadLevelThreshold FROM nwdafSubscriptionTable", new eventTableMapper());

        if (nnwdafEventsSubscriptionList.size() == 1) {

            String query = "insert into nwdafCounterTable value (?, ?,?,?)";
            jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

                @Override
                public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws
                        SQLException, DataAccessException {
                    //preparedStatement.setInt(1,c.getEvent_id());
                    preparedStatement.setString(1, "subscriptionRequest");
                    preparedStatement.setInt(2, nnwdafEventsSubscriptionList.size());
                    preparedStatement.setString(3, "subscriptionResponse");
                    preparedStatement.setInt(4, 0);

                    return preparedStatement.execute();
                }
            });
        } else {
            jdbcTemplate.update("select nwdafCounterTable set requestType = 'subscriptionRequest' set requestCount = " + nnwdafEventsSubscriptionList.size() + ")");
        }*/


        // in demo2branch


    }

}
