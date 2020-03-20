package com.nwdaf.Analytics.Repository;


import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnectionUE;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.NotificationData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SubscriptionTable;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainability;
import com.nwdaf.Analytics.Model.TableType.UEmobility.UEmobilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEmobility.userLocationTable;
import com.nwdaf.Analytics.Model.UserLocation;
import com.nwdaf.Analytics.Repository.Mapper.AnalyticsRowMapper;
import com.nwdaf.Analytics.Repository.Mapper.*;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SliceLoadLevelInformationMapper;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SliceLoadLevelSubscriptionDataMapper;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SliceLoadLevelSubscriptionTableMapper;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SubscriptionTableMapper;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.naming.event.EventContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Repository
public class Nnwdaf_Repository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Random random = new Random();


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
        jdbcTemplate.update(updateSubscriptionTable, new Object[]{user.getEventID(), user.getNotifMethod(), user.getRepetitionPeriod(), id});


        // Updating nwdafSliceLoadLevelSubscriptionData
        String updateLoadLevelSubData = "UPDATE nwdafSliceLoadLevelSubscriptionData SET loadLevelThreshold = ? WHERE subscriptionID = ?";
        jdbcTemplate.update(updateLoadLevelSubData, new Object[]{user.getLoadLevelThreshold(), id});


        return 1;
    }


    public String unsubscribeNF(SubscriptionTable subscriber) throws Exception {

        String snssais = "";
        Integer eventID = subscriber.getEventID();

        if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        {
            snssais = getSnssais_LoadLevelSubscriptionData(subscriber.getSubscriptionID());
            jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = ?", subscriber.getSubscriptionID());
        }

        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        {
            snssais = getSnssais_QosSubscriptionData(subscriber.getSubscriptionID());
            jdbcTemplate.update("DELETE FROM nwdafQosSustainabilitySubscriptionData WHERE subscriptionID = ?", subscriber.getSubscriptionID());
            jdbcTemplate.update("DELETE FROM nwdafQosSustainability WHERE subscriptionID = ?", subscriber.getSubscriptionID());
        }

        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionID = ?", subscriber.getSubscriptionID());

        return decrementRefCount(snssais, eventID);
    }


    public List<EventConnection> checkForData(String snssais, Boolean anySlice, Integer eventID) {


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

            String s = "select *From nwdafSliceLoadLevelInformation where snssais = '" + snssais + "';";
            try {
                return jdbcTemplate.query(s, new AnalyticsRowMapper());
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }


        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        {

        }

        return null;
    }

    /***UEmobility****************/


    public List<EventConnectionUE> checkForDataUE(String supi) {



           String s = "select * From nwdafuemobility";

            try {
                return jdbcTemplate.query(s, new AnalyticsRowMapperUE());
            }
            catch (EmptyResultDataAccessException e) {
                return null;
            }
    }

    /***UEmobility****************/


    public Boolean addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(SliceLoadLevelSubscriptionTable slice, boolean getAnalytics) {


        if (snsExistsLoadLevelSubscriptionTable(slice.getSnssais())) {
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

                if (getAnalytics) {
                    preparedStatement.setInt(4, 0);
                } else {
                    preparedStatement.setInt(4, 1);
                }

                return preparedStatement.execute();
            }
        });

    }


    /********UEmobility***********/

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


    /****UEmobility**********/

















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

    /****UEmobility*******/

    public Boolean add_data_into_nwdafUEmobility_table(String supi) {

        String query = "INSERT INTO nwdafuemobility (supi,ts,DurationSec,location) VALUES(?,?,?,?)";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, supi);
                preparedStatement.setDate(2, java.sql.Date.valueOf("2013-09-04"));
                preparedStatement.setInt(3, 2);
                preparedStatement.setString(4, "1,2");



                return preparedStatement.execute();
            }
        });

    }














    /****UEmobility*******/


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


    public boolean snsExistsLoadLevelSubscriptionTable(String snssais) {
        String query = "SELECT IFNULL ((SELECT snssais FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = '" + snssais + "'), null) AS snssais;";


        String result = jdbcTemplate.queryForObject(query, new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("snssais");
            }
        });

        return (result != null);

    }


    public Integer increment_ref_count(String snssais) {
        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", snssais);
    }


    /********UEmobility************/

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


    /********UEmobility************/


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


    public Integer increaseRefCount_LoadLevelSubscriptionTable(String snssais) {
        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", snssais);
    }


    // 14 feb update

    public String decrementRefCount(String snssais, Integer eventID) throws Exception {

        if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        {
            jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount - 1 WHERE snssais = ?", snssais);

            if (getRefCount_LoadLevelSubscriptionTable(snssais) == 0) {
                /* time to delete the entry from db, caller shall send the subscribe message towards peer first */
                return snssais;
            }
        }


        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        {
            jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionTable SET refCount = refCount - 1 WHERE snssais = ?", snssais);

            if (getRefCount_QosSubscriptionTable(snssais) == 0) {
                /* time to delete the entry from db, caller shall send the subscribe message towards peer first */
                return snssais;
            }
        }

        return null;
    }


    public Integer deleteEntry_SliceLoadLevelSubscriptionTable(String snssais) {

        return jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = ?", snssais);
    }


    public String getUnSubCorrelationID_LoadLevelInformation(String snssais) {

        //String query = "select snssais from nwdafSliceLoadLevelSubscriptionTable where subscriptionID "
        String query = "SELECT subscriptionID FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = '" + snssais + "';";
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





    public String getSnssais_LoadLevelSubscriptionData(String subID) {
        String query = "SELECT snssais FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionID = '" + subID + "';";

        try {

            String snssais1 = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("snssais");
                }
            });
            return snssais1;
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }


    public Integer getRefCount_LoadLevelSubscriptionTable(String snssais) {
        String query = "SELECT refCount FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssais = '" + snssais + "';";

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


    public Integer updateCurrentLoadLevel(int loadLevel, String snssais) {

        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelInformation SET currentLoadLevel = ? WHERE snssais = ?", new Object[]{loadLevel, snssais});
    }


    public String getSnssaisByCorrelationID(String correlationID) {
        String query = "SELECT snssais FROM nwdafSliceLoadLevelSubscriptionTable WHERE correlationID = '" + correlationID + "';";

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


    public String getCorrelationID_LoadLevelInformation(String mCorrelationID) {

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


    /****************************************************************************************************************/


    public List<NotificationData> getAllNotificationData(String snssais, Integer currentLoadLevel) {
        String query = "SELECT subscriptionID, loadLevelThreshold FROM nwdafSliceLoadLevelSubscriptionData WHERE snssais = '" + snssais + "' AND loadLevelThreshold < " + currentLoadLevel + ";";

        try {
            return jdbcTemplate.query(query, new NotificationDataMapper());
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



    public Boolean addDataQosSustainability(NnwdafEventsSubscription nnwdafEventsSubscription) {

        String query = "INSERT INTO nwdafQosSustainability (subscriptionID, 5Qi, plmnID, tac) VALUES(?, ?, ?, ?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, nnwdafEventsSubscription.getSubscriptionID());
                preparedStatement.setInt(2, nnwdafEventsSubscription.get_5Qi());
                preparedStatement.setString(3, nnwdafEventsSubscription.getPlmnID());
                preparedStatement.setString(4, nnwdafEventsSubscription.getTac());

                return preparedStatement.execute();
            }
        });
    }


    public Boolean addDataQosSustainabilitySubscriptionData(NnwdafEventsSubscription nnwdafEventsSubscription)
    {
        String query;

        if(nnwdafEventsSubscription.getRanUeThroughputThreshold() != null)
        { query = "INSERT INTO nwdafQosSustainabilitySubscriptionData (subscriptionID, snssais, ranUeThroughputThreshold) VALUES(?, ?, ?)"; }

        else
        { query = "INSERT INTO nwdafQosSustainabilitySubscriptionData (subscriptionID, snssais, qosFlowRetainThreshold) VALUES(?, ?, ?)"; }


        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, nnwdafEventsSubscription.getSubscriptionID());
                preparedStatement.setString(2, nnwdafEventsSubscription.getSnssais());

                if(nnwdafEventsSubscription.getRanUeThroughputThreshold() != null)
                { preparedStatement.setInt(3, nnwdafEventsSubscription.getRanUeThroughputThreshold()); }

                else
                { preparedStatement.setInt(3, nnwdafEventsSubscription.getQosFlowRetainThreshold()); }

                return preparedStatement.execute();
            }
        });
    }


    public Boolean setNwdafQosSustainabilityInformation(NnwdafEventsSubscription nnwdafEventsSubscription)
    {
        String query = "INSERT INTO nwdafQosSustainabilityInformation VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE snssais = snssais";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, nnwdafEventsSubscription.getSnssais());
                preparedStatement.setInt(2, 0);
                preparedStatement.setInt(3, 0);

                return preparedStatement.execute();
            }
        });
    }


    public boolean snsExistsQosSubscriptionTable(String snssais) {
        String query = "SELECT IFNULL ((SELECT snssais FROM nwdafQosSustainabilitySubscriptionTable WHERE snssais = '" + snssais + "'), null) AS snssais;";

        String result = jdbcTemplate.queryForObject(query, new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("snssais");
            }
        });

        return (result != null);
    }



    public Boolean addDataQosSustainabilitySubscriptionTable(QosSustainabilitySubscriptionTable qos, boolean getAnalytics) {


        if (snsExistsQosSubscriptionTable(qos.getSnssais())) {
            jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", qos.getSnssais());
            return true;
        }


        String query = "INSERT INTO nwdafQosSustainabilitySubscriptionTable (snssais,subscriptionID,correlationID,refCount) VALUES (?,?,?,?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, qos.getSnssais());
                preparedStatement.setString(2, qos.getSubscriptionID());
                preparedStatement.setString(3, qos.getCorrelationID());

                if (getAnalytics) {
                    preparedStatement.setInt(4, 0);
                } else {
                    preparedStatement.setInt(4, 1);
                }

                return preparedStatement.execute();
            }
        });

    }


    public Integer increaseRefCount_QosSubscriptionTable(String snssais) {
        return jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionTable SET refCount = refCount + 1 WHERE snssais = ?", snssais);
    }



    public String getSnssais_QosSubscriptionData(String subscriptionID) {

        String query = "SELECT snssais FROM nwdafQosSustainabilitySubscriptionData WHERE subscriptionID = '" + subscriptionID + "';";

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




    public Integer getRefCount_QosSubscriptionTable(String snssais) {
        String query = "SELECT refCount FROM nwdafQosSustainabilitySubscriptionTable WHERE snssais = '" + snssais + "';";

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


    public Integer deleteEntry_QosSustainabilitySubscriptionTable(String snssais) {

        return jdbcTemplate.update("DELETE FROM nwdafQosSustainabilitySubscriptionTable WHERE snssais = ?", snssais);
    }



    public String getUnSubCorrelationID_QosSustainability(String snssais) {

        //String query = "select snssais from nwdafSliceLoadLevelSubscriptionTable where subscriptionID "
        String query = "SELECT subscriptionID FROM nwdafQosSustainabilitySubscriptionTable WHERE snssais = '" + snssais + "';";
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


    public String getCorrelationID_QosSustainability(String mCorrelationID) {

        String query = "SELECT correlationID FROM nwdafQosSustainabilitySubscriptionTable WHERE subscriptionID = '" + mCorrelationID + "';";

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


    /************************************************************************************************/
    /*****************UEmobility******************************************************************/


    public String getAllNotificationDataForUEMobility(String supi) {

        String query = "SELECT location FROM nwdafUEmobility WHERE supi = '" + supi + "';";

        // System.out.println(query);

        try {

            String Supi = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
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


        String query = "SELECT ID,Tai,cellID,timeDuration FROM nwdafUserLocation WHERE ID = " + ID + ";";

        // System.out.println(query);

        try {

            UserLocation userLocation = jdbcTemplate.queryForObject(query, new RowMapper<UserLocation>() {
                @Override
                public UserLocation mapRow(ResultSet resultSet, int i) throws SQLException {

                    UserLocation userLocation = new UserLocation();
                    userLocation.setID(resultSet.getInt("ID"));
                    userLocation.setTai(resultSet.getString("Tai"));
                    userLocation.setCellID(resultSet.getString("cellID"));
                    userLocation.setTimeDuration(resultSet.getInt("timeDuration"));
                    return userLocation;
                }
            });

            return userLocation;
        } catch (EmptyResultDataAccessException e) {

            return null;
        }

    }

    /*****************UEmobility******************************************************************/



}