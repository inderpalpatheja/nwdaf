package com.nwdaf.Analytics;

import com.nwdaf.Analytics.Mapper.eventTableMapper;
import com.nwdaf.Analytics.NwdafMapper.SliceLoadLevelInformationMapper;
import com.nwdaf.Analytics.NwdafMapper.SliceLoadLevelSubscriptionDataMapper;
import com.nwdaf.Analytics.NwdafMapper.SubscriptionTableMapper;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelInformationModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelSubscriptionDataModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelSubscriptionTableModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSubscriptionTableModel;
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


    public Boolean subscribeNF(NnwdafEventsSubscription nnwdafEventsSubscription) {
        //String query = "INSERT INTO eventTable VALUES('" + id + "', ?, ?, ?, ?, ?);";
        // String query = "INSERT INTO nwdafSubscriptionTable VALUES('" + id + "', ?, ?, ?, ?, ?)";

        String query = "INSERT INTO nwdafSubscriptionTable VALUES(?, ?, ?, ?, ?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, nnwdafEventsSubscription.getSubscriptionID());
                preparedStatement.setInt(2, nnwdafEventsSubscription.getEventID());
                preparedStatement.setString(3, nnwdafEventsSubscription.getNotificationURI());
                preparedStatement.setInt(4, nnwdafEventsSubscription.getNotifMethod());
                preparedStatement.setInt(5, nnwdafEventsSubscription.getRepetitionPeriod());
                return preparedStatement.execute();
            }
        });
    }

    public SliceLoadLevelSubscriptionDataMapper findById(String id) {
        //  String query = "SELECT * FROM eventTable WHERE subscriptionID = ?";
        String query = "select * from nwdafSubscriptionDataTable where subscriptionID = ?";
        try {
            return (SliceLoadLevelSubscriptionDataMapper) this.jdbcTemplate.queryForObject(query, new Object[]{id}, new SliceLoadLevelSubscriptionDataMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    public Integer updateNF(NnwdafEventsSubscription user, String id) {
        String UPDATE_QUERY = "UPDATE nwdafSubscriptionTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ?, loadLevelThreshold = ? WHERE subscriptionID = ?";

        return jdbcTemplate.update(UPDATE_QUERY, user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), user.getLoadLevelThreshold(), id);


    }


    public Integer unsubscribeNF(String id) {
        jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = ?", id);
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


   /* public List<NnwdafEventsSubscription> getALLSubID(int eventID) {

        //return jdbcTemplate.query("select subscriptionID from eventTable where eventId = ?" + eventID,this );

        return (List<NnwdafEventsSubscription>) this.jdbcTemplate.query("SELECT *from nwdafSubscriptionTable WHERE eventID = ?", new Object[]{eventID}, new eventTableMapper());
    }*/


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

        if (anySlice == true) {
            String s = "select *From nwdafSliceLoadLevelInformation";
            return jdbcTemplate.query(s, new analyticsRowMapper());
        }

        String s = "select *From nwdafSliceLoadLevelInformation where snssais = '" + snssais + "';";
        return jdbcTemplate.query(s, new analyticsRowMapper());

    }

    //  String s = "Select * from nwdafSliceLoadLevelInformation  where snssais = '" + snssais + "' AND anySlice =" + anySlice;

    //  String s = "Select *From nwdafSliceLoadLevelInformation where snssais = '" + snssais;

    //String s = "SELECT *from nwdafLoadLevelInformation WHERE snssais='" + snssais + "' AND  anySlice =' + anySlice + '";
    //System.out.println("\n\n\n\ntest query String is " + s);

    //return jdbcTemplate.query(s, new analyticsRowMapper());
    //return jdbcTemplate.query("SELECT *from nwdafLoadLevelInformation WHERE snssais='xyz' AND  anySlice ='1'",new analyticsRowMapper());


    public Boolean saveData(events_connection c) {
        // String query = "INSERT INTO load_level_information (snssais,anySlice,subscriptionID, load_level_info) VALUES(?,?, '" + c.getSubscriptionID() + "', " + String.valueOf(30) + ")";
        String query = "INSERT INTO nwdafLoadLevelInformation (snssais, currentLoadLevel) VALUES(?," + String.valueOf(0) + ")";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, c.getSnssais());

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


    public Boolean addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(UUID correationId, String unSubCorrelationID, String snssais) {


        String query = "insert into nwdafSliceLoadLevelSubscriptionTable (snssais,subscriptionID,correlationID) values (?,?,?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, snssais);
                preparedStatement.setString(2, unSubCorrelationID);
                preparedStatement.setString(3, String.valueOf(correationId));

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

    public Boolean addDataIntoLoadLevelInformaitionTable(String snssais) {

        //  String query = "INSERT INTO nwdafLoadLevelInformation (snssais,anySlice, currentLoadLevelInfo ) VALUES(?,?, '" + c.getSubscriptionID() + "', " + String.valueOf(30) + ")";
        //  String query = "insert into nwdafLoadLevelInformation (snssais,anySlice) values ('" + snssais +"',"+ anySlice + ")";

        //  String query = "INSERT INTO nwdafLoadLevelInformation (snssais,currentLoadLevel) VALUES('" + snssais +  "," + currentLoadLevel +  "')";
        String query = "INSERT INTO nwdafSliceLoadLevelInformation (snssais,currentLoadLevel) VALUES(?,?) on duplicate key update snssais = snssais";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, snssais);
                preparedStatement.setInt(2, 0);
                // preparedStatement.setString(4, correlationID);


                return preparedStatement.execute();
            }
        });

    }

    public List<NwdafSliceLoadLevelInformationModel> getALLsnssais() {
        // String query = "Select snssais from nwdafSliceLoadLevelInformation";
        String query = "Select *from nwdafSliceLoadLevelInformation";
        return jdbcTemplate.query(query, new SliceLoadLevelInformationMapper());
    }

    public List<NwdafSliceLoadLevelSubscriptionDataModel> getAllSubIdsbysnssais(String snssais) {

        String query = "select  *from nwdafSliceLoadLevelSubscriptionData where snssais ='" + snssais + "'";


        return jdbcTemplate.query(query, new SliceLoadLevelSubscriptionDataMapper());

    }

    public Integer currentLoadLevel(String snssais) {

        String query = "SELECT currentLoadLevel FROM nwdafSliceLoadLevelInformation WHERE snssais = '" + snssais + "';";

        return jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("currentLoadLevel");
            }
        });
    }


    public Boolean addDataIntoNwdafSliceLoadLevelSubscriptionDataTable(NwdafSliceLoadLevelSubscriptionDataModel sliceLoadLevel) {
        String query = "INSERT INTO nwdafSliceLoadLevelSubscriptionData VALUES(null, ?, ?, ?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, sliceLoadLevel.getSubscriptionID());
                preparedStatement.setString(2, sliceLoadLevel.getSnssais());
                preparedStatement.setInt(3, sliceLoadLevel.getLoadLevelThreshold());

                return preparedStatement.execute();
            }
        });

    }


    public NwdafSubscriptionTableModel findById_subscriptionID(String subID) {
        String query = "SELECT * FROM nwdafSubscriptionTable WHERE subscriptionID = ?";

        try {
            return (NwdafSubscriptionTableModel) this.jdbcTemplate.queryForObject(query, new Object[]{subID}, new SubscriptionTableMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    public String getNotificationURI(String subID) {
        String query = "SELECT notificationURI FROM nwdafSubscriptionTable WHERE subscriptionID = '" + subID + "';";

        return jdbcTemplate.queryForObject(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("notificationURI");
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
}
