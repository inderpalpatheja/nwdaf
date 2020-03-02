package com.nwdaf.Analytics.Repository;

import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.TableType.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.SubscriptionTable;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Repository.Mapper.AnalyticsRowMapper;
import com.nwdaf.Analytics.Repository.Mapper.*;
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
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Repository
public class Nnwdaf_Repository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Random  random = new Random();




    public Boolean subscribeNF(NnwdafEventsSubscription nnwdafEventsSubscription) {

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



    public Integer updateNF(NnwdafEventsSubscription user, String id) {

        // Updating nwdafSubscriptionTable
        String updateSubscriptionTable = "UPDATE nwdafSubscriptionTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ? WHERE subscriptionID = ?";
        jdbcTemplate.update(updateSubscriptionTable, new Object[] { user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), id });


        // Updating nwdafSliceLoadLevelSubscriptionData
        String updateLoadLevelSubData = "UPDATE nwdafSliceLoadLevelSubscriptionData SET loadLevelThreshold = ? WHERE subscriptionID = ?";
        jdbcTemplate.update(updateLoadLevelSubData, new Object[] { user.getLoadLevelThreshold(), id });


        return 1;
    }



    public Integer unsubscribeNF(String id) throws Exception {

        String snssais = getSnssais(id);

        jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = ?", id);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionID = ?", id);

        return decrementRefCount(snssais);
    }




    public List<EventConnection> checkForData(String snssais, Boolean anySlice) {

        if (anySlice == true) {
            String s = "select *From nwdafSliceLoadLevelInformation";
            try {
                return jdbcTemplate.query(s, new AnalyticsRowMapper());
            }
            catch (EmptyResultDataAccessException e){
                return null;
            }
        }

        String s = "select *From nwdafSliceLoadLevelInformation where snssais = '" + snssais + "';";
        try {
            return jdbcTemplate.query(s, new AnalyticsRowMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }

    }







    public Boolean addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(SliceLoadLevelSubscriptionTable slice, boolean getAnalytics) {



            if (snsExists(slice.getSnssais())) {
                jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", slice.getSnssais());
                return true;
            }



        String query = "INSERT INTO nwdafSliceLoadLevelSubscriptionTable (snssais,subscriptionID,correlationID,refCount) VALUES (?,?,?,?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, slice.getSnssais());
                preparedStatement.setString(2, slice.getSubscriptionID());
                preparedStatement.setString(3, slice.getCorrelationID());

                if(getAnalytics)
                { preparedStatement.setInt(4, 0); }

                else
                { preparedStatement.setInt(4, 1); }

                return preparedStatement.execute();
            }
        });

    }




    public Boolean add_data_into_load_level_table(String snssais) {

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



    public List<SliceLoadLevelInformation> getALLsnssais() {

        String query = "SELECT * FROM nwdafSliceLoadLevelInformation";
        try {
            return jdbcTemplate.query(query, new SliceLoadLevelInformationMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }




    public List<SliceLoadLevelSubscriptionData> getAllSubIdsbysnssais(String snssais) {

        String query = "SELECT * FROM nwdafSliceLoadLevelSubscriptionData WHERE snssais ='" + snssais + "'";


       try {
           return jdbcTemplate.query(query, new SliceLoadLevelSubscriptionDataMapper());
       }
       catch(EmptyResultDataAccessException e){

           return null;
       }

    }




    public Integer currentLoadLevel(String snssais) {

        String query = "SELECT currentLoadLevel FROM nwdafSliceLoadLevelInformation WHERE snssais = '" + snssais + "';";
        try {

            Integer currentLoad=jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("currentLoadLevel");
                }
            });
            return currentLoad;
        }
        catch (EmptyResultDataAccessException e){

            return 0;
        }
    }


    public Boolean addDataIntoNwdafSliceLoadLevelSubscriptionDataTable(SliceLoadLevelSubscriptionData sliceLoadLevel) {
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


    public SubscriptionTable findById_subscriptionID(String subID) {
        String query = "SELECT * FROM nwdafSubscriptionTable WHERE subscriptionID = ?";

        try {

            return (SubscriptionTable) this.jdbcTemplate.queryForObject(query, new Object[]{subID}, new SubscriptionTableMapper());
        } catch (EmptyResultDataAccessException ex) {

            return null;
        }
    }



    public String getNotificationURI(String subID) {
        String query = "SELECT notificationURI FROM nwdafSubscriptionTable WHERE subscriptionID = '" + subID + "';";

        try {

            String notificationURI= jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("notificationURI");
                }
            });
            return notificationURI;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }




    public boolean snsExists(String snssais)
    {
        String query = "SELECT IFNULL ((SELECT snssais FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = '" + snssais + "'), null) AS snssais;";


            String result = jdbcTemplate.queryForObject(query, new RowMapper<String>() {

                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("snssais");
                }
            });

            return (result != null);

    }


    public String getSnssaisViaSubID(String correlationID) {

        //String query = "select snssais from nwdafSliceLoadLevelSubscriptionTable where subscriptionID "
        String query = "SELECT snssais FROM nwdafSliceLoadLevelSubscriptionTable WHERE subscriptionID = '" + correlationID + "';";

        try {

            return jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("snssais");
                }
            });
        }

        catch (EmptyResultDataAccessException e){

            return null;
        }

    }


    public Integer increment_ref_count(String snssais)
    { return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", snssais);  }


    // 14 feb update

    public Integer decrementRefCount(String snssais) throws Exception {
        jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount - 1 WHERE snssais = ?", snssais);

       // System.out.println("check1");
        if(getRefCount(snssais) == 0)
        {
             /* time to delete the entry from db, caller shall send the subscribe message towards peer first */
             return 0;
        }

        return 1;
    }


    public void RemoveNwDafSubscriptionEntry(String snssais)
    {

        jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = ?", snssais);
    }


    public String getUnSubCorrelationID(String snssais) {

        //String query = "select snssais from nwdafSliceLoadLevelSubscriptionTable where subscriptionID "
        String query = "SELECT subscriptionID FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = '" + snssais + "';";
        try {

            String unSubID=jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("subscriptionID");
                }
            });
            return unSubID;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }



    }

    public Integer incrementRefCount(String snssais)
    {
        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", snssais);
    }





    public String getSnssais(String subID)
    {
        String query = "SELECT snssais FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = '" + subID  +"';";

        try {

            String snssais1=jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("snssais");
                }
            });
            return snssais1;
        }
        catch (EmptyResultDataAccessException e){

            return null;
        }
    }


    public Integer getRefCount(String snssais)
    {
        String query = "SELECT refCount FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = '" + snssais + "';";

        try {

            Integer refCount1=jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("refCount");
                }
            });
            return refCount1;
        }
        catch(EmptyResultDataAccessException e){

            return 0;
        }
    }


    public Integer updateCurrentLoadLevel(int loadLevel, String snssais)
    {

        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelInformation SET currentLoadLevel = ? WHERE snssais = ?", new Object[] { loadLevel, snssais });
    }



    public String getSnssaisByCorrelationID(String correlationID)
    {
        String query = "SELECT snssais FROM nwdafSliceLoadLevelSubscriptionTable WHERE correlationID = '" + correlationID + "';";

        try {

            String s = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("snssais");
                }
            });
            return s;
        }
        catch(EmptyResultDataAccessException e){

            return null;
        }
    }




    public Integer getLoadLevelThreshold(String subID)
    {
        String query = "SELECT loadLevelThreshold FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = '" + subID + "';";

        try {

            Integer loadThreshold = jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("loadLevelThreshold");
                }
            });
            return loadThreshold;
        }
        catch(EmptyResultDataAccessException e){
            return 0;
        }
    }


    public Integer getCurrentLoadLevel(String snssais)
    {
        String query = "SELECT currentLoadLevel FROM nwdafSliceLoadLevelInformation WHERE snssais = '" + snssais + "';";

        try {

            Integer currentLoad= jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("currentLoadLevel");
                }
            });
            return currentLoad;
        }
        catch (EmptyResultDataAccessException e){

            return 0;
        }
    }


    public Object findby_snssais(String snssais1) {

        String query = "SELECT * FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais=?";

        try {
            Object obj= (SliceLoadLevelSubscriptionTable) this.jdbcTemplate.queryForObject(query, new Object[]{snssais1}, new SliceLoadLevelSubscriptionTableMapper());

             return obj;
        }
        catch (EmptyResultDataAccessException ex) {

            return null;
        }
    }


    public String getCorrelationID(String mCorrelationID) {

        String query = "SELECT correlationID FROM nwdafSliceLoadLevelSubscriptionTable WHERE subscriptionID = '" + mCorrelationID + "';";

         try {

             String correlationID = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                 @Override
                 public String mapRow(ResultSet resultSet, int i) throws SQLException {
                     return resultSet.getString("correlationID");
                 }
             });
             return correlationID;
         } catch (EmptyResultDataAccessException e) {

             return null;
         }
    }


}