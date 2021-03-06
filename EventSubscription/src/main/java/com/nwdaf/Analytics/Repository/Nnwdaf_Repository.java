package com.nwdaf.Analytics.Repository;

import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnectionForUEMobility;
import com.nwdaf.Analytics.Model.*;
import com.nwdaf.Analytics.Model.AnalyticsInformation.ServiceExperienceInfo;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.CustomData.NfLoad.NfThresholdType;
import com.nwdaf.Analytics.Model.CustomData.QosSustainability.QosThresholdType;
import com.nwdaf.Analytics.Model.CustomData.QosSustainability.RetainabilityThreshold;
import com.nwdaf.Analytics.Model.CustomData.ServiceExperience.SvcExperience;
import com.nwdaf.Analytics.Model.CustomData.TableType;
import com.nwdaf.Analytics.Model.EventNotificationType.QosSustainabilityInfo;
import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.NfLoad.NfLoadInformation;
import com.nwdaf.Analytics.Model.TableType.NfLoad.NfLoadSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.NfLoad.NfLoadSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.SubscriptionID;
import com.nwdaf.Analytics.Model.TableType.SubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionData;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.UEmobilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UserLocation;
import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommInformation;
import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionTable;
import com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper.*;
import com.nwdaf.Analytics.Repository.Mapper.AnalyticsRowMapper;
import com.nwdaf.Analytics.Repository.Mapper.*;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SliceLoadLevelInformationMapper;
import com.nwdaf.Analytics.Repository.Mapper.LoadLevelInformationMapper.SliceLoadLevelSubscriptionDataMapper;
import com.nwdaf.Analytics.Repository.Mapper.SubscriptionTableMapper;
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
import java.sql.Types;
import java.util.List;
import java.util.Random;

@Repository
public class Nnwdaf_Repository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Random random = new Random();


    public Boolean subscriberRegister(SubscriptionID subscription)
    {
        String query = "INSERT INTO nwdafSubscriptionID VALUES (?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, subscription.getSubscriptionId());
            preparedStatement.setString(2, subscription.getNotificationURI());

            return preparedStatement.execute();
        });
    }


    public Boolean subscribeNF(SubscriptionTable subscription) throws NullPointerException {

        String query = "INSERT INTO nwdafSubscriptionTable (subscriptionId, event, notificationMethod, repetitionPeriod) VALUES (?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, subscription.getSubscriptionId());
            preparedStatement.setInt(2, subscription.getEvent());
            preparedStatement.setInt(3, subscription.getNotificationMethod());

            if(subscription.getRepetitionPeriod() == null)
            { preparedStatement.setNull(4, Types.INTEGER); }

            else
            { preparedStatement.setInt(4, subscription.getRepetitionPeriod()); }

            return preparedStatement.execute();
        });
    }


    public List<Integer> getAllSubscribedEvents(String subscriptionId)
    {
        String query = "SELECT event FROM nwdafSubscriptionTable WHERE subscriptionId = ?;";
        return jdbcTemplate.query(query, new Object[]{subscriptionId}, (resultSet, i) -> resultSet.getInt("event"));
    }


    public boolean subscriptionExists(String subscriptionId)
    {
        String query = "SELECT EXISTS(SELECT 1 FROM nwdafSubscriptionID WHERE subscriptionId = ?) AS subscriptionId;";
        Integer subscriberExists = jdbcTemplate.queryForObject(query, new Object[]{subscriptionId}, (resultSet, i) -> resultSet.getInt("subscriptionId"));

        return subscriberExists == 1;
    }


    public Integer deleteSubscriberEntry(String subscriptionId)
    { return jdbcTemplate.update("DELETE FROM nwdafSubscriptionID WHERE subscriptionId = ?;", subscriptionId); }



    public String unsubscribeNF_SliceLoadLevel(String subscriptionId)
    {
        String snssais = getSnssais_SliceLoadLevel(subscriptionId);

        jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionId = ?;", subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ? AND event = ?;", subscriptionId, NwdafEvent.LOAD_LEVEL_INFORMATION.ordinal());

        return decrementRefCount_SliceLoadLevel(snssais);
    }


    public QosSustainabilitySubscriptionData unsubscribeNF_QosSustainability(String subscriptionId)
    {
        QosSustainabilitySubscriptionData qosData = getTai_Snssais_QosSustainability(subscriptionId);

        jdbcTemplate.update("DELETE FROM nwdafQosSustainabilitySubscriptionData WHERE subscriptionId = ?;", subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ? AND event = ?;", subscriptionId, NwdafEvent.QOS_SUSTAINABILITY.ordinal());

        return decrementRefCount_QosSustainability(qosData);
    }



    public ServiceExperienceSubscriptionData unsubscribeNF_ServiceExperience(String subscriptionId)
    {
        ServiceExperienceSubscriptionData svcExp = getSupi_Snssais_ServiceExperience(subscriptionId);

        jdbcTemplate.update("DELETE FROM nwdafServiceExperienceSubscriptionData WHERE subscriptionId = ?;", subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ? AND event = ?;", subscriptionId, NwdafEvent.SERVICE_EXPERIENCE.ordinal());

        return decrementRefCount_ServiceExperience(svcExp);
    }


    public NetworkPerformanceSubscriptionData unsubscribeNF_NetworkPerformance(String subscriptionId)
    {
        NetworkPerformanceSubscriptionData nwPerfSubData = getSupi_nwPerfType_NetworkPerformance(subscriptionId);

        jdbcTemplate.update("DELETE FROM nwdafNetworkPerformanceSubscriptionData WHERE subscriptionId = ?;", subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ? AND event = ?;", subscriptionId, NwdafEvent.NETWORK_PERFORMANCE.ordinal());

        return decrementRefCount_NetworkPerformance(nwPerfSubData);
    }


    public UserDataCongestionSubscriptionData unsubscribeNF_UserDataCongestion(String subscriptionId)
    {
        UserDataCongestionSubscriptionData usrDataCongSubData = getSupi_congType_UserDataCongestion(subscriptionId);

        jdbcTemplate.update("DELETE FROM nwdafUserDataCongestionSubscriptionData WHERE subscriptionId = ?;", subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ? AND event = ?;", subscriptionId, NwdafEvent.USER_DATA_CONGESTION.ordinal());

        return decrementRefCount_UserDataCongestion(usrDataCongSubData);
    }


    public void unsubscribeNF_AbnormalBehaviourSubscriptionEntry(String subscriptionId)
    {
        jdbcTemplate.update("DELETE FROM nwdafAbnormalBehaviourSubscriptionData WHERE subscriptionId = ?;", subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ? AND event = ?;", subscriptionId, NwdafEvent.ABNORMAL_BEHAVIOUR.ordinal());
    }

    public AbnormalBehaviourSubscriptionData unsubscribeNF_AbnormalBehaviour(String supi, Integer excepId)
    {
        AbnormalBehaviourSubscriptionData abnorBehavrSubData = new AbnormalBehaviourSubscriptionData();

        abnorBehavrSubData.setSupi(supi);
        abnorBehavrSubData.setExcepId(excepId);

        return decrementRefCount_AbnormalBehaviour(abnorBehavrSubData);
    }


    public String unsubscribeNF_UeComm(String subscriptionId)
    {
        String supi = getSupi_UeCommSubscriptionData(subscriptionId);

        jdbcTemplate.update("DELETE FROM nwdafUeCommSubscriptionData WHERE subscriptionId = ?;", subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ? AND event = ?;", subscriptionId, NwdafEvent.UE_COMM.ordinal());

        return decrementRefCount_UeComm(supi);
    }


    public NfLoadSubscriptionData unsubscribeNF_NfLoad(String subscriptionId)
    {
        NfLoadSubscriptionData nfLoadSubscriptionData = getNfType_NfInstanceId_NfLoadSubscriptionData(subscriptionId);

        jdbcTemplate.update("DELETE FROM nwdafNfLoadSubscriptionData WHERE subscriptionId = ?;", subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ? AND event = ?;", subscriptionId, NwdafEvent.NF_LOAD.ordinal());

        return decrementRefCount_NfLoad(nfLoadSubscriptionData);
    }



    public List<EventConnection> checkForData(String snssai, Boolean anySlice) {

        if (anySlice == true) {
            String s = "select * From nwdafSliceLoadLevelInformation";
            try {
                return jdbcTemplate.query(s, new AnalyticsRowMapper());
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }

        String s = "select * From nwdafSliceLoadLevelInformation where snssai = '" + snssai + "';";
        try {
            return jdbcTemplate.query(s, new AnalyticsRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }



    public boolean snssaisExists_SliceLoadLevelSubscriptionTable(String snssai)
    {
        String query = "SELECT EXISTS(SELECT 1 FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssai = ?) AS snssai;";

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{snssai}, new RowMapper<Integer>() {

            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("snssai");
            }
        });

        return exists == 1;
    }



    public Boolean addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(SliceLoadLevelSubscriptionTable slice, boolean getAnalytics) {


        String query = "INSERT INTO nwdafSliceLoadLevelSubscriptionTable (snssai,subscriptionId,correlationId,refCount) VALUES (?,?,?,?);";


        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, slice.getSnssai());
                preparedStatement.setString(2, slice.getSubscriptionId());
                preparedStatement.setString(3, slice.getCorrelationId());

                if (getAnalytics) {
                    preparedStatement.setInt(4, 0);
                } else {
                    preparedStatement.setInt(4, 1);
                }

                return preparedStatement.execute();
            }
        });

    }


    public Boolean add_data_into_load_level_table(String snssai) throws NullPointerException {

        String query = "INSERT INTO nwdafSliceLoadLevelInformation (snssai,currentLoadLevel) VALUES(?,?) on duplicate key update snssai = snssai";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, snssai);
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


    public List<SliceLoadLevelSubscriptionData> getAllSubIdsbysnssais(String snssai) {

        String query = "SELECT * FROM nwdafSliceLoadLevelSubscriptionData WHERE snssai ='" + snssai + "'";


        try {
            return jdbcTemplate.query(query, new SliceLoadLevelSubscriptionDataMapper());
        } catch (EmptyResultDataAccessException e) {

            return null;
        }

    }


    public Integer currentLoadLevel(String snssai) {

        String query = "SELECT currentLoadLevel FROM nwdafSliceLoadLevelInformation WHERE snssai = '" + snssai + "';";
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

                preparedStatement.setString(1, sliceLoadLevel.getSubscriptionId());
                preparedStatement.setString(2, sliceLoadLevel.getSnssai());

                if(sliceLoadLevel.getLoadLevelThreshold() == null)
                { preparedStatement.setNull(3, Types.INTEGER); }

                else
                { preparedStatement.setInt(3, sliceLoadLevel.getLoadLevelThreshold()); }

                return preparedStatement.execute();
            }
        });

    }


    public SubscriptionTable findById_subscriptionID(String subID) throws NullPointerException {
        String query = "SELECT * FROM nwdafSubscriptionTable WHERE subscriptionId = ?";

        try {

            return (SubscriptionTable) this.jdbcTemplate.queryForObject(query, new Object[]{subID}, new SubscriptionTableMapper());
        } catch (EmptyResultDataAccessException ex) {

            return null;
        }
    }


    public String getNotificationURI(String subscriptionId) {

        String query = "SELECT notificationURI FROM nwdafSubscriptionID WHERE subscriptionId = ?";

        return jdbcTemplate.queryForObject(query, new Object[]{subscriptionId}, (resultSet, i) -> resultSet.getString("notificationURI"));
    }




    public String getSnssaisViaSubID(String correlationId) {

        //String query = "select snssais from nwdafSliceLoadLevelSubscriptionTable where subscriptionID "
        String query = "SELECT snssais FROM nwdafSliceLoadLevelSubscriptionTable WHERE subscriptionId = '" + correlationId + "';";

        try {

            return jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("snssai");
                }
            });
        } catch (EmptyResultDataAccessException e) {

            return null;
        }

    }


    public Integer increaseRefCountSliceLoadLevel(String snssai) {
        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount + 1 WHERE snssai = ?", snssai);
    }


    // 14 feb update


    public String decrementRefCount_SliceLoadLevel(String snssai) {

        jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionTable SET refCount = refCount - 1 WHERE snssai = ?", snssai);

        if (getRefCount_SliceLoadLevel(snssai) == 0) {
            /* time to delete the entry from db, caller shall send the subscribe message towards peer first */
            return snssai;
        }

        return null;
    }


    public QosSustainabilitySubscriptionData decrementRefCount_QosSustainability(QosSustainabilitySubscriptionData qosData)
    {
        jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionTable SET refCount = refCount - 1 WHERE snssai = ? AND tai = ?;", new Object[] {qosData.getSnssai(), qosData.getTai()});

        if(getRefCount_QosSustainability(qosData.getTai(), qosData.getSnssai()) == 0)
        {  return qosData; }

        return null;
    }


    public ServiceExperienceSubscriptionData decrementRefCount_ServiceExperience(ServiceExperienceSubscriptionData svcExp)
    {
        jdbcTemplate.update("UPDATE nwdafServiceExperienceSubscriptionTable SET refCount = refCount - 1 WHERE supi = ? AND snssai = ?;", new Object[] {svcExp.getSupi(), svcExp.getSnssai()});

        if(getRefCount_ServiceExperience(svcExp.getSupi(), svcExp.getSnssai()) == 0)
        { return svcExp; }

        return null;
    }

    public NetworkPerformanceSubscriptionData decrementRefCount_NetworkPerformance(NetworkPerformanceSubscriptionData nwPerfSubData)
    {
        jdbcTemplate.update("UPDATE nwdafNetworkPerformanceSubscriptionTable SET refCount = refCount - 1 WHERE supi = ? AND nwPerfType = ?;", new Object[] { nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType() });

        if(getRefCount_NetworkPerformance(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType()) == 0)
        { return nwPerfSubData; }

        return null;
    }


    public UserDataCongestionSubscriptionData decrementRefCount_UserDataCongestion(UserDataCongestionSubscriptionData usrDataCongSubData)
    {
        jdbcTemplate.update("UPDATE nwdafUserDataCongestionSubscriptionTable SET refCount = refCount - 1 WHERE supi = ? AND congType = ?;", new Object[] { usrDataCongSubData.getSupi(), usrDataCongSubData.getCongType() });

        if(getRefCount_UserDataCongestion(usrDataCongSubData.getSupi(), usrDataCongSubData.getCongType()) == 0)
        { return usrDataCongSubData; }

        return null;
    }


    public AbnormalBehaviourSubscriptionData decrementRefCount_AbnormalBehaviour(AbnormalBehaviourSubscriptionData abnorBehavrSubData)
    {
        jdbcTemplate.update("UPDATE nwdafAbnormalBehaviourSubscriptionTable SET refCount = refCount - 1 WHERE supi = ? AND excepId = ?;", new Object[] { abnorBehavrSubData.getSupi(), abnorBehavrSubData.getExcepId() });

        if(getRefCount_AbnormalBehaviour(abnorBehavrSubData.getSupi(), abnorBehavrSubData.getExcepId()) == 0)
        { return abnorBehavrSubData; }

        return null;
    }


    public String decrementRefCount_UeComm(String supi)
    {
        jdbcTemplate.update("UPDATE nwdafUeCommSubscriptionTable SET refCount = refCount - 1 WHERE supi = ?;", supi);

        if(getRefCount_UeComm(supi) == 0)
        { return supi; }

        return null;
    }


    public NfLoadSubscriptionData decrementRefCount_NfLoad(NfLoadSubscriptionData nfLoadSubscriptionData)
    {
        jdbcTemplate.update("UPDATE nwdafNfLoadSubscriptionTable SET refCount = refCount - 1 WHERE nfType = ? AND nfInstanceId = ?;", new Object[] { nfLoadSubscriptionData.getNfType(), nfLoadSubscriptionData.getNfInstanceId() });

        if(getRefCount_NfLoad(nfLoadSubscriptionData.getNfType(), nfLoadSubscriptionData.getNfInstanceId()) == 0)
        { return nfLoadSubscriptionData; }

        return null;
    }



    public Integer deleteEntry_SliceLoadLevelSubscriptionTable(String snssai) throws NullPointerException {

        return jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssai = ?;", snssai);
    }

    public Integer deleteEntry_SliceLoadLevelInformation(String snssai) throws NullPointerException {

        return jdbcTemplate.update("DELETE FROM nwdafSliceLoadLevelInformation WHERE snssai = ?;", snssai);
    }



    public String getUnSubCorrelationID_SliceLoadLevel(String snssai) {

        String query = "SELECT subscriptionId FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssai = ?;";

        try {

            return jdbcTemplate.queryForObject(query, new Object[] {snssai}, (resultSet, i) -> resultSet.getString("subscriptionId"));

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }






    public String getSnssais_SliceLoadLevel(String subscriptionId) {

        String query = "SELECT snssai FROM nwdafSliceLoadLevelSubscriptionData WHERE subscriptionId = ?;";

        try {

            return jdbcTemplate.queryForObject(query, new Object[] {subscriptionId}, (resultSet, i) -> resultSet.getString("snssai"));

        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }


    public QosSustainabilitySubscriptionData getTai_Snssais_QosSustainability(String subscriptionId)
    {
        String query = "SELECT tai, snssai FROM nwdafQosSustainabilitySubscriptionData WHERE subscriptionId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{subscriptionId}, (resultSet, i) -> {

            QosSustainabilitySubscriptionData qosSustainabilitySubscriptionData = new QosSustainabilitySubscriptionData();

            qosSustainabilitySubscriptionData.setTai(resultSet.getString("tai"));
            qosSustainabilitySubscriptionData.setSnssai(resultSet.getString("snssai"));

            return qosSustainabilitySubscriptionData;
        });
    }




    public Integer getRefCount_SliceLoadLevel(String snssais) {

        String query = "SELECT refCount FROM nwdafSliceLoadLevelSubscriptionTable WHERE snssai = ?;";

        try {

            return jdbcTemplate.queryForObject(query, new Object[] {snssais} , (resultSet, i) -> resultSet.getInt("refCount"));

        } catch (EmptyResultDataAccessException e) {

            return 0;
        }
    }


    public Integer getRefCount_QosSustainability(String tai, String snssai)
    {
        String query = "SELECT refCount FROM nwdafQosSustainabilitySubscriptionTable WHERE tai = ? AND snssai = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[] {tai, snssai}, (resultSet, i) -> resultSet.getInt("refCount"));
        }

        catch(EmptyResultDataAccessException e)
        { return 0; }
    }



    public Integer getRefCount_ServiceExperience(String supi, String snssai)
    {
        String query = "SELECT refCount FROM nwdafServiceExperienceSubscriptionTable WHERE supi = ? AND snssai = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[]{supi, snssai}, (resultSet, i) -> resultSet.getInt("refCount"));
        }

        catch(EmptyResultDataAccessException e)
        { return 0; }
    }


    public Integer getRefCount_NetworkPerformance(String supi, Integer nwPerfType)
    {
        String query = "SELECT refCount FROM nwdafNetworkPerformanceSubscriptionTable WHERE supi = ? AND nwPerfType = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[]{supi, nwPerfType}, (resultSet, i) -> resultSet.getInt("refCount"));
        }

        catch(EmptyResultDataAccessException ex)
        { return 0; }
    }


    public Integer getRefCount_UserDataCongestion(String supi, Integer congType)
    {
        String query = "SELECT refCount FROM nwdafUserDataCongestionSubscriptionTable WHERE supi = ? AND congType = ?;";

        try
        { return jdbcTemplate.queryForObject(query, new Object[]{supi, congType}, (resultSet, i) -> resultSet.getInt("refCount")); }

        catch(EmptyResultDataAccessException ex)
        { return 0; }
    }


    public Integer getRefCount_AbnormalBehaviour(String supi, Integer excepId)
    {
        String query = "SELECT refCount FROM nwdafAbnormalBehaviourSubscriptionTable WHERE supi = ? AND excepId = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[]{supi, excepId}, (resultSet, i) -> resultSet.getInt("refCount"));
        }

        catch(EmptyResultDataAccessException ex)
        { return 0; }
    }




    public QosSustainabilitySubscriptionData getTai_Snssais_ByCorrelationID(String correlationId)
    {
        String query = "SELECT tai, snssai FROM nwdafQosSustainabilitySubscriptionTable WHERE correlationId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{correlationId}, (resultSet, i) -> {

            QosSustainabilitySubscriptionData qosSustainabilitySubscriptionData = new QosSustainabilitySubscriptionData();

            qosSustainabilitySubscriptionData.setTai(resultSet.getString("tai"));
            qosSustainabilitySubscriptionData.setSnssai(resultSet.getString("snssai"));

            return qosSustainabilitySubscriptionData;
        });
    }




    public Integer updateCurrentLoadLevel(Integer loadLevel, String snssai) {

        return jdbcTemplate.update("UPDATE nwdafSliceLoadLevelInformation SET currentLoadLevel = ? WHERE snssai = ?", new Object[]{loadLevel, snssai});
    }


    public String getSnssais_SliceLoadLevelSubscriptionTable(String correlationId) {

        String query = "SELECT snssai FROM nwdafSliceLoadLevelSubscriptionTable WHERE correlationId = ?";

        return jdbcTemplate.queryForObject(query, new Object[]{correlationId}, (resultSet, i) -> resultSet.getString("snssai"));
    }





    public String getCorrelationID(String unSubCorrelationId, NwdafEvent event) {

        String query = "";

        if(event == NwdafEvent.LOAD_LEVEL_INFORMATION)
        { query = "SELECT correlationId FROM nwdafSliceLoadLevelSubscriptionTable WHERE subscriptionId = ?;"; }

        else if(event == NwdafEvent.QOS_SUSTAINABILITY)
        { query = "SELECT correlationId FROM nwdafQosSustainabilitySubscriptionTable WHERE subscriptionId = ?;"; }


        try {

            return jdbcTemplate.queryForObject(query, new Object[] {unSubCorrelationId}, (resultSet, i) -> resultSet.getString("correlationId"));

        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }


    /****************************************************************************************************************/


    public List<NotificationData> getAllNotificationData(String snssai, Integer currentLoadLevel) {
        String query = "SELECT subscriptionId, loadLevelThreshold FROM nwdafSliceLoadLevelSubscriptionData WHERE snssai = ? AND loadLevelThreshold < ?;";

        try {
            return jdbcTemplate.query(query, new Object[] {snssai, currentLoadLevel} ,new NotificationDataMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }


    public Integer getMinLoadLevel(String snssai) {
        String query = "SELECT MIN(loadLevelThreshold) FROM nwdafSliceLoadLevelSubscriptionData WHERE snssai = '" + snssai + "';";

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



    /***************************************QOS_SUSTAINABILITY***************************************/




    public Boolean addDataQosSustainabilitySubscriptionData(QosSustainabilitySubscriptionData qosSubscription) throws NullPointerException
    {
        String query = "INSERT INTO nwdafQosSustainabilitySubscriptionData (subscriptionId, 5Qi, tai, snssai, ranUeThrouThrd, qosFlowRetThrd, relTimeUnit) VALUES (?, ?, ?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, qosSubscription.getSubscriptionId());
            preparedStatement.setInt(2, qosSubscription.get_5Qi());
            preparedStatement.setString(3, qosSubscription.getTai());
            preparedStatement.setString(4, qosSubscription.getSnssai());

            if(qosSubscription.getRanUeThrouThrd() == null)
            { preparedStatement.setNull(5, Types.INTEGER); }

            else
            { preparedStatement.setInt(5, qosSubscription.getRanUeThrouThrd()); }


            if(qosSubscription.getQosFlowRetThrd() == null)
            { preparedStatement.setNull(6, Types.INTEGER); }

            else
            { preparedStatement.setInt(6, qosSubscription.getQosFlowRetThrd()); }


            if(qosSubscription.getRelTimeUnit() == null)
            { preparedStatement.setNull(7, Types.VARCHAR); }

            else
            { preparedStatement.setString(7, qosSubscription.getRelTimeUnit()); }


            return preparedStatement.execute();
        });
    }


    public Boolean setNwdafQosSustainabilityInformation(String snssai, String tai) throws NullPointerException
    {
        if(taiSnssai_Exists_QosSustainability(tai, snssai, TableType.INFORMATION_TABLE))
        { return Boolean.FALSE; }


        String query = "INSERT INTO nwdafQosSustainabilityInformation (tai, snssai, ranUeThrou, qosFlowRet) VALUES(?, ?, ?, ?);";


        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, tai);
            preparedStatement.setString(2, snssai);
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);

            return preparedStatement.execute();
        });
    }





    public Boolean addDataQosSustainabilitySubscriptionTable(QosSustainabilitySubscriptionTable qos, boolean getAnalytics) {

        String query = "INSERT INTO nwdafQosSustainabilitySubscriptionTable (tai, snssai, subscriptionId, correlationId, refCount) VALUES (?,?,?,?,?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, qos.getTai());
            preparedStatement.setString(2, qos.getSnssai());
            preparedStatement.setString(3, qos.getSubscriptionId());
            preparedStatement.setString(4, qos.getCorrelationId());

            if (getAnalytics) {
                preparedStatement.setInt(5, 0);
            } else {
                preparedStatement.setInt(5, 1);
            }

            return preparedStatement.execute();
        });
    }



    public boolean taiSnssai_Exists_QosSustainability(String tai, String snssai, TableType qosTable)
    {
        String query = new String();

        switch(qosTable)
        {
            case SUBSCRIPTION_TABLE: query = "SELECT EXISTS(SELECT 1 FROM nwdafQosSustainabilitySubscriptionTable WHERE tai = ? AND snssai = ?) AS tai_snssai;";
                                     break;


            case INFORMATION_TABLE: query = "SELECT EXISTS(SELECT 1 FROM nwdafQosSustainabilityInformation WHERE tai = ? AND snssai = ?) AS tai_snssai;";
                                    break;
        }

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{tai, snssai}, (resultSet, i) -> resultSet.getInt("tai_snssai"));

        return exists == 1;
    }



    public Integer updateRefCount_QosSustainability(String tai, String snssai)
    { return jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionTable SET refCount = refCount + 1 WHERE tai = ? AND snssai = ?;", new Object[] {tai, snssai}); }



    public QosSustainabilitySubscriptionData getTaiSnssai_ByCorrelationId_QosSustainability(String correlationId)
    {
        String query = "SELECT tai, snssai FROM nwdafQosSustainabilitySubscriptionTable WHERE correlationId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{correlationId}, (resultSet, i) -> {

            QosSustainabilitySubscriptionData QosSustainabilitySubscriptionData = new QosSustainabilitySubscriptionData();

            QosSustainabilitySubscriptionData.setTai(resultSet.getString("tai"));
            QosSustainabilitySubscriptionData.setSnssai(resultSet.getString("snssai"));

            return QosSustainabilitySubscriptionData;
        });
    }



    public Integer updateQosSustainabilityThresholdLevel(String tai, String snssai, Integer threshold, QosThresholdType qosThresholdType)
    {
        switch(qosThresholdType)
        {
            case QOS_FLOW_RETAIN: return jdbcTemplate.update("UPDATE nwdafQosSustainabilityInformation SET qosFlowRet = ? WHERE tai = ? AND snssai = ?;", new Object[] {threshold, tai, snssai});

            case RAN_UE_THROUGHPUT: return jdbcTemplate.update("UPDATE nwdafQosSustainabilityInformation SET ranUeThrou = ? WHERE tai = ? AND snssai = ?;", new Object[] {threshold, tai, snssai});
        }

        return null;
    }


    public List<QosSustainabilitySubscriptionData> getCrossedThresholdSubscriptions_QosSustainability(String tai, String snssai, Integer threshold, QosThresholdType qosThresholdType)
    {
        String query = new String();

        switch(qosThresholdType)
        {
            case QOS_FLOW_RETAIN: query = "SELECT subscriptionId, qosFlowRetThrd, relTimeUnit FROM nwdafQosSustainabilitySubscriptionData WHERE tai = ? AND snssai = ? AND qosFlowRetThrd < ?;";
                                  break;

            case RAN_UE_THROUGHPUT: query = "SELECT subscriptionId, ranUeThrouThrd FROM nwdafQosSustainabilitySubscriptionData WHERE tai = ? AND snssai = ? AND ranUeThrouThrd < ?;";
                                    break;
        }

        return jdbcTemplate.query(query, new Object[] {tai, snssai, threshold}, new QosSustainabilityCrossedThresholdMapper(qosThresholdType));
    }


    public QosSustainabilitySubscriptionTable getCorrelation_UnSubCorrelation_QosSustainability(String tai, String snssai)
    {
        String query = "SELECT subscriptionId, correlationId FROM nwdafQosSustainabilitySubscriptionTable WHERE tai = ? AND snssai = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{tai, snssai}, (resultSet, i) -> {

            QosSustainabilitySubscriptionTable qosSustainabilitySubscriptionTable = new QosSustainabilitySubscriptionTable();

            qosSustainabilitySubscriptionTable.setSubscriptionId(resultSet.getString("subscriptionId"));
            qosSustainabilitySubscriptionTable.setCorrelationId(resultSet.getString("correlationId"));

            return qosSustainabilitySubscriptionTable;
        });
    }


    public List<Object> getAnalyticsInfo_QosSustainability(Integer _5Qi, String tai, String snssai)
    {
        String query = "SELECT ranUeThrouThrd, qosFlowRetThrd, relTimeUnit FROM nwdafQosSustainabilitySubscriptionData WHERE 5Qi = ? AND tai = ? AND snssai = ?;";

        try
        { return jdbcTemplate.query(query, new Object[] {_5Qi, tai, snssai}, new QosSustainabilityInfoMapper(tai)); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }



    /************************************************************************************************/





    /***************************************UE_MOBILITY**********************************************/



    public Boolean add_data_into_nwdaf_UeMobilitySubscriptionData(UEMobilitySubscriptionModel
                                                                          ue_mobilitySubscriptionModel) {

        String query = "INSERT INTO nwdafUEmobilitySubscriptionData(subscriptionId,supi) VALUES(?, ?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, ue_mobilitySubscriptionModel.getSubscriptionId());
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

        String query = "INSERT INTO nwdafUserLocation(tai,cellID,timeDuration) VALUES(?,?,?)";

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
        String query = "SELECT EXISTS(SELECT 1 FROM nwdafUserLocation WHERE tai = ? AND cellID = ?) AS tai_cellID;";

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{tai, cellID}, (resultSet, i) -> resultSet.getInt("tai_cellID"));

        return exists == 1;
    }




    public String getSupiValueByCorrelationID(String correlationId) {

        String query = "SELECT supi FROM nwdafUEmobilitySubscriptionTable WHERE correlationId = '" + correlationId + "';";

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

        String query = "SELECT ID FROM nwdafUserLocation WHERE tai = ? AND cellID = ?;";

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
        String query = "SELECT subscriptionId FROM nwdafUEmobilitySubscriptionData WHERE supi = ?;";

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[]{supi}, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("subscriptionId");
                }
            });
        }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }





    public UserLocation getUserLocationFromID(Integer ID) {


        String query = "SELECT tai, cellID, timeDuration FROM nwdafUserLocation WHERE ID = " + ID + ";";

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

        String query = "SELECT subscriptionId FROM nwdafUEmobilitySubscriptionData WHERE supi = ?";

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

        String query = "SELECT * FROM nwdafUEmobilitySubscriptionTable WHERE supi = ?;";

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


        String query = "INSERT INTO nwdafUEmobilitySubscriptionTable (supi,subscriptionId,correlationId,refCount) VALUES (?,?,?,?);";


        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {


                preparedStatement.setString(1, slice.getSupi());
                preparedStatement.setString(2, String.valueOf(slice.getSubscriptionId()));
                preparedStatement.setString(3, String.valueOf(slice.getCorrelationId()));

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


        String query = "INSERT INTO nwdafUEmobilitySubscriptionTable (supi,subscriptionId,correlationId,refCount) VALUES (?,?,?,?);";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                preparedStatement.setString(1, UEobj.getSupi());
                preparedStatement.setString(2, UEobj.getSubscriptionId());
                preparedStatement.setString(3, UEobj.getCorrelationId());

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
        String query = "SELECT supi FROM nwdafUEmobilitySubscriptionData WHERE subscriptionId = '" + subID + "';";

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




    public String unsubscribeNFForUE(String subscriptionId)  {


        String supi = getSupi_UEmobilitySubscriptionData(subscriptionId);
        jdbcTemplate.update("DELETE FROM nwdafUEmobilitySubscriptionData WHERE subscriptionId = ?", subscriptionId);

        //  jdbcTemplate.update("DELETE FROM nwdafUEmobility WHERE supi = ?", supi);

        jdbcTemplate.update("DELETE FROM nwdafSubscriptionTable WHERE subscriptionId = ?", subscriptionId);

        //return decrementRefCount(supi, eventID);
        return decrementRefCountForUE(supi);
    }



    public String decrementRefCountForUE(String supi)  {


        jdbcTemplate.update("UPDATE nwdafUEmobilitySubscriptionTable SET refCount = refCount - 1 WHERE supi = ?", supi);

        if (getRefCount_UEmobilitySubscriptionTable(supi) == 0) {
            /* time to delete the entry from db, caller shall send the subscribe message towards peer first */
            return supi;
        }

        return null;
    }




    public String getUnSubCorrelationID_UEMobility(String supi) {

        //String query = "select snssais from nwdafSliceLoadLevelSubscriptionTable where subscriptionID "
        String query = "SELECT subscriptionId FROM nwdafUEmobilitySubscriptionTable WHERE supi = '" + supi + "';";
        try {

            String unSubID = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("subscriptionId");
                }
            });
            return unSubID;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    public String getCorrelationID_UEMobility(String unSubCorrelationId) {

        String query = "SELECT correlationId FROM nwdafUEmobilitySubscriptionTable WHERE subscriptionId = '" + unSubCorrelationId + "';";

        try {

            String correlationId = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("correlationId");
                }
            });
            return correlationId;

        } catch (EmptyResultDataAccessException e) {
            // return e;
        }
        return null;

    }


    /************************************************************************************************/


    /*
    public void updateSliceLoadLevelEntry(EventSubscription subscription, int order)
    {

        if(order == 1)
        { jdbcTemplate.update("UPDATE nwdafSliceLoadLevelSubscriptionData SET loadLevelThreshold = ? WHERE subscriptionID = ?;", new Object[] { subscription.getLoadLevelThreshold(), subscription.getSubscriptionId() }); }

        if(order == 2)
        { jdbcTemplate.update("UPDATE nwdafSubscriptionTable SET notifMethod = ?, repetitionPeriod = ? WHERE subscriptionID = ?;", new Object[] { subscription.getNotificationMethod().ordinal(), subscription.getRepetitionPeriod(), subscription.getSubscriptionID() }); }

        else if(order == 3)
        { jdbcTemplate.update("UPDATE nwdafSubscriptionTable SET notifMethod = ? WHERE subscriptionID = ?;", new Object[] { subscription.getNotificationMethod(), subscription.getSubscriptionId() }); }

        else if(order == 4)
        { jdbcTemplate.update("UPDATE nwdafSubscriptionTable SET repetitionPeriod = ? WHERE subscriptionID = ?;", new Object[] { subscription.getRepetitionPeriod(), subscription.getSubscriptionID() }); }

    } */


    /*
    public void updateQosSustainabilityEntry(EventSubscription subscription, QosType qosType)
    {
        if(qosType == QosType.RAN_UE_THROUGHPUT)
        { jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionData SET ranUeThroughputThreshold = ?, qosFlowRetainThreshold = null WHERE subscriptionID = ?;", new Object[] { subscription.getRanUeThroughputThreshold(), subscription.getSubscriptionID()}); }

        else if(qosType == QosType.QOS_FLOW_RETAIN)
        { jdbcTemplate.update("UPDATE nwdafQosSustainabilitySubscriptionData SET ranUeThroughputThreshold = null, qosFlowRetainThreshold = ? WHERE subscriptionID = ?;", new Object[] { subscription.getQosFlowRetainThreshold(), subscription.getSubscriptionID()}); }
    }
     */







    /************************************SERVICE_EXPERIENCE***********************************************/




    public Boolean addServiceExperienceSubscriptionData(ServiceExperienceSubscriptionData svcExpSubscription)
    {
        String query = "INSERT INTO nwdafServiceExperienceSubscriptionData (subscriptionId, supi, snssai) VALUES (?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, svcExpSubscription.getSubscriptionId());
            preparedStatement.setString(2, svcExpSubscription.getSupi());
            preparedStatement.setString(3, svcExpSubscription.getSnssai());

            return preparedStatement.execute();
        });
    }


    public Boolean addServiceExperienceInformation(String supi, String snssai)
    {

        if(supiSnssaiExist_ServiceExperience(supi, snssai, TableType.INFORMATION_TABLE))
        { return Boolean.FALSE; }

        String query = "INSERT INTO nwdafServiceExperienceInformation (supi, snssai, mos, upperRange, lowerRange) VALUES(?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, supi);
            preparedStatement.setString(2, snssai);
            preparedStatement.setFloat(3, 0);
            preparedStatement.setFloat(4, 0);
            preparedStatement.setFloat(5, 0);

            return preparedStatement.execute();
        });
    }


    public Integer updateServiceExperienceInformation(SvcExperience svcExpInfo, String supi, String snssai)
    {
        String query = "UPDATE nwdafServiceExperienceInformation SET mos = ?, upperRange = ?, lowerRange = ? WHERE supi = ? AND snssai = ?;";
        return jdbcTemplate.update(query, new Object[] {svcExpInfo.getMos(), svcExpInfo.getUpperRange(), svcExpInfo.getLowerRange(), supi, snssai});
    }



    public Boolean addServiceExperienceSubscriptionTable(ServiceExperienceSubscriptionTable svcExp, boolean getAnalytics)
    {
        String query = "INSERT INTO nwdafServiceExperienceSubscriptionTable (supi, snssai, subscriptionId, correlationId, refCount) VALUES (?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, svcExp.getSupi());
            preparedStatement.setString(2, svcExp.getSnssai());
            preparedStatement.setString(3, svcExp.getSubscriptionId());
            preparedStatement.setString(4, svcExp.getCorrelationId());

            if(getAnalytics)
            { preparedStatement.setInt(5, 0); }

            else
            { preparedStatement.setInt(5, 1); }

            return preparedStatement.execute();
        });
    }



    public boolean supiSnssaiExist_ServiceExperience(String supi, String snssai, TableType table)
    {
        String query = "";

        if(table == TableType.SUBSCRIPTION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafServiceExperienceSubscriptionTable WHERE supi = ? AND snssai = ?) AS supi_snssai;"; }

        else if(table == TableType.INFORMATION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafServiceExperienceInformation WHERE supi = ? AND snssai = ?) AS supi_snssai;"; }

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{supi, snssai}, (resultSet, i) -> resultSet.getInt("supi_snssai"));

        return exists == 1;
    }


    public Integer updateRefCount_ServiceExperience(String supi, String snssai)
    { return jdbcTemplate.update("UPDATE nwdafServiceExperienceSubscriptionTable SET refCount = refCount + 1 WHERE supi = ? AND snssai = ?;", new Object[] {supi, snssai}); }



    public ServiceExperienceSubscriptionData getSupi_Snssais_ServiceExperience(String subscriptionId)
    {
        String query = "SELECT supi, snssai FROM nwdafServiceExperienceSubscriptionData WHERE subscriptionId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[] {subscriptionId}, (resultSet, i) -> new ServiceExperienceSubscriptionData(resultSet.getString("supi"), resultSet.getString("snssai")));
    }


    public NetworkPerformanceSubscriptionData getSupi_nwPerfType_NetworkPerformance(String subscriptionId)
    {
        String query = "SELECT supi, nwPerfType FROM nwdafNetworkPerformanceSubscriptionData WHERE subscriptionId = ?";

        return jdbcTemplate.queryForObject(query, new Object[]{subscriptionId}, (resultSet, i) -> new NetworkPerformanceSubscriptionData(resultSet.getString("supi"), resultSet.getInt("nwPerfType")));
    }


    public UserDataCongestionSubscriptionData getSupi_congType_UserDataCongestion(String subscriptionID)
    {
        String query = "SELECT supi, congType FROM nwdafUserDataCongestionSubscriptionData WHERE subscriptionId = ?";

        return jdbcTemplate.queryForObject(query, new Object[]{subscriptionID}, (resultSet, i) -> {

            UserDataCongestionSubscriptionData usrDataCongSubData = new UserDataCongestionSubscriptionData();

            usrDataCongSubData.setSupi(resultSet.getString("supi"));
            usrDataCongSubData.setCongType(resultSet.getInt("congType"));

            return usrDataCongSubData;
        });
    }


    public Integer deleteEntry_ServiceExperienceSubscriptionTable(String supi, String snssai)
    { return jdbcTemplate.update("DELETE FROM nwdafServiceExperienceSubscriptionTable WHERE supi = ? AND snssai = ?;", new Object[] {supi, snssai}); }


    public Integer deleteEntry_NetworkPerformanceSubscriptionTable(String supi, Integer nwPerfType)
    { return jdbcTemplate.update("DELETE FROM nwdafNetworkPerformanceSubscriptionTable WHERE supi = ? AND nwPerfType = ?;", new Object[] {supi, nwPerfType}); }


    public Integer deleteEntry_UserDataCongestionSubscriptionTable(String correlationId)
    { return jdbcTemplate.update("DELETE FROM nwdafUserDataCongestionSubscriptionTable WHERE correlationId = ?;", correlationId); }


    public Integer deleteEntry_QosSustainabilitySubscriptionTable(String correlationId)
    { return jdbcTemplate.update("DELETE FROM nwdafQosSustainabilitySubscriptionTable WHERE correlationId = ?;", correlationId); }


    public Integer deleteEntry_AbnormalBehaviourSubscriptionTable(String correlationId)
    { return jdbcTemplate.update("DELETE FROM nwdafAbnormalBehaviourSubscriptionTable WHERE correlationId = ?;", correlationId); }


    public Integer deleteEntry_UeCommSubscriptionTable(String correlationId)
    { return jdbcTemplate.update("DELETE FROM nwdafUeCommSubscriptionTable WHERE correlationId = ?;", correlationId); }


    public Integer deleteEntry_NfLoadSubscriptionTable(String correlationId)
    { return jdbcTemplate.update("DELETE FROM nwdafNfLoadSubscriptionTable WHERE correlationId = ?;", correlationId); }



    /****************************************************************************************************************************************/



    public Integer deleteEntry_ServiceExperienceInformation(String supi, String snssai)
    { return jdbcTemplate.update("DELETE FROM nwdafServiceExperienceInformation WHERE supi = ? AND snssai = ?;", new Object[] {supi, snssai}); }


    public Integer deleteEntry_NetworkPerformanceInformation(String supi, Integer nwPerfType)
    { return jdbcTemplate.update("DELETE FROM nwdafNetworkPerformanceInformation WHERE supi = ? AND nwPerfType = ?;", new Object[] {supi, nwPerfType}); }


    public Integer deleteEntry_UserDataCongestionInformation(String supi, Integer congType)
    { return jdbcTemplate.update("DELETE FROM nwdafUserDataCongestionInformation WHERE supi = ? AND congType = ?;", new Object[] {supi, congType}); }


    public Integer deleteEntry_QosSustainabilityInformation(String tai, String snssai)
    { return jdbcTemplate.update("DELETE FROM nwdafQosSustainabilityInformation WHERE tai = ? AND snssai = ?;", new Object[] {tai, snssai}); }


    public Integer deleteEntry_AbnormalBehaviourInformation(String supi, Integer excepId)
    { return jdbcTemplate.update("DELETE FROM nwdafAbnormalBehaviourInformation WHERE supi = ? AND excepId = ?;", new Object[] {supi, excepId}); }







    public List<ServiceExperienceInfo> getServiceExperienceInfo(String supi, String snssai, Boolean anyUe)
    {
        if(anyUe)
        { return getAllServiceExperienceInfo(); }

        String query = "SELECT * FROM nwdafServiceExperienceInformation WHERE supi = ? AND snssai = ?;";

        try
        { return jdbcTemplate.query(query, new Object[] {supi, snssai}, new ServiceExperienceInfoMapper()); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public List<ServiceExperienceInfo> getAllServiceExperienceInfo()
    {
        String query = "SELECT * FROM nwdafServiceExperienceInformation;";

        try
        { return jdbcTemplate.query(query, new ServiceExperienceInfoMapper()); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public ServiceExperienceSubscriptionTable getSupi_snssais_ServiceExperienceSubscriptionTable(String correlationId)
    {
        String query = "SELECT supi, snssai FROM nwdafServiceExperienceSubscriptionTable WHERE correlationId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{correlationId}, (resultSet, i) -> new ServiceExperienceSubscriptionTable(resultSet.getString("supi"), resultSet.getString("snssai")));
    }


    public String getSubscriptionID_ServiceExperienceSubscriptionData(String supi, String snssai)
    {
        String query = "SELECT subscriptionId FROM nwdafServiceExperienceSubscriptionData WHERE supi = ? AND snssai = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{supi, snssai}, (resultSet, i) -> resultSet.getString("subscriptionId"));
    }

    public ServiceExperienceSubscriptionTable getCorrelation_UnSubCorrelation_ServiceExperience(String supi, String snssai)
    {
        String query = "SELECT subscriptionId, correlationId FROM nwdafServiceExperienceSubscriptionTable WHERE supi = ? AND snssai = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{supi, snssai}, (resultSet, i) -> {

            ServiceExperienceSubscriptionTable svcExpRightSideData = new ServiceExperienceSubscriptionTable();

            svcExpRightSideData.setSubscriptionId(resultSet.getString("subscriptionId"));
            svcExpRightSideData.setCorrelationId(resultSet.getString("correlationId"));

            return svcExpRightSideData;
        });
    }



    /************************************NETWORK_PERFORMANCE***********************************************/


    public Boolean addNetworkPerformanceSubscriptionData(NetworkPerformanceSubscriptionData nwPerfSubscription, NetworkPerfThreshold nwPerfThreshold)
    {
        String query = "";

        if(nwPerfThreshold == NetworkPerfThreshold.ABSOLUTE_NUM)
        { query = "INSERT INTO nwdafNetworkPerformanceSubscriptionData (subscriptionId, supi, nwPerfType, absoluteNumThrd) VALUES (?, ?, ?, ?);"; }

        else if(nwPerfThreshold == NetworkPerfThreshold.RELATIVE_RATIO)
        { query = "INSERT INTO nwdafNetworkPerformanceSubscriptionData (subscriptionId, supi, nwPerfType, relativeRatioThrd) VALUES (?, ?, ?, ?);"; }

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, nwPerfSubscription.getSubscriptionId());
            preparedStatement.setString(2, nwPerfSubscription.getSupi());
            preparedStatement.setInt(3, nwPerfSubscription.getNwPerfType());

            if(nwPerfThreshold == NetworkPerfThreshold.ABSOLUTE_NUM)
            { preparedStatement.setInt(4, nwPerfSubscription.getAbsoluteNumThrd()); }

            else if(nwPerfThreshold == NetworkPerfThreshold.RELATIVE_RATIO)
            { preparedStatement.setInt(4, nwPerfSubscription.getRelativeRatioThrd()); }

            return preparedStatement.execute();
        });
    }



    public Boolean addNetworkPerformanceInformation(String supi, Integer nwPerfType)
    {

        if(supi_nwPerfTypeExists(supi, nwPerfType, TableType.INFORMATION_TABLE))
        { return Boolean.FALSE; }

        String query = "INSERT INTO nwdafNetworkPerformanceInformation (supi, nwPerfType, relativeRatio, absoluteNum) VALUES(?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, supi);
            preparedStatement.setInt(2, nwPerfType);
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);

            return preparedStatement.execute();
        });
    }



    public Boolean addNetworkPerformanceSubscriptionTable(NetworkPerformanceSubscriptionTable nwPerfSubTable, boolean getAnalytics)
    {
        String query = "INSERT INTO nwdafNetworkPerformanceSubscriptionTable (supi, nwPerfType, subscriptionId, correlationId, refCount) VALUES (?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, nwPerfSubTable.getSupi());
            preparedStatement.setInt(2, nwPerfSubTable.getNwPerfType());
            preparedStatement.setString(3, nwPerfSubTable.getSubscriptionId());
            preparedStatement.setString(4, nwPerfSubTable.getCorrelationId());

            if(getAnalytics)
            { preparedStatement.setInt(5, 0); }

            else
            { preparedStatement.setInt(5, 1); }

            return preparedStatement.execute();
        });
    }





    public boolean supi_nwPerfTypeExists(String supi, Integer nwPerfType, TableType nwPerfTable)
    {
        String query = "";

        if(nwPerfTable == TableType.SUBSCRIPTION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafNetworkPerformanceSubscriptionTable WHERE supi = ? AND nwPerfType = ?) AS supi_nwPerfType;"; }

        else if(nwPerfTable == TableType.INFORMATION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafNetworkPerformanceInformation WHERE supi = ? AND nwPerfType = ?) AS supi_nwPerfType;"; }

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{supi, nwPerfType}, (resultSet, i) -> resultSet.getInt("supi_nwPerfType"));

        return exists == 1;
    }


    public NetworkPerformanceSubscriptionData getSupi_nwPerfType_byCorrelationID(String correlationID)
    {
        String query = "SELECT supi, nwPerfType FROM nwdafNetworkPerformanceSubscriptionTable WHERE correlationId = ?";

        return jdbcTemplate.queryForObject(query, new Object[]{correlationID}, (resultSet, i) -> new NetworkPerformanceSubscriptionData(resultSet.getString("supi"), resultSet.getInt("nwPerfType")));
    }


    public Integer updateRelativeRatio_NetworkPerf(Integer relativeRatio, String supi, Integer nwPerfType)
    { return jdbcTemplate.update("UPDATE nwdafNetworkPerformanceInformation SET relativeRatio = ? WHERE supi = ? AND nwPerfType = ?;", new Object[] {relativeRatio, supi, nwPerfType}); }

    public Integer updateAbsoluteNum_NetworkPerf(Integer absoluteNum, String supi, Integer nwPerfType)
    { return jdbcTemplate.update("UPDATE nwdafNetworkPerformanceInformation SET absoluteNum = ? WHERE supi = ? AND nwPerfType = ?;", new Object[] {absoluteNum, supi, nwPerfType}); }



    public NetworkPerformanceSubscriptionData getCrossedNwPerfType_subscriptionID_NetworkPerf(String supi, Integer nwPerfType, Integer threshold, NetworkPerfThreshold nwPerfThreshold)
    {
        String query = "";

        if(nwPerfThreshold == NetworkPerfThreshold.RELATIVE_RATIO)
        { query = "SELECT nwPerfType, subscriptionID FROM nwdafNetworkPerformanceSubscriptionData WHERE supi = ? AND nwPerfType = ? AND relativeRatioThrd < ?;"; }

        else if(nwPerfThreshold == NetworkPerfThreshold.ABSOLUTE_NUM)
        { query = "SELECT nwPerfType, subscriptionID FROM nwdafNetworkPerformanceSubscriptionData WHERE supi = ? AND nwPerfType = ? AND absoluteNumThrd  < ?;"; }

        try
        {
            return jdbcTemplate.queryForObject(query, new Object[]{supi, nwPerfType, threshold}, (resultSet, i) -> {

                NetworkPerformanceSubscriptionData nwPerfSubData = new NetworkPerformanceSubscriptionData();

                nwPerfSubData.setSubscriptionId(resultSet.getString("subscriptionId"));
                nwPerfSubData.setNwPerfType(resultSet.getInt("nwPerfType"));

                return nwPerfSubData;
            });
        }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public NetworkPerformanceSubscriptionTable getCorrelation_UnSubCorrelation_NetworkPerformance(String supi, Integer nwPerfType)
    {
        String query = "SELECT subscriptionId, correlationId FROM nwdafNetworkPerformanceSubscriptionTable WHERE supi = ? AND nwPerfType = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{supi, nwPerfType}, (resultSet, i) -> {

            NetworkPerformanceSubscriptionTable nwPerfSubTable = new NetworkPerformanceSubscriptionTable();

            nwPerfSubTable.setSubscriptionId(resultSet.getString("subscriptionId"));
            nwPerfSubTable.setCorrelationId(resultSet.getString("correlationId"));

            return nwPerfSubTable;
        });
    }



    public UserDataCongestionSubscriptionTable getCorrelation_UnSubCorrelation_UserDataCongestion(String supi, Integer congType)
    {
        String query = "SELECT subscriptionId, correlationId FROM nwdafUserDataCongestionSubscriptionTable WHERE supi = ? AND congType = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{supi, congType}, (resultSet, i) -> {

            UserDataCongestionSubscriptionTable usrDataCongSubTable = new UserDataCongestionSubscriptionTable();

            usrDataCongSubTable.setSubscriptionId(resultSet.getString("subscriptionId"));
            usrDataCongSubTable.setCorrelationId(resultSet.getString("correlationId"));

            return usrDataCongSubTable;
        });
    }




    public AbnormalBehaviourSubscriptionTable getCorrelation_UnSubCorrelation_AbnormalBehaviour(String supi, Integer excepId)
    {
        String query = "SELECT subscriptionId, correlationId FROM nwdafAbnormalBehaviourSubscriptionTable WHERE supi = ? AND excepId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{supi, excepId}, (resultSet, i) -> {

            AbnormalBehaviourSubscriptionTable abnorBehavrSubTable = new AbnormalBehaviourSubscriptionTable();

            abnorBehavrSubTable.setSubscriptionId(resultSet.getString("subscriptionId"));
            abnorBehavrSubTable.setCorrelationId(resultSet.getString("correlationId"));

            return abnorBehavrSubTable;
        });
    }




    public List<Object> getNetworkPerformanceInfo(String supi, Integer nwPerfType, Boolean anyUe)
    {
        if(anyUe)
        { return getAllNetworkPerformanceInfo(); }

        String query = "SELECT * FROM nwdafNetworkPerformanceInformation WHERE supi = ? AND nwPerfType = ?;";

        try
        { return jdbcTemplate.query(query, new Object[] {supi, nwPerfType}, new NetworkPerformanceInfoMapper()); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public List<Object> getAllNetworkPerformanceInfo()
    {
        String query = "SELECT * FROM nwdafNetworkPerformanceInformation;";

        try
        { return jdbcTemplate.query(query, new NetworkPerformanceInfoMapper()); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public Integer updateRefCount_NetworkPerformance(String supi, Integer nwPerfType)
    { return jdbcTemplate.update("UPDATE nwdafNetworkPerformanceSubscriptionTable SET refCount = refCount + 1 WHERE supi = ? AND nwPerfType = ?;", new Object[] {supi, nwPerfType}); }


    public Integer deleteAnalyticsEntry_NetworkPerformance(String correlationId)
    { return jdbcTemplate.update("DELETE FROM nwdafNetworkPerformanceSubscriptionTable WHERE correlationId = ? AND refCount = 0;", correlationId); }




    /************************************USER_DATA_CONGESTION***********************************************/


    public Boolean addUserDataCongestionSubscriptionData(UserDataCongestionSubscriptionData usrDataCongSubscription)
    {
        String query = "INSERT INTO nwdafUserDataCongestionSubscriptionData (subscriptionId, supi, congType, congThreshold) VALUES (?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, usrDataCongSubscription.getSubscriptionId());
            preparedStatement.setString(2, usrDataCongSubscription.getSupi());
            preparedStatement.setInt(3, usrDataCongSubscription.getCongType());
            preparedStatement.setInt(4, usrDataCongSubscription.getCongThreshold());

            return preparedStatement.execute();
        });
    }


    public Boolean addUserDataCongestionInformation(String supi, Integer congType, String tai)
    {
        if(userDataCongDetailsExist(supi, congType, tai, TableType.INFORMATION_TABLE))
        { return Boolean.FALSE; }

        String query = "INSERT INTO nwdafUserDataCongestionInformation (supi, congType, tai, congLevel) VALUES (?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, supi);
            preparedStatement.setInt(2, congType);
            preparedStatement.setString(3, tai);
            preparedStatement.setInt(4, 0);

            return preparedStatement.execute();
        });
    }


    public Boolean addUserDataCongestionSubscriptionTable(UserDataCongestionSubscriptionTable usrDataCongSubTable, boolean getAnalytics)
    {
        String query = "INSERT INTO nwdafUserDataCongestionSubscriptionTable (supi, congType, tai, subscriptionId, correlationId, refCount) VALUES (?, ?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, usrDataCongSubTable.getSupi());
            preparedStatement.setInt(2, usrDataCongSubTable.getCongType());
            preparedStatement.setString(3, usrDataCongSubTable.getTai());
            preparedStatement.setString(4, usrDataCongSubTable.getSubscriptionId());
            preparedStatement.setString(5, usrDataCongSubTable.getCorrelationId());

            if(getAnalytics)
            { preparedStatement.setInt(6, 0); }

            else
            { preparedStatement.setInt(6, 1); }

            return preparedStatement.execute();
        });
    }


    public boolean userDataCongDetailsExist(String supi, Integer congType, String tai, TableType usrCongTable)
    {
        String query = "";

        if(usrCongTable == TableType.SUBSCRIPTION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafUserDataCongestionSubscriptionTable WHERE supi = ? AND congType = ? AND tai = ?) AS usrDataCong_details;"; }

        else if(usrCongTable == TableType.INFORMATION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafUserDataCongestionInformation WHERE supi = ? AND congType = ? AND tai = ?) AS usrDataCong_details;"; }

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{supi, congType, tai}, (resultSet, i) -> resultSet.getInt("usrDataCong_details"));

        return exists == 1;
    }


    public Integer updateRefCount_UserDataCongestion(String supi, Integer congType, String tai)
    { return jdbcTemplate.update("UPDATE nwdafUserDataCongestionSubscriptionTable SET refCount = refCount + 1 WHERE supi = ? AND congType = ? AND tai = ?;", new Object[] {supi, congType, tai}); }



    public UserDataCongestionSubscriptionTable getUserDataCongestionDetails_byCorrelationID(String correlationID)
    {
        String query = "SELECT supi, congType, tai FROM nwdafUserDataCongestionSubscriptionTable WHERE correlationId = ?";

        return jdbcTemplate.queryForObject(query, new Object[]{correlationID}, (resultSet, i) -> {

            UserDataCongestionSubscriptionTable usrDataCongDetails = new UserDataCongestionSubscriptionTable();

            usrDataCongDetails.setSupi(resultSet.getString("supi"));
            usrDataCongDetails.setCongType(resultSet.getInt("congType"));
            usrDataCongDetails.setTai(resultSet.getString("tai"));

            return usrDataCongDetails;
        });
    }


    public Integer updateCongestionLevel_UserDataCongestion(Integer congLevel, String supi, Integer congType, String tai)
    { return jdbcTemplate.update("UPDATE nwdafUserDataCongestionInformation SET congLevel = ? WHERE supi = ? AND congType = ? AND tai = ?;", new Object[] {congLevel, supi, congType, tai}); }


    public boolean congestionLevelThresholdCrossed(String supi, Integer congType, Integer congLevel)
    {
        String query = "SELECT EXISTS(SELECT 1 FROM nwdafUserDataCongestionSubscriptionData WHERE supi = ? AND congType = ? AND congThreshold < ?) AS crossedThreshold;";
        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{supi, congType, congLevel}, (resultSet, i) -> resultSet.getInt("crossedThreshold"));

        return exists == 1;
    }


    public String getSubscriptionID_UserDataCongestion(String supi, Integer congType)
    {
        String query = "SELECT subscriptionID FROM nwdafUserDataCongestionSubscriptionData WHERE supi = ? AND congType = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{supi, congType}, (resultSet, i) -> resultSet.getString("subscriptionId"));
    }


    public List<Object> getUserDataCongestionInfo(String supi, String tai, Integer congType)
    {
        String query = "SELECT * FROM nwdafUserDataCongestionInformation WHERE supi = ? AND tai = ? AND congType = ?;";

        try
        { return jdbcTemplate.query(query, new Object[] {supi, tai, congType}, new UserDataCongestionInfoMapper()); }

        catch (EmptyResultDataAccessException ex)
        { return null; }
    }


    public String getNetworkInfo_UserDataCongestion(String supi, Integer congType)
    {
        String query = "SELECT tai FROM nwdafUserDataCongestionInformation WHERE supi = ? AND congType = ?";

        try
        { return jdbcTemplate.queryForObject(query, new Object[]{supi, congType}, (resultSet, i) -> resultSet.getString("tai")); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }

    public Integer deleteAnalyticsEntry_UserDataCongestion(String correlationId)
    { return jdbcTemplate.update("DELETE FROM nwdafUserDataCongestionSubscriptionTable WHERE correlationId = ? AND refCount = 0;", correlationId); }





    /************************************ABNORMAL_BEHAVIOUR***********************************************/


    public Boolean addAbnormalBehaviourSubscriptionData(AbnormalBehaviourSubscriptionData abnorBehavrSubscription)
    {
        String query = "INSERT INTO nwdafAbnormalBehaviourSubscriptionData (subscriptionId, supi, excepId, excepLevelThrd) VALUES (?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, abnorBehavrSubscription.getSubscriptionId());
            preparedStatement.setString(2, abnorBehavrSubscription.getSupi());
            preparedStatement.setInt(3, abnorBehavrSubscription.getExcepId());
            preparedStatement.setInt(4, abnorBehavrSubscription.getExcepLevelThrd());

            return preparedStatement.execute();
        });
    }



    public Boolean addAbnormalBehaviourInformation(String supi, Integer excepId)
    {
        if(abnorBehavrDetailsExist(supi, excepId, TableType.INFORMATION_TABLE))
        { return Boolean.FALSE; }

        String query = "INSERT INTO nwdafAbnormalBehaviourInformation (supi, excepId, excepLevel) VALUES (?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, supi);
            preparedStatement.setInt(2, excepId);
            preparedStatement.setInt(3, 0);

            return preparedStatement.execute();
        });
    }



    public boolean abnorBehavrDetailsExist(String supi, Integer excepId, TableType abnorBehavrTable)
    {
        String query = "";

        if(abnorBehavrTable == TableType.SUBSCRIPTION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafAbnormalBehaviourSubscriptionTable WHERE supi = ? AND excepId = ?) AS abnorBehavr_details;"; }

        else if(abnorBehavrTable == TableType.INFORMATION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafAbnormalBehaviourInformation WHERE supi = ? AND excepId = ?) AS abnorBehavr_details;"; }

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{supi, excepId}, (resultSet, i) -> resultSet.getInt("abnorBehavr_details"));

        return exists == 1;
    }



    public Boolean addAbnormalBehaviourSubscriptionTable(AbnormalBehaviourSubscriptionTable abnorBehavrSubTable, boolean getAnalytics)
    {
        String query = "INSERT INTO nwdafAbnormalBehaviourSubscriptionTable (supi, excepId, subscriptionId, correlationId, refCount) VALUES (?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, abnorBehavrSubTable.getSupi());
            preparedStatement.setInt(2, abnorBehavrSubTable.getExcepId());
            preparedStatement.setString(3, abnorBehavrSubTable.getSubscriptionId());
            preparedStatement.setString(4, abnorBehavrSubTable.getCorrelationId());

            if(getAnalytics)
            { preparedStatement.setInt(5, 0); }

            else
            { preparedStatement.setInt(5, 1); }

            return preparedStatement.execute();
        });
    }


    public Integer updateRefCount_AbnormalBehaviour(String supi, Integer excepId)
    { return jdbcTemplate.update("UPDATE nwdafAbnormalBehaviourSubscriptionTable SET refCount = refCount + 1 WHERE supi = ? AND excepId = ?;", new Object[] {supi, excepId}); }


    public Integer updateExcepLevel_AbnormalBehaviour(Integer excepLevel, String supi, Integer excepId)
    { return jdbcTemplate.update("UPDATE nwdafAbnormalBehaviourInformation SET excepLevel = ? WHERE supi = ? AND excepId = ?;", new Object[] {excepLevel, supi, excepId}); }


    public AbnormalBehaviourSubscriptionData getSupi_ExcepId_ByCorrelationId_AbnormalBehaviour(String correlationId)
    {
        String query = "SELECT supi, excepId FROM nwdafAbnormalBehaviourSubscriptionTable WHERE correlationId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{correlationId}, (resultSet, i) -> {

            AbnormalBehaviourSubscriptionData abnormalBehaviourSubscriptionData = new AbnormalBehaviourSubscriptionData();

            abnormalBehaviourSubscriptionData.setSupi(resultSet.getString("supi"));
            abnormalBehaviourSubscriptionData.setExcepId(resultSet.getInt("excepId"));

            return abnormalBehaviourSubscriptionData;
        });
    }

    public boolean thresholdCrossed_AbnormalBehaviour(String supi, Integer excepId, Integer excepLevel)
    {
        String query = "SELECT EXISTS(SELECT 1 FROM nwdafAbnormalBehaviourSubscriptionData WHERE supi = ? AND excepId = ? AND excepLevelThrd < ?) AS crossedThreshold;";
        Integer thresholdCrossed = jdbcTemplate.queryForObject(query, new Object[]{supi, excepId, excepLevel}, (resultSet, i) -> resultSet.getInt("crossedThreshold"));

        return thresholdCrossed == 1;
    }

    public String getSubscriptionId_AbnormalBehaviour(String supi, Integer excepId)
    {
        String query = "SELECT subscriptionId FROM nwdafAbnormalBehaviourSubscriptionData WHERE supi = ? AND excepId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{supi, excepId}, (resultSet, i) -> resultSet.getString("subscriptionId"));
    }


    public List<Object> getAbnormalBehaviourInfo(String supi, Integer excepId)
    {
        String query = "SELECT * FROM nwdafAbnormalBehaviourInformation WHERE supi = ? AND excepId = ?;";

        try
        { return jdbcTemplate.query(query, new Object[] {supi, excepId}, new AbnormalBehaviourInfoMapper()); }

        catch (EmptyResultDataAccessException ex)
        { return null; }
    }



    public String getSupi_AbnormalBehaviour(String subscriptionId)
    {
        String query = "SELECT supi FROM nwdafAbnormalBehaviourSubscriptionData WHERE subscriptionId = ? LIMIT 1;";
        return jdbcTemplate.queryForObject(query, new Object[]{subscriptionId}, (resultSet, i) -> resultSet.getString("supi"));
    }

    public List<Integer> getExcepIds_AbnormalBehaviour(String subscriptionId)
    {
        String query = "SELECT excepId FROM nwdafAbnormalBehaviourSubscriptionData WHERE subscriptionId = ?;";
        return jdbcTemplate.query(query, new Object[]{subscriptionId}, (resultSet, i) -> resultSet.getInt("excepId"));
    }



    /************************************UE_COMM***********************************************/




    public Boolean addUeCommSubscriptionData(UeCommSubscriptionData ueCommSubscriptionData)
    {
        String query = "INSERT INTO nwdafUeCommSubscriptionData (subscriptionId, supi, maxAnaEntry) VALUES (?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, ueCommSubscriptionData.getSubscriptionId());
            preparedStatement.setString(2, ueCommSubscriptionData.getSupi());
            preparedStatement.setInt(3, ueCommSubscriptionData.getMaxAnaEntry());

            return preparedStatement.execute();
        });
    }


    public Boolean addUeCommInformation(UeCommInformation ueCommInformation)
    {
        String query = "INSERT INTO nwdafUeCommInformation (supi, commDur, ts, ulVol, dlVol) VALUES (?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, ueCommInformation.getSupi());
            preparedStatement.setInt(2, ueCommInformation.getCommDur());
            preparedStatement.setString(3, ueCommInformation.getTs());
            preparedStatement.setInt(4, ueCommInformation.getUlVol());
            preparedStatement.setInt(5, ueCommInformation.getDlVol());

            return preparedStatement.execute();
        });
    }


    public Boolean addUeCommSubscriptionTable(UeCommSubscriptionTable ueCommSubscriptionTable, boolean getAnalytics)
    {
        String query = "INSERT INTO nwdafUeCommSubscriptionTable (supi, subscriptionId, correlationId, refCount) VALUES (?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, ueCommSubscriptionTable.getSupi());
            preparedStatement.setString(2, ueCommSubscriptionTable.getSubscriptionId());
            preparedStatement.setString(3, ueCommSubscriptionTable.getCorrelationId());

            if(getAnalytics)
            { preparedStatement.setInt(4, 0); }

            else
            { preparedStatement.setInt(4, 1); }

            return preparedStatement.execute();
        });
    }


    public boolean ueComm_SupiExists(String supi, TableType ueCommTable)
    {
        String query = "";

        if(ueCommTable == TableType.SUBSCRIPTION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafUeCommSubscriptionTable WHERE supi = ?) AS supi_exists;"; }

        else if(ueCommTable == TableType.INFORMATION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafUeCommInformation WHERE supi = ?) AS supi_exists;"; }

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{supi}, (resultSet, i) -> resultSet.getInt("supi_exists"));

        return exists == 1;
    }


    public Integer updateRefCount_UeComm(String supi)
    { return jdbcTemplate.update("UPDATE nwdafUeCommSubscriptionTable SET refCount = refCount + 1 WHERE supi = ?;", supi); }


    public String getSupiByCorrelationId_UeComm(String correlationId)
    {
        String query = "SELECT supi FROM nwdafUeCommSubscriptionTable WHERE correlationId = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{correlationId}, (resultSet, i) -> resultSet.getString("supi"));
    }


    public String getSubscriptionBySupi_UeComm(String supi)
    {
        String query = "SELECT subscriptionId FROM nwdafUeCommSubscriptionData WHERE supi = ?;";
        return jdbcTemplate.queryForObject(query, new Object[]{supi}, (resultSet, i) -> resultSet.getString("subscriptionId"));
    }


    public List<Object> getUeCommAnalytics(String supi, Integer maxAnaEntry)
    {
        String query = "SELECT commDur, ts, ulVol, dlVol FROM nwdafUeCommInformation WHERE supi = ? LIMIT ?";

        try
        { return jdbcTemplate.query(query, new Object[]{supi, maxAnaEntry}, new UeCommInfoMapper()); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public Integer deleteAnalyticsEntry_UeComm(String correlationId)
    { return jdbcTemplate.update("DELETE FROM nwdafUeCommSubscriptionTable WHERE correlationId = ? AND refCount = 0;", correlationId); }


    public Integer getSupiTimeSlotsCount_UeComm(String supi)
    {
        String query = "SELECT COUNT(ID) FROM nwdafUeCommInformation WHERE supi = ?;";
        return jdbcTemplate.queryForObject(query, new Object[]{supi}, (resultSet, i) -> resultSet.getInt("COUNT(ID)"));
    }

    public boolean maxAnaEntryReached(String supi, Integer anaEntry)
    {
        String query = "SELECT EXISTS(SELECT 1 FROM nwdafUeCommSubscriptionData WHERE supi = ? AND maxAnaEntry = ?) AS maxEntries;";
        Integer reached = jdbcTemplate.queryForObject(query, new Object[]{supi, anaEntry}, (resultSet, i) -> resultSet.getInt("maxEntries"));

        return reached == 1;
    }

    public Integer deleteLRUtimeSlot_UeComm(String supi)
    { return jdbcTemplate.update("DELETE FROM nwdafUeCommInformation WHERE supi = ? ORDER BY ID LIMIT 1;", supi); }


    public String getSupi_UeCommSubscriptionData(String subscriptionId)
    {
        String query = "SELECT supi FROM nwdafUeCommSubscriptionData WHERE subscriptionId = ?;";
        return jdbcTemplate.queryForObject(query, new Object[]{subscriptionId}, (resultSet, i) -> resultSet.getString("supi"));
    }

    public Integer getRefCount_UeComm(String supi)
    {
        String query = "SELECT refCount FROM nwdafUeCommSubscriptionTable WHERE supi = ?;";
        return jdbcTemplate.queryForObject(query, new Object[]{supi}, (resultSet, i) -> resultSet.getInt("refCount"));
    }


    public UeCommSubscriptionTable getCorrelation_UnSubCorrelation_UeComm(String supi)
    {
        String query = "SELECT subscriptionId, correlationId FROM nwdafUeCommSubscriptionTable WHERE supi = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{supi}, (resultSet, i) -> {

            UeCommSubscriptionTable ueCommSubscriptionTable = new UeCommSubscriptionTable();

            ueCommSubscriptionTable.setSubscriptionId(resultSet.getString("subscriptionId"));
            ueCommSubscriptionTable.setCorrelationId(resultSet.getString("correlationId"));

            return ueCommSubscriptionTable;
        });
    }


    public Integer deleteEntry_UeCommInformation(String supi)
    { return jdbcTemplate.update("DELETE FROM nwdafUeCommInformation WHERE supi = ?;", supi); }











    /************************************NF_LOAD***********************************************/



    public Boolean addNfLoadSubscriptionData(NfLoadSubscriptionData nfLoadSubscriptionData)
    {
        String query = "INSERT INTO nwdafNfLoadSubscriptionData (subscriptionId, nfType, nfInstanceId, supi, snssai, nfLoadLevelThrd, nfCpuUsageThrd, nfMemoryUsageThrd, nfStorageUsageThrd) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setString(1, nfLoadSubscriptionData.getSubscriptionId());
            preparedStatement.setInt(2, nfLoadSubscriptionData.getNfType());
            preparedStatement.setString(3, nfLoadSubscriptionData.getNfInstanceId());


            if(nfLoadSubscriptionData.getSupi() == null)
            { preparedStatement.setNull(4, Types.VARCHAR); }

            else
            { preparedStatement.setString(4, nfLoadSubscriptionData.getSupi()); }



            if(nfLoadSubscriptionData.getSnssai() == null)
            { preparedStatement.setNull(5, Types.VARCHAR); }

            else
            { preparedStatement.setString(5, nfLoadSubscriptionData.getSnssai()); }



            if(nfLoadSubscriptionData.getNfLoadLevelThrd() == null)
            { preparedStatement.setNull(6, Types.INTEGER); }

            else
            { preparedStatement.setInt(6, nfLoadSubscriptionData.getNfLoadLevelThrd()); }


            if(nfLoadSubscriptionData.getNfCpuUsageThrd() == null)
            { preparedStatement.setNull(7, Types.INTEGER); }

            else
            { preparedStatement.setInt(7, nfLoadSubscriptionData.getNfCpuUsageThrd()); }


            if(nfLoadSubscriptionData.getNfMemoryUsageThrd() == null)
            { preparedStatement.setNull(8, Types.INTEGER); }

            else
            { preparedStatement.setInt(8, nfLoadSubscriptionData.getNfMemoryUsageThrd()); }


            if(nfLoadSubscriptionData.getNfStorageUsageThrd() == null)
            { preparedStatement.setNull(9, Types.INTEGER); }

            else
            { preparedStatement.setInt(9, nfLoadSubscriptionData.getNfStorageUsageThrd()); }

            return preparedStatement.execute();
        });
    }



    public Boolean addNfLoadInformation(NfLoadInformation nfLoadInformation)
    {
        if(nfLoad_NfType_NfInstanceIdExists(nfLoadInformation.getNfType(), nfLoadInformation.getNfInstanceId(), TableType.INFORMATION_TABLE))
        { return Boolean.FALSE; }

        String query = "INSERT INTO nwdafNfLoadInformation (nfType, nfInstanceId, nfLoadLevel, nfCpuUsage, nfMemoryUsage, nfStorageUsage) VALUES (?, ?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setInt(1, nfLoadInformation.getNfType());
            preparedStatement.setString(2, nfLoadInformation.getNfInstanceId());

            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);

            return preparedStatement.execute();
        });
    }


    public Boolean addNfLoadSubscriptionTable(NfLoadSubscriptionTable nfLoadSubscriptionTable, boolean getAnalytics)
    {
        String query = "INSERT INTO nwdafNfLoadSubscriptionTable (nfType, nfInstanceId, subscriptionId, correlationId, refCount) VALUES (?, ?, ?, ?, ?);";

        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {

            preparedStatement.setInt(1, nfLoadSubscriptionTable.getNfType());
            preparedStatement.setString(2, nfLoadSubscriptionTable.getNfInstanceId());
            preparedStatement.setString(3, nfLoadSubscriptionTable.getSubscriptionId());
            preparedStatement.setString(4, nfLoadSubscriptionTable.getCorrelationId());

            if(getAnalytics)
            { preparedStatement.setInt(5, 0);  }

            else
            { preparedStatement.setInt(5, 1); }

            return preparedStatement.execute();
        });
    }


    public boolean nfLoad_NfType_NfInstanceIdExists(Integer nfType, String nfInstanceId, TableType nfLoadTable)
    {
        String query = "";

        if(nfLoadTable == TableType.SUBSCRIPTION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafNfLoadSubscriptionTable WHERE nfType = ? AND nfInstanceId = ?) AS nfInfo;"; }

        else if(nfLoadTable == TableType.INFORMATION_TABLE)
        { query = "SELECT EXISTS(SELECT 1 FROM nwdafNfLoadInformation WHERE nfType = ? AND nfInstanceId = ?) AS nfInfo;"; }

        Integer exists = jdbcTemplate.queryForObject(query, new Object[]{nfType, nfInstanceId}, (resultSet, i) -> resultSet.getInt("nfInfo"));

        return exists == 1;
    }


    public Integer updateRefCount_NfLoad(Integer nfType, String nfInstanceId)
    { return jdbcTemplate.update("UPDATE nwdafNfLoadSubscriptionTable SET refCount = refCount + 1 WHERE nfType = ? AND nfInstanceId = ?;", new Object[] {nfType, nfInstanceId}); }



    public NfLoadSubscriptionData getNfType_NfInstanceId_ByCorrelationId(String correlationId)
    {
        String query = "SELECT nfType, nfInstanceId FROM nwdafNfLoadSubscriptionTable WHERE correlationId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{correlationId}, (resultSet, i) -> {

            NfLoadSubscriptionData nfLoadSubscriptionData = new NfLoadSubscriptionData();

            nfLoadSubscriptionData.setNfType(resultSet.getInt("nfType"));
            nfLoadSubscriptionData.setNfInstanceId(resultSet.getString("nfInstanceId"));

            return nfLoadSubscriptionData;
        });
    }


    public Integer updateNfThresholdLevel(Integer nfType, String nfInstanceId, Integer threshold, NfThresholdType thresholdType)
    {
        switch(thresholdType)
        {
            case CPU_USAGE: return jdbcTemplate.update("UPDATE nwdafNfLoadInformation SET nfCpuUsage = ? WHERE nfType = ? AND nfInstanceId = ?;", new Object[] {threshold, nfType, nfInstanceId});

            case MEMORY_USAGE: return jdbcTemplate.update("UPDATE nwdafNfLoadInformation SET nfMemoryUsage = ? WHERE nfType = ? AND nfInstanceId = ?;", new Object[] {threshold, nfType, nfInstanceId});

            case STORAGE_USAGE: return jdbcTemplate.update("UPDATE nwdafNfLoadInformation SET nfStorageUsage = ? WHERE nfType = ? AND nfInstanceId = ?;", new Object[] {threshold, nfType, nfInstanceId});

            case LOAD_LEVEL: return jdbcTemplate.update("UPDATE nwdafNfLoadInformation SET nfLoadLevel = ? WHERE nfType = ? AND nfInstanceId = ?;", new Object[] {threshold, nfType, nfInstanceId});
        }

        return null;
    }


    public List<NfLoadSubscriptionData> getCrossedThresholdSubscriptions_NfLoad(Integer nfType, String nfInstanceId, Integer threshold, NfThresholdType thresholdType)
    {
        String query = "";

        switch(thresholdType)
        {
            case CPU_USAGE: query = "SELECT subscriptionId, nfCpuUsageThrd FROM nwdafNfLoadSubscriptionData WHERE nfType = ? AND nfInstanceId = ? AND nfCpuUsageThrd < ?;";
                            break;

            case MEMORY_USAGE: query = "SELECT subscriptionId, nfMemoryUsageThrd FROM nwdafNfLoadSubscriptionData WHERE nfType = ? AND nfInstanceId = ? AND nfMemoryUsageThrd < ?;";
                               break;

            case STORAGE_USAGE: query = "SELECT subscriptionId, nfStorageUsageThrd FROM nwdafNfLoadSubscriptionData WHERE nfType = ? AND nfInstanceId = ? AND nfStorageUsageThrd < ?;";
                                break;

            case LOAD_LEVEL: query = "SELECT subscriptionId, nfLoadLevelThrd FROM nwdafNfLoadSubscriptionData WHERE nfType = ? AND nfInstanceId = ? AND nfLoadLevelThrd < ?;";
                             break;
        }

        return jdbcTemplate.query(query, new Object[] {nfType, nfInstanceId, threshold}, new NfLoadCrossedThresholdMapper(thresholdType));
    }


    public List<Object> getNfLoadAnalytics(Integer nfType, String nfInstanceId)
    {
        String query = "SELECT * FROM nwdafNfLoadInformation WHERE nfType = ? AND nfInstanceId = ?;";

        try
        { return jdbcTemplate.query(query, new Object[] {nfType, nfInstanceId}, new NfLoadLevelInfoMapper()); }

        catch(EmptyResultDataAccessException ex)
        { return null; }
    }


    public NfLoadSubscriptionData getNfType_NfInstanceId_NfLoadSubscriptionData(String subscriptionId)
    {
        String query = "SELECT nfType, nfInstanceId FROM nwdafNfLoadSubscriptionData WHERE subscriptionId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{subscriptionId}, (resultSet, i) -> {

            NfLoadSubscriptionData nfLoadSubscriptionData = new NfLoadSubscriptionData();

            nfLoadSubscriptionData.setNfType(resultSet.getInt("nfType"));
            nfLoadSubscriptionData.setNfInstanceId(resultSet.getString("nfInstanceId"));

            return nfLoadSubscriptionData;
        });
    }

    public Integer getRefCount_NfLoad(Integer nfType, String nfInstanceId)
    {
        String query = "SELECT refCount FROM nwdafNfLoadSubscriptionTable WHERE nfType = ? AND nfInstanceId = ?;";
        return jdbcTemplate.queryForObject(query, new Object[]{nfType, nfInstanceId}, (resultSet, i) -> resultSet.getInt("refCount"));
    }


    public NfLoadSubscriptionTable getCorrelation_UnSubCorrelation_NfLoad(Integer nfType, String nfInstanceId)
    {
        String query = "SELECT subscriptionId, correlationId FROM nwdafNfLoadSubscriptionTable WHERE nfType = ? AND nfInstanceId = ?;";

        return jdbcTemplate.queryForObject(query, new Object[]{nfType, nfInstanceId}, (resultSet, i) -> {

            NfLoadSubscriptionTable nfLoadSubscriptionTable = new NfLoadSubscriptionTable();

            nfLoadSubscriptionTable.setSubscriptionId(resultSet.getString("subscriptionId"));
            nfLoadSubscriptionTable.setCorrelationId(resultSet.getString("correlationId"));

            return nfLoadSubscriptionTable;
        });
    }

    public Integer deleteEntry_NfLoadInformation(Integer nfType, String nfInstanceId)
    { return jdbcTemplate.update("DELETE FROM nwdafNfLoadInformation WHERE nfType = ? AND nfInstanceId = ?;", new Object[] {nfType, nfInstanceId}); }

}