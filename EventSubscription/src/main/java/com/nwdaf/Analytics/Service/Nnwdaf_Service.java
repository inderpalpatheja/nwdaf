package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Model.APIBuildInformation;
import com.nwdaf.Analytics.Model.CustomData.AmfEventType;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.CustomData.QosSustainabilityData.PlmnID;
import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.CustomData.ServiceExperience.SvcExperience;
import com.nwdaf.Analytics.Model.CustomData.Tai;
import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.NotificationFormat.NetworkPerformanceNotification;
import com.nwdaf.Analytics.Model.NotificationFormat.ServiceExperienceNotification;
import com.nwdaf.Analytics.Model.NotificationFormat.UserDataCongestionNotification;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionData;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.NnwdafEventsSubscriptionUEmobility;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionModel;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionTable;
import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.*;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator.*;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.*;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator.*;
import com.nwdaf.Analytics.Service.Validator.UpdateValidator.Validator.QosSustainabilityUpdateValidator;
import com.nwdaf.Analytics.Service.Validator.UpdateValidator.Validator.SliceLoadLevelUpdateValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.*;
import java.net.*;
import java.util.*;


import static com.nwdaf.Analytics.Controller.Nnwdaf_Controller.EventCounter;
import static org.springframework.http.HttpHeaders.USER_AGENT;



@Service
public class Nnwdaf_Service extends BusinessLogic {


    @Autowired
    Nnwdaf_Repository repository;

    @Autowired
    BuildProperties buildProperties;


/*
    @AllArgsConstructor
    public class TimedNotification extends TimerTask {

        private String subscriptionID;
        private EventID eventID;

        @Override
        public void run() {

            try
            {
                String notificationURI = repository.getNotificationURI(subscriptionID);
                String snssais = repository.getSnssais_SliceLoadLevel(subscriptionID);

                if(eventID == EventID.LOAD_LEVEL_INFORMATION)
                { send_notificaiton_to_NF(notificationURI, EventID.LOAD_LEVEL_INFORMATION, snssais, repository.getCurrentLoadLevel(snssais), subscriptionID, null); }
            }

            catch(IOException e)
            { e.printStackTrace(); }

            catch(JSONException e)
            { e.printStackTrace(); }
        }
    } */





    private HashMap<String, Timer> timedNotify_subscriptions = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(Nnwdaf_Service.class);
    List<UUID> subIDs;
    String updated_POST_NRF_URL = null;

    /************************************************************************************************************************/


    public Object nwdaf_analytics(AnalyticsRawData rawData)  {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {
            NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
            Object validate;


            // Validate EventID
            if((validate = EventValidator.check(rawData, nnwdafEventsSubscription)) instanceof EventError)
            { return new ResponseEntity<EventError>((EventError)validate, HttpStatus.NOT_ACCEPTABLE); }

            nnwdafEventsSubscription = (NnwdafEventsSubscription)validate;


            int eventID = nnwdafEventsSubscription.getEventID();
            EventCounter[eventID].incrementAnalyticsRequest();


            // Validate other attributes for specific EventID
            if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
            {
                if((validate = SliceLoadLevelAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof AnalyticsError)
                { return new ResponseEntity<>(validate, HttpStatus.NOT_ACCEPTABLE); }
            }

            else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
            {
                if((validate = QosSustainabilityAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof AnalyticsError)
                { return new ResponseEntity<>(validate, HttpStatus.NOT_ACCEPTABLE); }
            }

            else if(eventID == EventID.UE_MOBILITY.ordinal())
            {
                if((validate = UeMobilityAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof UeMobilityAnalyticsError)
                { return new ResponseEntity<>(validate, HttpStatus.NOT_ACCEPTABLE); }
            }

            else if(eventID == EventID.SERVICE_EXPERIENCE.ordinal())
            {
                if((validate = ServiceExperienceAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof ServiceExperienceAnalyticsError)
                { return new ResponseEntity<>(validate, HttpStatus.NOT_ACCEPTABLE); }
            }

            else if(eventID == EventID.NETWORK_PERFORMANCE.ordinal())
            {
                if((validate = NetworkPerformanceAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof NetworkPerfAnalyticsError)
                { return new ResponseEntity<>(validate, HttpStatus.NOT_ACCEPTABLE); }
            }

            else if(eventID == EventID.USER_DATA_CONGESTION.ordinal())
            {
                if((validate = UserDataCongestionAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof UserDataCongestionAnalyticsError)
                { return new ResponseEntity<>(validate, HttpStatus.NOT_ACCEPTABLE); }
            }


            nnwdafEventsSubscription = (NnwdafEventsSubscription)validate;


            if(eventID == EventID.UE_MOBILITY.ordinal())
            { return nwdaf_analyticsUEmobility(nnwdafEventsSubscription); }

            else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
            { return checkForData_QosSustainability(nnwdafEventsSubscription, true); }


            else if(eventID == EventID.SERVICE_EXPERIENCE.ordinal())
            { return checkForData_ServiceExperience(nnwdafEventsSubscription, true); }


            else if(eventID == EventID.NETWORK_PERFORMANCE.ordinal())
            { return checkForData_NetworkPerformance(nnwdafEventsSubscription, true); }


            else if(eventID == EventID.USER_DATA_CONGESTION.ordinal())
            { return checkForData_UserDataCongestion(nnwdafEventsSubscription, true); }


            Object snssaisDataList = checkForData_LoadLevelInformation(nnwdafEventsSubscription, true);


            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);

            EventCounter[eventID].incrementAnalyticsResponse();
            return snssaisDataList;
        }


        catch(IOException e)
        {
            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }

        catch(JSONException e)
        {
            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }


        return null;
    }



    public Object nwdaf_subscription(SubscriptionRawData subscriptionRawData) {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {
            //Test Simulator Connection
            if (!testConnection()) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }

            NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
            Object validator;


            //Check for essential attributes
            if((validator = EssentialsValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof EssentialsError)
            { return new ResponseEntity<EssentialsError>((EssentialsError)validator, HttpStatus.NOT_ACCEPTABLE); }

            nnwdafEventsSubscription = (NnwdafEventsSubscription)validator;


            int eventID = nnwdafEventsSubscription.getEventID();
            EventCounter[eventID].incrementSubscriptionsReceived();


            //Validate Event specific data
            if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
            {
                if((validator = SliceLoadLevelValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof SliceLoadLevelError)
                { return new ResponseEntity<>(validator, HttpStatus.NOT_ACCEPTABLE); }
            }

            else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
            {
                if((validator = QosSustainabilityValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof QosSustainabilityError)
                { return new ResponseEntity<>(validator, HttpStatus.NOT_ACCEPTABLE); }
            }


            else if(eventID == EventID.UE_MOBILITY.ordinal())
            {
                if((validator = UeMobilityValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof UeMobilityError)
                { return new ResponseEntity<>(validator, HttpStatus.NOT_ACCEPTABLE); }
            }


            else if(eventID == EventID.SERVICE_EXPERIENCE.ordinal())
            {
                if((validator = ServiceExperienceValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof ServiceExperienceError)
                { return new ResponseEntity<>(validator, HttpStatus.NOT_ACCEPTABLE); }
            }

            else if(eventID == EventID.NETWORK_PERFORMANCE.ordinal())
            {
                if((validator = NetworkPerformanceValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof NetworkPerformanceError)
                { return new ResponseEntity<>(validator, HttpStatus.NOT_ACCEPTABLE); }
            }


            else if(eventID == EventID.USER_DATA_CONGESTION.ordinal())
            {
                if((validator = UserDataCongestionValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof UserDataCongestionError)
                { return new ResponseEntity<>(validator, HttpStatus.NOT_ACCEPTABLE); }
            }


            nnwdafEventsSubscription = (NnwdafEventsSubscription)validator;


            logger.info("consumer is subscribing for an event and providing these details. eventID:  " + nnwdafEventsSubscription.getEventID() + "\n" + " notificationURI:  " + nnwdafEventsSubscription.getNotificationURI() + "\n" +
                    " snssais:  " + nnwdafEventsSubscription.getSnssais() + "\n" + " notifMethod:  " + nnwdafEventsSubscription.getNotifMethod() + "\n" +
                    " repetitionPeriod:  " + nnwdafEventsSubscription.getRepetitionPeriod() + "\n" + " loadLevelThreshold: " + nnwdafEventsSubscription.getLoadLevelThreshold());

            // Random generating SubscriptionID
            UUID subscriptionID = FrameWorkFunction.getUniqueID();

            // setting subscription ID
            nnwdafEventsSubscription.setSubscriptionID(String.valueOf(subscriptionID));


            if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
            {
                // Adding data into NwdafSubscriptionTable   [ Database ]
                repository.subscribeNF(nnwdafEventsSubscription, EventID.LOAD_LEVEL_INFORMATION);

                // adding values into subscriptionData
                add_values_into_subscriptionData(subscriptionID, nnwdafEventsSubscription.getSnssais(),
                        nnwdafEventsSubscription.getLoadLevelThreshold());

                // Storing data into loadlevelInformation Table
                repository.add_data_into_load_level_table(nnwdafEventsSubscription.getSnssais());

                Object obj = checkForData_LoadLevelInformation(nnwdafEventsSubscription, false);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

           /* if(nnwdafEventsSubscription.getNotifMethod() == NotificationMethod.PERIODIC.ordinal())
            {
                timedNotify_subscriptions.put(nnwdafEventsSubscription.getSubscriptionID(), new Timer(true));
                timedNotify_subscriptions.get(nnwdafEventsSubscription.getSubscriptionID()).scheduleAtFixedRate(new TimedNotification(nnwdafEventsSubscription.getSubscriptionID(), EventID.LOAD_LEVEL_INFORMATION), nnwdafEventsSubscription.getRepetitionPeriod() * 1000, nnwdafEventsSubscription.getRepetitionPeriod() * 1000);
            } */
            }


            else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
            {
                repository.subscribeNF(nnwdafEventsSubscription, EventID.QOS_SUSTAINABILITY);

                repository.addDataQosSustainabilitySubscriptionData(nnwdafEventsSubscription);
                repository.setNwdafQosSustainabilityInformation(nnwdafEventsSubscription.getPlmnID(), nnwdafEventsSubscription.getSnssais());

                Object obj = checkForData_QosSustainability(nnwdafEventsSubscription, false);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }
            }


            else if(eventID == EventID.UE_MOBILITY.ordinal())
            {
                repository.subscribeNF(nnwdafEventsSubscription, EventID.UE_MOBILITY);
                return perform_UEMobility(nnwdafEventsSubscription);
            }


            else if(eventID == EventID.SERVICE_EXPERIENCE.ordinal())
            {
                repository.subscribeNF(nnwdafEventsSubscription, EventID.SERVICE_EXPERIENCE);

                repository.addServiceExperienceSubscriptionData(nnwdafEventsSubscription);
                repository.addServiceExperienceInformation(nnwdafEventsSubscription);

                Object obj = checkForData_ServiceExperience(nnwdafEventsSubscription, false);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }
            }


            else if(eventID == EventID.NETWORK_PERFORMANCE.ordinal())
            {
                nnwdafEventsSubscription.setRepetitionPeriod(0);

                repository.subscribeNF(nnwdafEventsSubscription, EventID.NETWORK_PERFORMANCE);

                if(nnwdafEventsSubscription.getAbsoluteNumThreshold() != null)
                { repository.addNetworkPerformanceSubscriptionData(nnwdafEventsSubscription, NetworkPerfThreshold.ABSOLUTE_NUM); }

                else
                { repository.addNetworkPerformanceSubscriptionData(nnwdafEventsSubscription, NetworkPerfThreshold.RELATIVE_RATIO); }

                repository.addNetworkPerformanceInformation(nnwdafEventsSubscription);

                Object obj = checkForData_NetworkPerformance(nnwdafEventsSubscription, false);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }
            }


            else if(eventID == EventID.USER_DATA_CONGESTION.ordinal())
            {
                repository.subscribeNF(nnwdafEventsSubscription, EventID.USER_DATA_CONGESTION);
                repository.addUserDataCongestionSubscriptionData(nnwdafEventsSubscription);

                fetchSupiLocationFromAMF(nnwdafEventsSubscription, AmfEventType.LOCATION_REPORT);
                repository.addUserDataCongestionInformation(nnwdafEventsSubscription);

                Object obj = checkForData_UserDataCongestion(nnwdafEventsSubscription, false);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }
            }



            logger.info("sending response to NF");
            // function to send response header to NF
            HttpHeaders responseHeaders = send_response_header_to_NF(subscriptionID);


            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);

            EventCounter[eventID].incrementSubscriptionsResponse();
            return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);
        }


        catch (IOException e)
        {
            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }

        catch (JSONException e)
        {
            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }

        catch(URISyntaxException e)
        {
            ErrorCounters.incrementUriSyntaxException();
            e.printStackTrace();
        }

        catch(NullPointerException e)
        {
            ErrorCounters.incrementNullPointerException();
            e.printStackTrace();
        }

        return null;
    }






    public ResponseEntity<?> update_nf_subscription(String subscriptionID, SubUpdateRawData updateData) {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        try
        {
            logger.info("subscriptionID:  " + subscriptionID + "\n" +
                    "notifMethod:  " + updateData.getNotifMethod() + "\n" +
                    "repetitionPeriod:  " + updateData.getRepetitionPeriod() +
                    "loadLevelThreshold:  " + updateData.getLoadLevelThreshold() + "\n" +
                    "ranUeThroughputThreshold:  " + updateData.getRanUeThroughputThreshold() + "\n" +
                    "qosFlowRetainThreshold:  " + updateData.getQosFlowRetainThreshold() + "\n");

            SubscriptionTable subscription = repository.findById_subscriptionID(subscriptionID);

            if (subscription == null) {
                logger.warn("no Content");

                ErrorCounters.incrementDataNotFound();
                return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
            }

            NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
            nnwdafEventsSubscription.setSubscriptionID(subscriptionID);

            Object validator;
            EventCounter[subscription.getEventID()].incrementSubscriptionUpdatesReceived();

            EventID eventID = EventID.values()[subscription.getEventID()];


            if(eventID == EventID.LOAD_LEVEL_INFORMATION)
            {
                if(!((validator = SliceLoadLevelUpdateValidator.check(updateData, nnwdafEventsSubscription)) instanceof NnwdafEventsSubscription))
                { return new ResponseEntity(validator, HttpStatus.NOT_ACCEPTABLE); }

                nnwdafEventsSubscription = (NnwdafEventsSubscription)validator;

                updateForSliceLoadLevelInformation(nnwdafEventsSubscription);
            }

            else if(eventID == EventID.QOS_SUSTAINABILITY)
            {
                if(!((validator = QosSustainabilityUpdateValidator.check(updateData, nnwdafEventsSubscription)) instanceof NnwdafEventsSubscription))
                { return new ResponseEntity(validator, HttpStatus.NOT_ACCEPTABLE); }

                nnwdafEventsSubscription = (NnwdafEventsSubscription)validator;

                updateForQosSustainability(nnwdafEventsSubscription);
            }


            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);

            EventCounter[subscription.getEventID()].incrementSubscriptionUpdatesResponse();
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.OK);
        }

        catch (NullPointerException e)
        {
            ErrorCounters.incrementNullPointerException();
            e.printStackTrace();
        }

        return null;
    }




    public ResponseEntity<?> unsubscription_nf(String subscriptionID)  {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {
            SubscriptionTable subscriber = repository.findById_subscriptionID(subscriptionID);
            //String snssais = repository.getSnssais_LoadLevelSubscriptionData(subscriptionID);
            //String unsubId = repository.getUnSubCorrelationID(snssais);

       /* if(repository.getRefCount(snssais) < 1){
            out.println("Call Unsubscribe From NWDAF");
            unsubscribeFromNWDAF();

        }*/

            if (subscriber == null) {
                logger.warn("subscriptionID= not found");

                ErrorCounters.incrementDataNotFound();
                return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND);
            }

            EventCounter[subscriber.getEventID()].incrementUnSubscriptionsReceived();

            int eventID = subscriber.getEventID();


            if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
            {
                String snssais;

                if((snssais = repository.unsubscribeNF_SliceLoadLevel(subscriber)) != null)
                {
                    EventCounter[eventID].incrementUnSubscriptionsSent();
                    unsubscribeFromNWDAF(snssais);

                    repository.deleteEntry_SliceLoadLevelSubscriptionTable(snssais);

                    EventCounter[eventID].incrementUnSubscriptionsResponse();
                }
            }


            else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
            {
                QosSustainabilitySubscriptionData qosData;

                if((qosData = repository.unsubscribeNF_QosSustainability(subscriber)) != null)
                {
                    QosSustainabilitySubscriptionTable qosRightSideData = repository.getCorrelation_UnSubCorrelation_QosSustainability(qosData.getPlmnID(), qosData.getSnssais());

                    unSubscribeRightSide(DELETE_OAM_URL, qosRightSideData.getCorrelationID(), qosRightSideData.getSubscriptionID(), EventID.QOS_SUSTAINABILITY);
                    repository.deleteEntry_QosSustainabilitySubscriptionTable(qosRightSideData.getCorrelationID());

                    EventCounter[eventID].incrementUnSubscriptionsResponse();
                }
            }


            else if (subscriber.getEventID() == EventID.UE_MOBILITY.ordinal()) {
                unsubscribeNF_forUEMobility(subscriber);
            }


            else if(eventID == EventID.SERVICE_EXPERIENCE.ordinal())
            {
                ServiceExperienceSubscriptionData svcExp;

                if((svcExp = repository.unsubscribeNF_ServiceExperience(subscriptionID)) != null)
                {
                    ServiceExperienceSubscriptionTable svcExpRightSideData = repository.getCorrelation_UnSubCorrelation_ServiceExperience(svcExp.getSupi(), svcExp.getSnssais());

                    unSubscribeRightSide(DELETE_NF_URL, svcExpRightSideData.getCorrelationID(), svcExpRightSideData.getSubscriptionID(), EventID.SERVICE_EXPERIENCE);
                    repository.deleteEntry_ServiceExperienceSubscriptionTable(svcExp.getSupi(), svcExp.getSnssais());

                    EventCounter[eventID].incrementUnSubscriptionsResponse();
                }
            }


            else if(eventID == EventID.NETWORK_PERFORMANCE.ordinal())
            {
                NetworkPerformanceSubscriptionData nwPerfSubData;

                if((nwPerfSubData = repository.unsubscribeNF_NetworkPerformance(subscriptionID)) != null)
                {
                    NetworkPerformanceSubscriptionTable nwPerfSubTable = repository.getCorrelation_UnSubCorrelation_NetworkPerformance(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType());

                    unSubscribeRightSide(DELETE_OAM_URL, nwPerfSubTable.getCorrelationID(), nwPerfSubTable.getSubscriptionID(), EventID.NETWORK_PERFORMANCE);
                    repository.deleteEntry_NetworkPerformanceSubscriptionTable(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType());

                    EventCounter[eventID].incrementUnSubscriptionsResponse();
                }
            }


            else if(eventID == EventID.USER_DATA_CONGESTION.ordinal())
            {
                UserDataCongestionSubscriptionData usrDataCongSubData;

                if((usrDataCongSubData = repository.unsubscribeNF_UserDataCongestion(subscriptionID)) != null)
                {
                    UserDataCongestionSubscriptionTable usrDataCongSubTable = repository.getCorrelation_UnSubCorrelation_UserDataCongestion(usrDataCongSubData.getSupi(), usrDataCongSubData.getCongType());

                    unSubscribeRightSide(DELETE_OAM_URL, usrDataCongSubTable.getCorrelationID(), usrDataCongSubTable.getSubscriptionID(), EventID.USER_DATA_CONGESTION);
                    repository.deleteEntry_UserDataCongestionSubscriptionTable(usrDataCongSubTable.getCorrelationID());

                    EventCounter[eventID].incrementUnSubscriptionsResponse();
                }
            }



            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
        }

        catch(JSONException e)
        {
            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }

        catch(IOException e)
        {
            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }

        catch(NullPointerException e)
        {
            ErrorCounters.incrementNullPointerException();
            e.printStackTrace();
        }

        return null;
    }





    public ResponseEntity<?> get_all_network_function(String id) {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        SubscriptionTable user = repository.findById_subscriptionID(id);

        if (user == null) {
            logger.warn("subscriptionID= not found");
            return new ResponseEntity<SubscriptionTable>(HttpStatus.NOT_FOUND);
        }
        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return new ResponseEntity<SubscriptionTable>(user, HttpStatus.OK);
    }





    public void notificationHandler_LoadLevelInformation(String response)  {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {
            // here subID == correlationID

            JSONObject json = new JSONObject(response);

            int currentLoadLevel = json.getInt("currentLoadLevel");
            String correlationID = json.getString("correlationID");


            String snssais = repository.getSnssais_SliceLoadLevelSubscriptionTable(correlationID);


            if (repository.getRefCount_SliceLoadLevel(snssais) == 0) {
                repository.deleteEntry_SliceLoadLevelSubscriptionTable(snssais);
            }


            repository.updateCurrentLoadLevel(currentLoadLevel, snssais);
            EventCounter[EventID.LOAD_LEVEL_INFORMATION.ordinal()].incrementSubscriptionNotificationsReceived();

            sliceLoadLevelNotificationManager();


            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        }

        catch(NullPointerException e)
        {
            ErrorCounters.incrementNullPointerException();
            e.printStackTrace();
        }

        catch (JSONException e)
        {
            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }

        catch(IOException e)
        {
            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }

    }





    public Object nwdaf_counters(Integer eventID) {

        return FrameWorkFunction.getStats(eventID);
    }


    public Object nwdaf_error_counters() {

        return FrameWorkFunction.getErrorStats();
    }


    public String nwdaf_reset_counter() {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        FrameWorkFunction.restCounters();

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return "Counters set to 0.";
    }


    /************************************************************************************************************************/



    // API info details via Swagger
    // Adding Swagger Configurations
    public Object check_api_details() throws IOException {

        APIBuildInformation apiBuildInformation = new APIBuildInformation();


        apiBuildInformation.setAPI_NAME(buildProperties.getArtifact());
        apiBuildInformation.setAPI_BUILD_TIME(buildProperties.getTime());
        apiBuildInformation.setAPI_VERSION(buildProperties.getVersion());


        return apiBuildInformation;

    }


    @Bean
    public Docket swaggerConfiguration() throws IOException {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nwdaf"))
                .build()
                .apiInfo(apiDetails());

    }

    private ApiInfo apiDetails() throws IOException {


        return new ApiInfo(
                "NWDAF API ( Network Data Analytics Function )",
                " Sample API for NWDAF in 5G",
                buildProperties.getVersion(),
                "Free to use",
                new springfox.documentation.service.Contact("Team NWDAF", "https://truminds.com/home", "sheetal.kumar@truminds.co.in"),
                "API License",
                "https://truminds.com/home",
                Collections.emptyList());
    }






    /*************************************Sheetal's UE_MOBILITY functions***************************************************************/





    public ResponseEntity<?> perform_UEMobility(NnwdafEventsSubscription nnwdafEventsSubscription) throws IOException, JSONException, URISyntaxException {

        //System.out.println("in UE-Mobility");

        System.out.println(nnwdafEventsSubscription.getSupi());

        // updating UE-Mobility Table if event id UE_mobility
        UEMobilitySubscriptionModel ue_mobilitySubscriptionModel = new UEMobilitySubscriptionModel();

        ue_mobilitySubscriptionModel.setSubscriptionID(nnwdafEventsSubscription.getSubscriptionID());
        ue_mobilitySubscriptionModel.setEventID(nnwdafEventsSubscription.getEventID());
        ue_mobilitySubscriptionModel.setNotificationURI(nnwdafEventsSubscription.getNotificationURI());
        ue_mobilitySubscriptionModel.setNotifMethod(nnwdafEventsSubscription.getNotifMethod());
        ue_mobilitySubscriptionModel.setRepetitionPeriod(nnwdafEventsSubscription.getRepetitionPeriod());
        ue_mobilitySubscriptionModel.setSupi(nnwdafEventsSubscription.getSupi());


        //  System.out.println(ue_mobilitySubscriptionModel.toString());

        repository.add_data_into_nwdaf_UeMobilitySubscriptionData(ue_mobilitySubscriptionModel);
        repository.add_data_into_nwdafUEmobility(ue_mobilitySubscriptionModel);


        Object obj = check_For_data_for_UE_Mobility(nnwdafEventsSubscription, false);

        if (obj instanceof ResponseEntity) {
            return (ResponseEntity<?>) obj;
        }

        logger.info("sending response to NF");

        // function to send response header to NF
        HttpHeaders responseHeaders = send_response_header_to_NF(UUID.fromString(nnwdafEventsSubscription.getSubscriptionID()));


        EventCounter[EventID.UE_MOBILITY.ordinal()].incrementSubscriptionsResponse();
        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);

    }


    public void notificationHandlerForUEMobility(String response)  {


        try
        {
            EventCounter[EventID.UE_MOBILITY.ordinal()].incrementSubscriptionNotificationsReceived();

            // System.out.println("In-Notification-Handler-for-UE-Mobility");
            //JSONArray jsonArray = new JSONArray(response);
            JSONArray jsonArray = new JSONArray(response);
            String correlationID = jsonArray.get(0).toString();
            String cellID;
            String TacValue;
            Integer MCC_value;
            Integer MNC_value;
            JSONObject plmnObject = new JSONObject();

            List<String> tai_cellIDs = new ArrayList<String>();

            // At 0 position I am sending CorrelationID;
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // String correlationID = jsonObject.getString("correlationID");
                Integer timeDurationValue = jsonObject.getInt("timeDuration");

                // System.out.println("TimeDurationValue " + timeDurationValue);

                JSONObject taiValue = jsonObject.getJSONObject("Tai");
                JSONObject ecgiValue = jsonObject.getJSONObject("Ecgi");

                cellID = ecgiValue.getString("cellID");
                TacValue = taiValue.getString("Tac");
                plmnObject = taiValue.getJSONObject("plmn");
                MCC_value = plmnObject.getInt("MCC");
                MNC_value = plmnObject.getInt("MNC");
                String TaiValue = MCC_value + "," + MNC_value + ":" + TacValue;

                // update all the location of supi received in JsonArray;
                add_value_to_userLocationTable(TaiValue, cellID, timeDurationValue);

                tai_cellIDs.add(TaiValue + " " + cellID);
            }

            String supi = repository.getSupiValueByCorrelationID(correlationID);


            // now time to update UE-MobilityTable;
            // First Fetch all ID
            updateLocationIdTable(supi, tai_cellIDs);

            if (repository.getRefCount_UEmobilitySubscriptionTable(supi) == 0) {
                repository.deleteEntry_UEMobilitySubscriptionTable(supi);
            }

            // repository.getRefCount_UEmobilitySubscriptionTable()
            nwdaf_notification_manager_ForUEMobility(supi);
        }

        catch(JSONException e)
        {
            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }

        catch(IOException e)
        {
            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }

    }


    /****UEmobility******/

    public Object nwdaf_analyticsUEmobility(NnwdafEventsSubscription nnwdafEventsSubscription) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if (nnwdafEventsSubscription.getEventID() == EventID.UE_MOBILITY.ordinal()) {

            NnwdafEventsSubscriptionUEmobility nnwdafEventsSubscriptionUE = new NnwdafEventsSubscriptionUEmobility();
            nnwdafEventsSubscriptionUE.setSupi(nnwdafEventsSubscription.getSupi());

            Object supiDataList = check_For_dataUEmobility(nnwdafEventsSubscriptionUE, true);


            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return supiDataList;

        } else {

            return "eventID: eventID for UEmobility must be 5";
        }
    }



    private void unsubscribeNF_forUEMobility(SubscriptionTable subscriber) throws IOException, JSONException {

        String supi;


        if ((supi = repository.unsubscribeNFForUE(subscriber)) != null) {

            Integer eventID = subscriber.getEventID();

            unsubscribeFromNWDAF_forUEMobility(supi, eventID);

            repository.deleteEntry_UEMobilitySubscriptionTable(supi);

            EventCounter[eventID].incrementUnSubscriptionsSent();
        }
    }



    protected void unsubscribeFromNWDAF_forUEMobility(String supi, Integer eventID) throws IOException, JSONException {

        // here subscriptionID = unsubCorrealtionID

        // String subscriptionID =  repository.getUnSubCorrelationID(snssais);

        URL obj = new URL(DELETE_AMF_URL);
        //  out.println(DELETE_NRF_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();


        String unSubCorrelationID = repository.getUnSubCorrelationID_UEMobility(supi);
        String correlationID = repository.getCorrelationID_UEMobility(unSubCorrelationID);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventID", EventID.UE_MOBILITY.ordinal());
        jsonObject.put("unSubCorrelationID", unSubCorrelationID);
        jsonObject.put("correlationID", correlationID);


        con.setRequestMethod("DELETE");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonObject.toString().getBytes("utf-8");

            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Read the response from input stream;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

    }




    /*************************************SERVICE_EXPERIENCE***********************************************************/



    public void notificationHandler_ServiceExperience(String response)
    {
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {
            JSONObject notificationData = new JSONObject(response);

            Float mos = Float.valueOf(notificationData.getString("mos"));
            Float upperRange = Float.valueOf(notificationData.getString("upperRange"));
            Float lowerRange = Float.valueOf(notificationData.getString("lowerRange"));

            String correlationID = notificationData.getString("correlationID");
            ServiceExperienceSubscriptionTable svcExp_supi_snssais = repository.getSupi_snssais_ServiceExperienceSubscriptionTable(correlationID);

            SvcExperience svcExperience = new SvcExperience(mos, upperRange, lowerRange);

            repository.updateServiceExperienceInformation(svcExperience, svcExp_supi_snssais.getSupi(), svcExp_supi_snssais.getSnssais());

            String subscriptionID = repository.getSubscriptionID_ServiceExperienceSubscriptionData(svcExp_supi_snssais.getSupi(), svcExp_supi_snssais.getSnssais());
            String notificationURI = repository.getNotificationURI(subscriptionID);

            ServiceExperienceNotification svcExpNotifyData = new ServiceExperienceNotification();

            svcExpNotifyData.setSubscriptionID(subscriptionID);
            svcExpNotifyData.setNotificationURI(notificationURI);
            svcExpNotifyData.setSvcExpInfo(svcExperience);
            svcExpNotifyData.setSupi(svcExp_supi_snssais.getSupi());
            svcExpNotifyData.setSnssais(svcExp_supi_snssais.getSnssais());

            sendServiceExperienceNotification(svcExpNotifyData);
        }

        catch(JSONException e)
        {
            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }

        catch(IOException e)
        {
            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }
    }




    /*************************************QOS_SUSTAINABILITY***********************************************************/


    public void notificationHandler_QosSustainability(String response)
    {
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);
        EventCounter[EventID.QOS_SUSTAINABILITY.ordinal()].incrementSubscriptionNotificationsReceived();


        try
        {
            JSONObject json = new JSONObject(response);

            Integer ranUeThroughput = null;
            Integer qosFlowRetain = null;

            if(json.has("ranUeThroughput"))
            { ranUeThroughput = json.getInt("ranUeThroughput"); }

            else
            { qosFlowRetain = json.getInt("qosFlowRetain"); }

            String correlationID = json.getString("correlationID");
            QosSustainabilitySubscriptionData qosData = repository.getPlmnID_Snssais_ByCorrelationID(correlationID);


            // DELETE Analytics entry in SubscriptionTable (refCount = 0)
            repository.deleteAnalyticsEntry_QosSustainability(correlationID);


            if(ranUeThroughput != null)
            {
                repository.updateRanUeThroughput(ranUeThroughput, qosData.getPlmnID(), qosData.getSnssais());

                qosNotificationManager(QosType.RAN_UE_THROUGHPUT);
            }

            else if(qosFlowRetain != null)
            {
                repository.updateQosFlowRetain(qosFlowRetain, qosData.getPlmnID(), qosData.getSnssais());

                qosNotificationManager(QosType.QOS_FLOW_RETAIN);
            }


            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        }

        catch(NullPointerException e)
        {
            ErrorCounters.incrementNullPointerException();
            e.printStackTrace();
        }

        catch (JSONException e)
        {
            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }
    }




    /*************************************NETWORK_PERFORMANCE***********************************************************/


    public void notificationHandler_NetworkPerformance(String response)
    {
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);
        EventCounter[EventID.NETWORK_PERFORMANCE.ordinal()].incrementSubscriptionNotificationsReceived();

        try
        {
            JSONObject notificationData = new JSONObject(response);

           /* Integer relativeRatio = null;
            Integer absoluteNum = null;

            if(notificationData.has("relativeRatio"))
            { relativeRatio = notificationData.getInt("relativeRatio"); }

            else if(notificationData.has("absoluteNum"))
            { absoluteNum = notificationData.getInt("absoluteNum"); } */

            Integer reportingThreshold = notificationData.getInt("reportingThreshold");

            String correlationID = notificationData.getString("correlationID");
            NetworkPerformanceSubscriptionData nwPerfSubData = repository.getSupi_nwPerfType_byCorrelationID(correlationID);

            NetworkPerfType nwPerfType = NetworkPerfType.values()[nwPerfSubData.getNwPerfType()];
            NetworkPerfThreshold networkPerfThreshold = NetworkPerfThreshold.ABSOLUTE_NUM;


            if(nwPerfType == NetworkPerfType.GNB_ACTIVE_RATIO || nwPerfType == NetworkPerfType.SESS_SUCC_RATIO || nwPerfType == NetworkPerfType.HO_SUCC_RATIO)
            { networkPerfThreshold = NetworkPerfThreshold.RELATIVE_RATIO; }


            // DELETE Analytics entry in SubscriptionTable (refCount = 0)
            repository.deleteAnalyticsEntry_NetworkPerformance(correlationID);


            if(networkPerfThreshold == NetworkPerfThreshold.RELATIVE_RATIO)
            {
                repository.updateRelativeRatio_NetworkPerf(reportingThreshold, nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType());
                NetworkPerformanceSubscriptionData crossedThresholdData = repository.getCrossedNwPerfType_subscriptionID_NetworkPerf(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType(), reportingThreshold, NetworkPerfThreshold.RELATIVE_RATIO);

                if(crossedThresholdData != null)
                {
                    NetworkPerformanceNotification nwPerfNotifyData = new NetworkPerformanceNotification();

                    nwPerfNotifyData.setSubscriptionID(crossedThresholdData.getSubscriptionID());
                    nwPerfNotifyData.setNotificationURI(repository.getNotificationURI(crossedThresholdData.getSubscriptionID()));
                    nwPerfNotifyData.setNwPerfType(crossedThresholdData.getNwPerfType());
                    nwPerfNotifyData.setRelativeRatio(reportingThreshold);
                    //nwPerfNotifyData.setMcc(nwPerfSubData.getSupi().substring(0, 3));
                    //nwPerfNotifyData.setMnc(nwPerfSubData.getSupi().substring(3, 5));
                    //nwPerfNotifyData.setTac(RandomStringUtils.randomAlphanumeric(6));

                    sendNetworkExperienceNotification(nwPerfNotifyData, NetworkPerfThreshold.RELATIVE_RATIO);
                }
            }

            else
            {
                repository.updateAbsoluteNum_NetworkPerf(reportingThreshold, nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType());
                NetworkPerformanceSubscriptionData crossedThresholdData = repository.getCrossedNwPerfType_subscriptionID_NetworkPerf(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType(), reportingThreshold, NetworkPerfThreshold.ABSOLUTE_NUM);

                if(crossedThresholdData != null)
                {
                    NetworkPerformanceNotification nwPerfNotifyData = new NetworkPerformanceNotification();

                    nwPerfNotifyData.setSubscriptionID(crossedThresholdData.getSubscriptionID());
                    nwPerfNotifyData.setNotificationURI(repository.getNotificationURI(crossedThresholdData.getSubscriptionID()));
                    nwPerfNotifyData.setNwPerfType(crossedThresholdData.getNwPerfType());
                    nwPerfNotifyData.setAbsoluteNum(reportingThreshold);
                    //nwPerfNotifyData.setMcc(nwPerfSubData.getSupi().substring(0, 3));
                    //nwPerfNotifyData.setMnc(nwPerfSubData.getSupi().substring(3, 5));
                    //nwPerfNotifyData.setTac(RandomStringUtils.randomAlphanumeric(6));

                    sendNetworkExperienceNotification(nwPerfNotifyData, NetworkPerfThreshold.ABSOLUTE_NUM);
                }
            }

            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);

        }

        catch (JSONException e) {

            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }

        catch (IOException e) {

            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }

    }





    /*************************************USER_DATA_CONGESTION***********************************************************/




    public void notificationHandler_UserDataCongestion(JSONObject notifyData)
    {
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);
        EventCounter[EventID.USER_DATA_CONGESTION.ordinal()].incrementSubscriptionNotificationsReceived();


        try
        {
            Integer congLevel = notifyData.getInt("congLevel");
            String correlationID = notifyData.getString("correlationID");

            UserDataCongestionSubscriptionTable usrDataCongDetails = repository.getUserDataCongestionDetails_byCorrelationID(correlationID);
            repository.updateCongestionLevel_UserDataCongestion(congLevel, usrDataCongDetails.getSupi(), usrDataCongDetails.getCongType(), usrDataCongDetails.getTai());

            // DELETE Analytics entry in SubscriptionTable (refCount = 0)
            repository.deleteAnalyticsEntry_UserDataCongestion(correlationID);

            if(repository.congestionLevelThresholdCrossed(usrDataCongDetails.getSupi(), usrDataCongDetails.getCongType(), congLevel))
            {
                UserDataCongestionNotification usrDataCongNotifyData = new UserDataCongestionNotification();

                usrDataCongNotifyData.setSubscriptionID(repository.getSubscriptionID_UserDataCongestion(usrDataCongDetails.getSupi(), usrDataCongDetails.getCongType()));
                usrDataCongNotifyData.setNotificationURI(repository.getNotificationURI(usrDataCongNotifyData.getSubscriptionID()));

                String areaInfo[] = usrDataCongDetails.getTai().split("-");

                usrDataCongNotifyData.setTai(new Tai(new PlmnID(areaInfo[0], areaInfo[1]), areaInfo[2]));
                usrDataCongNotifyData.setCongType(usrDataCongDetails.getCongType());
                usrDataCongNotifyData.setCongLevel(congLevel);

                sendUserDataCongestionNotification(usrDataCongNotifyData);
            }
        }

        catch (JSONException e) {

            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }


        catch (IOException e) {

            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }

    }




    /********************************************************************************************************************/




    public void notificationHandlerOAM(String response) throws JSONException {

        JSONObject json = new JSONObject(response);

        EventID eventID = EventID.values()[json.getInt("eventID")];

        switch(eventID)
        {
            case QOS_SUSTAINABILITY: notificationHandler_QosSustainability(response);
                                     break;

            case NETWORK_PERFORMANCE: notificationHandler_NetworkPerformance(response);
                                      break;

            case USER_DATA_CONGESTION: notificationHandler_UserDataCongestion(json);
                                       break;
        }
    }



}




/*

            JSONObject notificationData = new JSONObject(response);

            String svcExpData = notificationData.getString("svcExpData");
            svcExpData = svcExpData.substring(1, svcExpData.length() - 1);

            String values[] = svcExpData.split(", ");

            List<Float> svcExpValues = new ArrayList<>();
            Float sum = 0f;

            for(String svcVal: values)
            {
                Float value = Float.valueOf(svcVal);
                svcExpValues.add(value);
                sum += value;
            }

            Float upperRange = Collections.max(svcExpValues);
            Float lowerRange = Collections.min(svcExpValues);
            Float mos = sum / svcExpValues.size();

            String correlationID = notificationData.getString("correlationID");
            ServiceExperienceSubscriptionTable svcExp_supi_snssais = repository.getSupi_snssais_ServiceExperienceSubscriptionTable(correlationID);

            SvcExperience svcExperience = new SvcExperience(mos, upperRange, lowerRange);

            repository.updateServiceExperienceInformation(svcExperience, svcExp_supi_snssais.getSupi(), svcExp_supi_snssais.getSnssais());

            String subscriptionID = repository.getSubscriptionID_ServiceExperienceSubscriptionData(svcExp_supi_snssais.getSupi(), svcExp_supi_snssais.getSnssais());
            String notificationURI = repository.getNotificationURI(subscriptionID);


            ServiceExperienceNotification svcExpNotifyData = new ServiceExperienceNotification();

            svcExpNotifyData.setSubscriptionID(subscriptionID);
            svcExpNotifyData.setNotificationURI(notificationURI);
            svcExpNotifyData.setSvcExpInfo(svcExperience);
            svcExpNotifyData.setSupi(svcExp_supi_snssais.getSupi());
            svcExpNotifyData.setSnssais(svcExp_supi_snssais.getSnssais());


            sendServiceExperienceNotification(svcExpNotifyData);
 */