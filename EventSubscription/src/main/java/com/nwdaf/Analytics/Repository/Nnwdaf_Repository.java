package com.nwdaf.Analytics.Repository;

import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnectionForUEMobility;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.QosSustainabilityData.QosSustainabilityInfo;
import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.NotificationData;
import com.nwdaf.Analytics.Model.QosNotificationData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilityInformation;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionData;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.UEmobilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UserLocation;
import com.nwdaf.Analytics.Repository.Mapper.AnalyticsRowMapper;
import com.nwdaf.Analytics.Repository.Mapper.*;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SliceLoadLevelInformationMapper;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SliceLoadLevelSubscriptionDataMapper;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SliceLoadLevelSubscriptionTableMapper;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SubscriptionTableMapper;
import com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper.QosSustainabilityInformationMapper;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionModel;
import com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper.AnalyticsRowMapperForUEMobility;
import com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper.nwdafUEmobilitySubscriptionTableMapper;
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
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Repository
public class Nnwdaf_Repository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Random random = new Random();


    // To keep track of available snssais values in nwdafSliceLoadLevelSubscriptionTable
    HashSet<String> snssais_in_sliceLoadLevelSubscriptionTable = new HashSet<>();


    // To keep track of (plmnID + snssais) values in nwdafQosSustainabilitySubscriptionTable
    HashSet<String> plmnID_snssais_in_qosSustainabilitySubscriptionTable =  new HashSet<>();

    // To keep track of (plmnID + snssais) values in nwdafQosSustainabilityInformation
    HashSet<String> plmnID_snssais_in_qosSustainabilityInformation = new HashSet<>();



    public Boolean subscribeNF(NnwdafEventsSubscription nnwdafEventsSubscription, EventID eventID) throws NullPointerException {

        String query = "";

        if(eventID == EventID.LOAD_LEVEL_INFORMATION || eventID == EventID.UE_MOBILITY)
        { query = "INSERT INTO nwdafSubscriptionTable VALUES(?, ?, ?, ?, ?)"; }

        else if(eventID == EventID.QOS_SUSTAINABILITY)
        { query = "INSERT INTO nwdafSubscriptionTable (subscriptionID, eventID, notificationURI) VALUES(?, ?, ?)"; }


        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, nnwdafEventsSubscription.getSubscriptionID());
                preparedStatement.setInt(2, nnwdafEventsSubscription.getEventID());
                preparedStatement.setString(3, nnwdafEventsSubscription.getNotificationURI());

                if(eventID == EventID.LOAD_LEVEL_INFORMATION || eventID == EventID.UE_MOBILITY)
                {
                    preparedStatement.setInt(4, nnwdafEventsSubscription.getNotifMethod());
                    preparedStatement.setInt(5, nnwdafEventsSubscription.getRepetitionPeriod());
                }

                return preparedStatement.execute();
            }
        });
    }


    public Integer updateNF(NnwdafEventsSubscription user, String id) {

        // Updating nwdafSubscriptionTable
        String updateSubscriptionTable = "UPDATE nwdafSubscriptionTable SET eventID = ?, notifMethod = ?, repetitionPeriod = ? WHERE subscriptionID = ?";
        jdbcTemplate.update(updateSubscriptionTable, new Object[]{user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), id});


        // Updating nwdafSliceLoadLevelSubscriptionData
        String updateLoadLevelSubData = "UPDATE nwdafSliceLoadLevelSubscriptionData SET loadLevelThreshold = ? WHERE subscriptionID = ?";
        jdbcTemplate.update(updateLoadLevelSubData, new Object[]{user.getLoadLevelThreshold(), id});


        return 1;
    }




    public String unsubscribeNF_SliceLoadLevel(SubscriptionTable subscriber)
    {
        String snssais = getSnssais_SliceLoadLevel(subscriber.getSubscriptionID());

        jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = ?", subscriber.getSubscriptionID());
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionID = ?", subscriber.getSubscriptionID());

        return decrementRefCount_SliceLoadLevel(snssais);
    }


    public QosSustainabilitySubscriptionData unsubscribeNF_QosSustainability(SubscriptionTable subscriber)
    {
        QosSustainabilitySubscriptionData qosData = getPlmnID_Snssais_QosSustainability(subscriber.getSubscriptionID());

        jdbcTemplate.update("DELETE FROM nwdafQosSustainabilitySubscriptionData WHERE subscriptionID = ?", subscriber.getSubscriptionID());
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionID = ?", subscriber.getSubscriptionID());

        return decrementRefCount_QosSustainability(qosData);
    }





    public List<Object> checkForData(String snssais, Boolean anySlice, Integer eventID) {


        if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        {
            if (anySlice == true) {
                String s = "select * From nwdafSliceLoadLevelInformation";
                try {
                    return jdbcTemplate.query(s, new AnalyticsRowMapper());
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            String s = "select * From nwdafSliceLoadLevelInformation where snssais = '" + snssais + "';";
            try {
                return jdbcTemplate.query(s, new AnalyticsRowMapper());
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }


        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        {
            String query = "";

            if(anySlice)
            { query = "SELECT * FROM nwdafQosSustainabilityInformation"; }

            else
            { query = "SELECT * FROM nwdafQosSustainabilityInformation WHERE snssais = '" + snssais + "';"; }

            try
            { return jdbcTemplate.query(query, new QosAnalyticsMapper()); }

            catch(EmptyResultDataAccessException ex)
            { return null; }
        }

        return null;
    }


    public Boolean addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(SliceLoadLevelSubscriptionTable slice, boolean getAnalytics) {


        if (snssais_in_sliceLoadLevelSubscriptionTable.contains(slice.getSnssais())) {
            jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", slice.getSnssais());
            return true;
        }


        String query = "INSERT INTO nwdafSliceLoadLevelSubscriptionTable (snssais,subscriptionID,correlationID,refCount) VALUES (?,?,?,?);";

        snssais_in_sliceLoadLevelSubscriptionTable.add(slice.getSnssais());

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, slice.getSnssais());
                preparedStatement.setString(2, slice.getSubscriptionID());
                preparedStatement.setString(3, slice.getCorrelationID());

                if (getAnalytics) {
                    preparedStatement.setInt(4, 0);
                } else {
                    preparedStatement.setInt(4, 1);
                }

                return preparedStatement.execute();
            }
        });

    }


    public Boolean add_data_into_load_level_table(String snssais) throws NullPointerException {

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
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<SliceLoadLevelSubscriptionData> getAllSubIdsbysnssais(String snssais) {

        String query = "SELECT * FROM nwdafSliceLoadLevelSubscriptionData WHERE snssais ='" + snssais + "'";


        try {
            return jdbcTemplate.query(query, new SliceLoadLevelSubscriptionDataMapper());
        } catch (EmptyResultDataAccessException e) {

            return null;
        }

    }


    public Integer currentLoadLevel(String snssais) {

        String query = "SELECT currentLoadLevel FROM nwdafSliceLoadLevelInformation WHERE snssais = '" + snssais + "';";
        try {

            Integer currentLoad = jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("currentLoadLevel");
                }
            });
            return currentLoad;
        } catch (EmptyResultDataAccessException e) {

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


    public SubscriptionTable findById_subscriptionID(String subID) throws NullPointerException {
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

            String notificationURI = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("notificationURI");
                }
            });
            return notificationURI;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public boolean snsExistsSliceLoadLevelSubscriptionTable(String snssais) {
        return snssais_in_sliceLoadLevelSubscriptionTable.contains(snssais);
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
        } catch (EmptyResultDataAccessException e) {

            return null;
        }

    }


    public Integer increaseRefCountSliceLoadLevel(String snssais) {
        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", snssais);
    }


    // 14 feb update


    public String decrementRefCount_SliceLoadLevel(String snssais) {

        jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount - 1 WHERE snssais = ?", snssais);

        if (getRefCount_SliceLoadLevel(snssais) == 0) {
            /* time to delete the entry from db, caller shall send the subscribe message towards peer first */
            return snssais;
        }

        return null;
    }


    public QosSustainabilitySubscriptionData decrementRefCount_QosSustainability(QosSustainabilitySubscriptionData qosData)
    {
        jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionTable SET refCount = refCount - 1 WHERE plmnID = ? AND snssais = ?;", new Object[] {qosData.getPlmnID(), qosData.getSnssais()});

        if(getRefCount_QosSustainability(qosData.getPlmnID(), qosData.getSnssais()) == 0)
        {  return qosData; }

        return null;
    }




    public Integer deleteEntry_SliceLoadLevelSubscriptionTable(String snssais) throws NullPointerException {

        snssais_in_sliceLoadLevelSubscriptionTable.remove(snssais);
        return jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = ?", snssais);
    }



    public String getUnSubCorrelationID_SliceLoadLevel(String snssais) {

        String query = "SELECT subscriptionID FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = ?;";

        try {

            return jdbcTemplate.queryForObject(query, new Object[] {snssais}, (resultSet, i) -> resultSet.getString("subscriptionID"));

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public String getUnSubCorrelationID_QosSustainability(String plmnID, String snssais)
    {
        String query = "SELECT subscriptionID FROM nwdafQosSustainabilitySubscriptionTable WHERE plmnID = ? AND snssais = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[] {plmnID, snssais}, (resultSet, i) -> resultSet.getString("subscriptionID"));
        }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }






    public String getSnssais_SliceLoadLevel(String subscriptionID) {

        String query = "SELECT snssais FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = ?;";

        try {

            return jdbcTemplate.queryForObject(query, new Object[] {subscriptionID}, (resultSet, i) -> resultSet.getString("snssais"));

        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }


    public QosSustainabilitySubscriptionData getPlmnID_Snssais_QosSustainability(String subscriptionID)
    {
        String query = "SELECT plmnID, snssais FROM nwdafQosSustainabilitySubscriptionData WHERE subscriptionID = ?";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[] {subscriptionID}, (resultSet, i) -> new QosSustainabilitySubscriptionData(resultSet.getString("plmnID"), resultSet.getString("snssais")));
        }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }




    public Integer getRefCount_SliceLoadLevel(String snssais) {

        String query = "SELECT refCount FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = ?;";

        try {

            return jdbcTemplate.queryForObject(query, new Object[] {snssais} , (resultSet, i) -> resultSet.getInt("refCount"));

        } catch (EmptyResultDataAccessException e) {

            return 0;
        }
    }


    public Integer getRefCount_QosSustainability(String plmnID, String snssais)
    {
        String query = "SELECT refCount FROM nwdafQosSustainabilitySubscriptionTable WHERE plmnID = ? AND snssais = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[] {plmnID, snssais}, (resultSet, i) -> resultSet.getInt("refCount"));
        }

        catch(EmptyResultDataAccessException e)
        { return 0; }
    }





    public QosSustainabilitySubscriptionData getPlmnID_Snssais_ByCorrelationID(String correlationID)
    {
        String query = "SELECT plmnID, snssais FROM nwdafQosSustainabilitySubscriptionTable WHERE correlationID = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[]{correlationID}, (resultSet, i) -> new QosSustainabilitySubscriptionData(resultSet.getString("plmnID"), resultSet.getString("snssais")));
        }

        catch (EmptyResultDataAccessException ex)
        { return null; }
    }




    public Integer updateCurrentLoadLevel(int loadLevel, String snssais) {

        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelInformation SET currentLoadLevel = ? WHERE snssais = ?", new Object[]{loadLevel, snssais});
    }


    public String getSnssaisByCorrelationID(String correlationID, EventID eventID) {

        String query = "";

        if(eventID == EventID.LOAD_LEVEL_INFORMATION)
        { query = "SELECT snssais FROM nwdafSliceLoadLevelSubscriptionTable WHERE correlationID = '" + correlationID + "';"; }

        else if(eventID == EventID.QOS_SUSTAINABILITY)
        { query = "SELECT snssais FROM nwdafQosSustainabilitySubscriptionTable WHERE correlationID = '" + correlationID + "';"; }

        try {

            String s = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("snssais");
                }
            });
            return s;
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }


    public Integer getLoadLevelThreshold(String subID) {
        String query = "SELECT loadLevelThreshold FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = '" + subID + "';";

        try {

            Integer loadThreshold = jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("loadLevelThreshold");
                }
            });
            return loadThreshold;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }


    public Integer getCurrentLoadLevel(String snssais) {
        String query = "SELECT currentLoadLevel FROM nwdafSliceLoadLevelInformation WHERE snssais = '" + snssais + "';";

        try {

            Integer currentLoad = jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("currentLoadLevel");
                }
            });
            return currentLoad;
        } catch (EmptyResultDataAccessException e) {

            return 0;
        }
    }


    public Object findby_snssais(String snssais1) {

        String query = "SELECT * FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais=?";

        try {
            Object obj = (SliceLoadLevelSubscriptionTable) this.jdbcTemplate.queryForObject(query, new Object[]{snssais1}, new SliceLoadLevelSubscriptionTableMapper());

            return obj;
        } catch (EmptyResultDataAccessException ex) {

            return null;
        }
    }


    public String getCorrelationID(String mCorrelationID, EventID eventID) {

        String query = "";

        if(eventID == EventID.LOAD_LEVEL_INFORMATION)
        { query = "SELECT correlationID FROM nwdafSliceLoadLevelSubscriptionTable WHERE subscriptionID = ?;"; }

        else if(eventID == EventID.QOS_SUSTAINABILITY)
        { query = "SELECT correlationID FROM nwdafQosSustainabilitySubscriptionTable WHERE subscriptionID = ?;"; }


        try {

            return jdbcTemplate.queryForObject(query, new Object[] {mCorrelationID}, (resultSet, i) -> resultSet.getString("correlationID"));

        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }


    /****************************************************************************************************************/


    public List<NotificationData> getAllNotificationData(String snssais, Integer currentLoadLevel) {
        String query = "SELECT subscriptionID, loadLevelThreshold FROM nwdafSliceLoadLevelSubscriptionData WHERE snssais = ? AND loadLevelThreshold < ?;";

        try {
            return jdbcTemplate.query(query, new Object[] {snssais, currentLoadLevel} ,new NotificationDataMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }


    public Integer getMinLoadLevel(String snssais) {
        String query = "SELECT MIN(loadLevelThreshold) FROM nwdafSliceLoadLevelSubscriptionData WHERE snssais = '" + snssais + "';";

        try {
            return jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("MIN(loadLevelThreshold)");
                }
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }



    // function to update UE-Mobility Data.
    // Here we will insert values in to UE-mobility table;
    public void add_data_into_UE_mobilityTable() {
    }



    /***************************************Qos_Sustainability***************************************/




    public Boolean addDataQosSustainabilitySubscriptionData(NnwdafEventsSubscription nnwdafEventsSubscription) throws NullPointerException
    {
        String query;

        if(nnwdafEventsSubscription.getRanUeThroughputThreshold() != null)
        { query = "INSERT INTO nwdafQosSustainabilitySubscriptionData (subscriptionID, 5Qi, plmnID, tac, snssais, ranUeThroughputThreshold) VALUES(?, ?, ?, ?, ?, ?);"; }

        else
        { query = "INSERT INTO nwdafQosSustainabilitySubscriptionData (subscriptionID, 5Qi, plmnID, tac, snssais, qosFlowRetainThreshold) VALUES(?, ?, ?, ?, ?, ?);"; }


        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, nnwdafEventsSubscription.getSubscriptionID());

                preparedStatement.setInt(2, nnwdafEventsSubscription.get_5Qi());
                preparedStatement.setString(3, nnwdafEventsSubscription.getPlmnID());
                preparedStatement.setString(4, nnwdafEventsSubscription.getTac());

                preparedStatement.setString(5, nnwdafEventsSubscription.getSnssais());

                if(nnwdafEventsSubscription.getRanUeThroughputThreshold() != null)
                { preparedStatement.setInt(6, nnwdafEventsSubscription.getRanUeThroughputThreshold()); }

                else
                { preparedStatement.setInt(6, nnwdafEventsSubscription.getQosFlowRetainThreshold()); }

                return preparedStatement.execute();
            }
        });
    }


    public Boolean setNwdafQosSustainabilityInformation(String plmnID ,String snssais) throws NullPointerException
    {
        if(plmnIDandSnssaisExist("nwdafQosSustainabilityInformation", plmnID, snssais))
        { return Boolean.FALSE; }


        String query = "INSERT INTO nwdafQosSustainabilityInformation (plmnID, snssais, ranUeThroughput, qosFlowRetain) VALUES(?, ?, ?, ?);";

        plmnID_snssais_in_qosSustainabilityInformation.add(plmnID + "&" + snssais);

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, plmnID);
                preparedStatement.setString(2, snssais);
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, 0);

                return preparedStatement.execute();
            }
        });
    }




    public Boolean addDataQosSustainabilitySubscriptionTable(QosSustainabilitySubscriptionTable qos, boolean getAnalytics) {


        if(plmnIDandSnssaisExist("nwdafQosSustainabilitySubscriptionTable", qos.getPlmnID(), qos.getSnssais())) {
            jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", qos.getSnssais());
            return true;
        }


        String query = "INSERT INTO nwdafQosSustainabilitySubscriptionTable (plmnID, snssais,subscriptionID,correlationID,refCount) VALUES (?,?,?,?,?);";

        plmnID_snssais_in_qosSustainabilitySubscriptionTable.add(qos.getPlmnID() + "&" + qos.getSnssais());

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, qos.getPlmnID());
                preparedStatement.setString(2, qos.getSnssais());
                preparedStatement.setString(3, qos.getSubscriptionID());
                preparedStatement.setString(4, qos.getCorrelationID());

                if (getAnalytics) {
                    preparedStatement.setInt(5, 0);
                } else {
                    preparedStatement.setInt(5, 1);
                }

                return preparedStatement.execute();
            }
        });
    }


    public boolean plmnIDandSnssaisExist(String tableName, String plmnID ,String snssais)
    {
        if(tableName.equals("nwdafQosSustainabilitySubscriptionTable"))
        { return plmnID_snssais_in_qosSustainabilitySubscriptionTable.contains(plmnID + "&" + snssais); }

        else if(tableName.equals("nwdafQosSustainabilityInformation"))
        { return plmnID_snssais_in_qosSustainabilityInformation.contains(plmnID + "&" + snssais); }

        return false;
    }






  /*  public boolean plmnIDandSnssaisExist(String tableName, String plmnID ,String snssais)
    {
        if(tableName.equals("nwdafQosSustainabilitySubscriptionTable"))
        {
            if(plmnID_snssais_in_qosSustainabilitySubscriptionTable.contains(plmnID + "&" + snssais))
            { return true; }
        }

        else if(tableName.equals("nwdafQosSustainabilityInformation"))
        {
            if(plmnID_snssais_in_qosSustainabilityInformation.contains(plmnID + "&" + snssais))
            { return true; }
        }


        String plmnID_query = "SELECT IFNULL ((SELECT plmnID FROM " + tableName + " WHERE plmnID = ? LIMIT 1), null) AS plmnID;";

        String plmnIdExists = jdbcTemplate.queryForObject(plmnID_query, new Object[] {plmnID} , new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("plmnID");
            }
        });

        if(plmnIdExists != null)
        {
            String snssais_query = "SELECT IFNULL ((SELECT snssais FROM " + tableName + " WHERE snssais = ? AND plmnID = ? LIMIT 1), null) AS snssais;";

            String snssaisExists = jdbcTemplate.queryForObject(snssais_query, new Object[] {snssais, plmnID} ,new RowMapper<String>() {

                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("snssais");
                }
            });


            if(snssaisExists != null)
            {
                if(tableName.equals("nwdafQosSustainabilitySubscriptionTable"))
                { plmnID_snssais_in_qosSustainabilitySubscriptionTable.add(plmnID + "&" + snssais); }

                else if(tableName.equals("nwdafQosSustainabilityInformation"))
                { plmnID_snssais_in_qosSustainabilityInformation.add(plmnID + "&" + snssais); }

                return false;
            }

            return (snssaisExists != null);
        }



        if(tableName.equals("nwdafQosSustainabilitySubscriptionTable"))
        { plmnID_snssais_in_qosSustainabilitySubscriptionTable.add(plmnID + "&" + snssais); }

        else if(tableName.equals("nwdafQosSustainabilityInformation"))
        { plmnID_snssais_in_qosSustainabilityInformation.add(plmnID + "&" + snssais); }


        return false;
    } */






    public Integer deleteEntry_QosSustainabilitySubscriptionTable(String plmnID, String snssais) throws NullPointerException {

        plmnID_snssais_in_qosSustainabilitySubscriptionTable.remove(plmnID + "&" + snssais);
        return jdbcTemplate.update("DELETE FROM nwdafQosSustainabilitySubscriptionTable WHERE plmnID = ? AND snssais = ?", new Object[] {plmnID, snssais});
    }



    public Integer updateRanUeThroughput(Integer ranUeThroughput, String plmnID, String snssais) {

        return jdbcTemplate.update("UPDATE nwdafQosSustainabilityInformation SET ranUeThroughput = ? WHERE plmnID = ? AND snssais = ?", new Object[]{ranUeThroughput, plmnID, snssais});
    }

    public Integer updateQosFlowRetain(Integer qosFlowRetain, String plmnID, String snssais) {

        return jdbcTemplate.update("UPDATE nwdafQosSustainabilityInformation SET qosFlowRetain = ? WHERE plmnID = ? AND snssais = ?", new Object[]{qosFlowRetain, plmnID, snssais});
    }


    public List<QosNotificationData> getAllQosNotificationData(String plmnID, String snssais, Integer loadVal,  QosType qosType) {

        String query = "";

        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { query = "SELECT subscriptionID, tac, ranUeThroughputThreshold FROM nwdafQosSustainabilitySubscriptionData WHERE plmnID = ? AND snssais = ? AND ranUeThroughputThreshold < ?;"; }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { query = "SELECT subscriptionID, tac, qosFlowRetainThreshold FROM nwdafQosSustainabilitySubscriptionData WHERE plmnID = ? AND snssais = ? AND qosFlowRetainThreshold < ?;"; }

        try
        { return jdbcTemplate.query(query, new Object[] {plmnID, snssais, loadVal}, new QosNotificationDataMapper(qosType)); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public List<QosSustainabilityInformation> getAllPlmnId_SnssaisForQos(QosType qosType)
    {
        String query = "";

        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { query = "SELECT plmnID, snssais, ranUeThroughput FROM nwdafQosSustainabilityInformation;"; }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { query = "SELECT plmnID, snssais, qosFlowRetain FROM nwdafQosSustainabilityInformation;"; }

        try
        { return jdbcTemplate.query(query, new QosSustainabilityInformationMapper(qosType)); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }



    public Integer getRanUeThroughputThreshold(String subscriptionID)
    {
        String query = "SELECT ranUeThroughputThreshold FROM nwdafQosSustainabilitySubscriptionData WHERE subscriptionID = '" + subscriptionID + "';";

        return jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("ranUeThroughputThreshold");
            }
        });
    }


    public Integer getQosFlowRetainThreshold(String subscriptionID)
    {
        String query = "SELECT qosFlowRetainThreshold FROM nwdafQosSustainabilitySubscriptionData WHERE subscriptionID = '" + subscriptionID + "';";

        return jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("qosFlowRetainThreshold");
            }
        });
    }




    public QosSustainabilityInfo getPlmnID_Tac(String subscriptionID, Integer threshold, QosType qosType)
    {
        String query = "SELECT plmnID, tac FROM nwdafQosSustainability WHERE subscriptionID = '" + subscriptionID + "';";

        return jdbcTemplate.queryForObject(query, new RowMapper<QosSustainabilityInfo>() {
            @Override
            public QosSustainabilityInfo mapRow(ResultSet resultSet, int i) throws SQLException {

                return new QosSustainabilityInfo(resultSet.getString("plmnID"), resultSet.getString("tac"), threshold, qosType);
            }
        });
    }


    public Integer increaseRefCountQosSustainability(String plmnID, String snssais) {
        return jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionTable SET refCount = refCount + 1 WHERE plmnID = ? AND snssais = ?", new Object[] {plmnID, snssais});
    }





    /************************************************************************************************/





    /***************************************UE_MOBILITY**********************************************/



    public Boolean add_data_into_nwdaf_UeMobilitySubscriptionData(UEMobilitySubscriptionModel
                                                                          ue_mobilitySubscriptionModel) {

        String query = "INSERT INTO nwdafUEmobilitySubscriptionData(subscriptionID,supi) VALUES(?, ?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, ue_mobilitySubscriptionModel.getSubscriptionID());
                preparedStatement.setString(2, ue_mobilitySubscriptionModel.getSupi());

                return preparedStatement.execute();
            }
        });

    }




    public Boolean add_data_into_nwdafUEmobility(UEMobilitySubscriptionModel ue_mobilitySubscriptionModel) {

        System.out.println("in add_data_in_nwdafUEmobility");
        System.out.println("\n" + ue_mobilitySubscriptionModel.getSupi() + "     <---supi");

        String query = "INSERT INTO nwdafUEmobilityInformation(supi,ts,DurationSec) VALUES(?,'2008-11-11 13:23:44',0)on duplicate key update supi = supi";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, ue_mobilitySubscriptionModel.getSupi());

                return preparedStatement.execute();
            }
        });

    }



    public Boolean addValuesToUserLocationTable(String taiValue, String cellID, Integer timeDuration) {

        if(taiCellIdExists(taiValue, cellID))
        { return Boolean.FALSE; }

        String query = "INSERT INTO nwdafUserLocation(Tai,cellID,timeDuration) VALUES(?,?,?)";
        //String query1= "CREATE UNIQUE INDEX Tai ON UserLocation (plmnID,Tac,cellId);";


        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, taiValue);
                preparedStatement.setString(2, cellID);
                preparedStatement.setInt(3, timeDuration);


                return preparedStatement.execute();
            }
        });
    }


    public boolean taiCellIdExists(String tai, String cellID)
    {
        String tai_query = "SELECT IFNULL ((SELECT Tai FROM nwdafUserLocation WHERE Tai = '" + tai + "'), null) AS Tai;";


        String taiExists = jdbcTemplate.queryForObject(tai_query, new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("Tai");
            }
        });

        if(taiExists != null)
        {
            String cellID_query = "SELECT IFNULL ((SELECT cellID FROM nwdafUserLocation WHERE cellID = '" + cellID + "'), null) AS cellID;";

            String cellIDExists = jdbcTemplate.queryForObject(cellID_query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("cellID");
                }
            });

            return (cellIDExists != null);
        }

        return false;
    }



    public String getSupiValueByCorrelationID(String correlationID) {

        String query = "SELECT supi FROM nwdafUEmobilitySubscriptionTable WHERE correlationID = '" + correlationID + "';";

        //  System.out.println(query);

        try {

            String Supi = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("supi");
                }
            });
            return Supi;
        } catch (EmptyResultDataAccessException e) {

            return null;
        }

    }


    public Integer getRefCount_UEmobilitySubscriptionTable(String supi) {
        String query = "SELECT refCount FROM nwdafUEmobilitySubscriptionTable WHERE supi = '" + supi + "';";

        try {

            return jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("refCount");
                }
            });

        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }


    public Integer deleteEntry_UEMobilitySubscriptionTable(String supi) {

        return jdbcTemplate.update("DELETE FROM nwdafUEmobilitySubscriptionTable WHERE supi = ?", supi);
    }



    public List<EventConnectionForUEMobility> checkForUEMobilityData(String supi) {

       /* if (anySlice == true) {
            String s = "select *From nwdafSliceLoadLevelInformation";
            try {
                return jdbcTemplate.query(s, new AnalyticsRowMapper());
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }*/

        String s = "select * From nwdafUEmobilityInformation where supi = '" + supi + "';";
        try {
            return jdbcTemplate.query(s, new AnalyticsRowMapperForUEMobility());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }


    }




  /*  public List<Integer> getAllIDFromUserLocationTable(List<String> taiList) {

        String query = "SELECT ID FROM nwdafUserLocation WHERE Tai = '" + tai + "';";

        try {
            return jdbcTemplate.query(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("ID");
                }
            });
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    } */


    public Integer getID_UserLocationTable(String tai, String cellID) {

        String query = "SELECT ID FROM nwdafUserLocation WHERE Tai = ? AND cellID = ?;";

        try {
            return jdbcTemplate.queryForObject(query, new Object[] { tai, cellID } , new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("ID");
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    public Boolean updateLocationIdTable(String supi, String areaInfo[]) {

        String query = "INSERT INTO nwdafLocationID (supi, locationID) VALUES(?, ?);";

        Integer locationID = getID_UserLocationTable(areaInfo[0], areaInfo[1]);

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, supi);
                preparedStatement.setInt(2, locationID);

                return preparedStatement.execute();
            }
        });
    }


    public Integer deleteOldEntriesInLocationID(String supi)
    {  return jdbcTemplate.update("DELETE FROM nwdafLocationID WHERE supi = ?", supi); }



    public List<Integer> getLocationIDs(String supi)
    {
        String query = "SELECT locationID FROM nwdafLocationID WHERE supi = ?";

        try
        {
            return jdbcTemplate.query(query, new Object[] {supi}, new RowMapper<Integer>() {

                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt("locationID");
                }
            });
        }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }




    public String getSubscriptionIdBySupi(String supi)
    {
        String query = "SELECT subscriptionID FROM nwdafUEmobilitySubscriptionData WHERE supi = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[]{supi}, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("subscriptionID");
                }
            });
        }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }




    public String getAllNotificationDataForUEMobility(String supi) {

        String query = "SELECT location FROM nwdafUEmobility WHERE supi = ?;";

        System.out.println(query);

        try {

            String Supi = jdbcTemplate.queryForObject(query, new Object[] {supi}, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("location");
                }
            });
            return Supi;
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }




    public UserLocation getUserLocationFromID(Integer ID) {


        String query = "SELECT Tai,cellID,timeDuration FROM nwdafUserLocation WHERE ID = " + ID + ";";

        // System.out.println(query);

        try {

            UserLocation userLocation = jdbcTemplate.queryForObject(query, new RowMapper<UserLocation>() {
                @Override
                public UserLocation mapRow(ResultSet resultSet, int i) throws SQLException {
                    UserLocation userLocation = new UserLocation();
                    userLocation.setTai(resultSet.getString("Tai"));
                    userLocation.setCellID(resultSet.getString("cellID"));
                    userLocation.setTimeDuration(resultSet.getInt("timeDuration"));
                    //userLocation.setID(resultSet.getInt("ID"));
                    return userLocation;
                }
            });

            return userLocation;

        } catch (EmptyResultDataAccessException e) {

            return null;
        }

    }




    public List<String> findById_supi(String supi) {

        String query = "SELECT subscriptionID FROM nwdafUEmobilitySubscriptionData WHERE supi = ?";

        System.out.println(query);

        try {
            return jdbcTemplate.query(query, new Object[] {supi} ,new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("subscriptionID");
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    public Object findby_supi(String supi) {

        String query = "SELECT * FROM nwdafUEmobilitySubscriptionTable WHERE supi=?";

        try {
            Object obj = (UEMobilitySubscriptionTable) this.jdbcTemplate.queryForObject(query, new Object[]{supi},
                    new nwdafUEmobilitySubscriptionTableMapper());

            return obj;
        } catch (EmptyResultDataAccessException ex) {

            return null;
        }
    }


    public Integer increment_ref_count_ofSupi(String supi) {
        return jdbcTemplate.update("UPDATE nwdafUEmobilitySubscriptionTable SET refCount = refCount + 1 WHERE supi = ?", supi);
    }


    public boolean supiExists(String supi) {
        String query = "SELECT IFNULL ((SELECT supi FROM nwdafUEmobilitySubscriptionTable WHERE supi = '" + supi + "'), null) AS supi;";


        String result = jdbcTemplate.queryForObject(query, new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {

                return resultSet.getString("supi");
            }
        });
        return (result != null);
    }



    public Boolean addCorrealationIDAndUnSubCorrelationIDIntonwdafUEmobilitySubscriptionTable
            (UEMobilitySubscriptionTable slice, boolean getAnalytics) {


        if (supiExists(slice.getSupi())) {
            jdbcTemplate.update("UPDATE nwdafUEmobilitySubscriptionTable  SET refCount = refCount + 1 WHERE supi = ?", slice.getSupi());

            return true;
        }


        String query = "INSERT INTO nwdafUEmobilitySubscriptionTable (supi,subscriptionID,correlationID,refCount) VALUES (?,?,?,?);";


        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {


                preparedStatement.setString(1, slice.getSupi());
                preparedStatement.setString(2, String.valueOf(slice.getSubscriptionID()));
                preparedStatement.setString(3, String.valueOf(slice.getCorrelationID()));

                if (getAnalytics) {
                    preparedStatement.setInt(4, 0);
                } else {
                    preparedStatement.setInt(4, 1);
                }

                return preparedStatement.execute();
            }
        });

    }



    public Boolean addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable_UE(UEmobilitySubscriptionTable UEobj, boolean getAnalytics) {


        if (supiExists(UEobj.getSupi())) {
            jdbcTemplate.update("UPDATE nwdafUEmobilitySubscriptionTable SET refCount = refCount + 1 WHERE supi = ?", UEobj.getSupi());
            return true;
        }


        String query = "INSERT INTO nwdafUEmobilitySubscriptionTable (supi,subscriptionID,correlationID,refCount) VALUES (?,?,?,?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, UEobj.getSupi());
                preparedStatement.setString(2, UEobj.getSubscriptionID());
                preparedStatement.setString(3, UEobj.getCorrelationID());

                if (getAnalytics) {
                    preparedStatement.setInt(4, 0);
                } else {
                    preparedStatement.setInt(4, 1);
                }

                return preparedStatement.execute();
            }
        });

    }





    public String getSupi_UEmobilitySubscriptionData(String subID) {
        String query = "SELECT supi FROM nwdafUEmobilitySubscriptionData WHERE subscriptionID = '" + subID + "';";

        try {

            String snssais1 = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("supi");
                }
            });
            return snssais1;
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }




    public String unsubscribeNFForUE(SubscriptionTable subscriber)  {

        String supi = "";
        Integer eventID = subscriber.getEventID();

        supi = getSupi_UEmobilitySubscriptionData(subscriber.getSubscriptionID());
        jdbcTemplate.update("DELETE FROM nwdafUEmobilitySubscriptionData WHERE subscriptionID = ?", subscriber.getSubscriptionID());

        //  jdbcTemplate.update("DELETE FROM nwdafUEmobility WHERE supi = ?", supi);

        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionID = ?", subscriber.getSubscriptionID());

        //return decrementRefCount(supi, eventID);
        return decrementRefCountForUE(supi, eventID);
    }



    public String decrementRefCountForUE(String supi, Integer eventID)  {


        jdbcTemplate.update("UPDATE nwdafUEmobilitySubscriptionTable SET refCount = refCount - 1 WHERE supi = ?", supi);

        if (getRefCount_UEmobilitySubscriptionTable(supi) == 0) {
            /* time to delete the entry from db, caller shall send the subscribe message towards peer first */
            return supi;
        }

        return null;
    }




    public String getUnSubCorrelationID_UEMobility(String supi) {

        //String query = "select snssais from nwdafSliceLoadLevelSubscriptionTable where subscriptionID "
        String query = "SELECT subscriptionID FROM nwdafUEmobilitySubscriptionTable WHERE supi = '" + supi + "';";
        try {

            String unSubID = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("subscriptionID");
                }
            });
            return unSubID;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    public String getCorrelationID_UEMobility(String mCorrelationID) {

        String query = "SELECT correlationID FROM nwdafUEmobilitySubscriptionTable WHERE subscriptionID = '" + mCorrelationID + "';";

        try {

            String correlationID = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("correlationID");
                }
            });
            return correlationID;

        } catch (EmptyResultDataAccessException e) {
            // return e;
        }
        return null;

    }


    /************************************************************************************************/


    public void updateSliceLoadLevelEntry(NnwdafEventsSubscription subscription, int order)
    {

        if(order == 1)
        { jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionData SET loadLevelThreshold = ? WHERE subscriptionID = ?;", new Object[] { subscription.getLoadLevelThreshold(), subscription.getSubscriptionID() }); }

        if(order == 2)
        { jdbcTemplate.update("UPDATE nwdafSubscriptionTable SET notifMethod = ?, repetitionPeriod = ? WHERE subscriptionID = ?;", new Object[] { subscription.getNotifMethod(), subscription.getRepetitionPeriod(), subscription.getSubscriptionID() }); }

        else if(order == 3)
        { jdbcTemplate.update("UPDATE nwdafSubscriptionTable SET notifMethod = ? WHERE subscriptionID = ?;", new Object[] { subscription.getNotifMethod(), subscription.getSubscriptionID() }); }

        else if(order == 4)
        { jdbcTemplate.update("UPDATE nwdafSubscriptionTable SET repetitionPeriod = ? WHERE subscriptionID = ?;", new Object[] { subscription.getRepetitionPeriod(), subscription.getSubscriptionID() }); }

    }


    public void updateQosSustainabilityEntry(NnwdafEventsSubscription subscription, QosType qosType)
    {
        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionData SET ranUeThroughputThreshold = ?, qosFlowRetainThreshold = null WHERE subscriptionID = ?;", new Object[] { subscription.getRanUeThroughputThreshold(), subscription.getSubscriptionID()}); }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionData SET ranUeThroughputThreshold = null, qosFlowRetainThreshold = ? WHERE subscriptionID = ?;", new Object[] { subscription.getQosFlowRetainThreshold(), subscription.getSubscriptionID()}); }
    }



    public String subscriptionExists(String subscriptionID)
    {
        String query = "SELECT subscriptionID FROM nwdafSubscriptionTable WHERE subscriptionID = ?";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[] {subscriptionID}, new RowMapper<String>() {

                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("subscriptionID");
                }
            });
        }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }

}