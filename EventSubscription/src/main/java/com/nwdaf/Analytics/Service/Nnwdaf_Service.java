package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Model.*;
import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.ExceptionId;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfValue;
import com.nwdaf.Analytics.Model.CustomData.NfLoad.NfThresholdType;
import com.nwdaf.Analytics.Model.CustomData.QosSustainability.QosThresholdType;
import com.nwdaf.Analytics.Model.CustomData.TargetUeInformation;
import com.nwdaf.Analytics.Model.NetworkArea.PlmnId;
import com.nwdaf.Analytics.Model.CustomData.ServiceExperience.SvcExperience;
import com.nwdaf.Analytics.Model.NetworkArea.Tai;
import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NotificationFormat.*;
import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourSubscriptionTable;
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
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.NnwdafEventsSubscriptionUEmobility;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionModel;
import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommInformation;
import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionTable;
import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
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
import org.springframework.http.MediaType;
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


    public Object nwdaf_analytics(NwdafEvent event, TargetUeInformation tgtUe, EventFilter eventFilter)  {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {

            switch(event)
            {
                case LOAD_LEVEL_INFORMATION: return checkForData_LoadLevelInformation(eventFilter.getSnssais().get(0).toString(), true);

                case UE_MOBILITY: return nwdaf_analyticsUEmobility(tgtUe.getSupi());

                case QOS_SUSTAINABILITY: return analyticsRequest_QosSustainability(eventFilter.getQosRequ().get_5Qi(), eventFilter.getNetworkArea().getTais().get(0).toString(), eventFilter.getSnssais().get(0).toString());

                case SERVICE_EXPERIENCE: return checkForData_ServiceExperience(tgtUe.getSupi(), eventFilter.getSnssais().get(0).toString(), tgtUe.getAnyUe(), true);

                case NETWORK_PERFORMANCE: return checkForData_NetworkPerformance(tgtUe.getSupi(), eventFilter.getNwPerfTypes().get(0).ordinal(), tgtUe.getAnyUe(), true);

                case ABNORMAL_BEHAVIOUR: return checkForData_AbnormalBehaviour(tgtUe.getSupi(), eventFilter.getExcepIds().get(0).ordinal(), true);

                case UE_COMM: return analyticsRequest_UeComm(tgtUe.getSupi(), eventFilter.getMaxAnaEntry());

                case NF_LOAD: return analyticsRequest_NfLoad(eventFilter.getNfTypes().get(0).ordinal(), eventFilter.getNfInstanceIds().get(0));
            }


            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            EventCounter[event.ordinal()].incrementAnalyticsResponse();

            return null;
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



    public Object nwdaf_subscription(NnwdafEventsSubscription nnwdafEventsSubscription) {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {
            //Test Simulator Connection
            if (!testConnection()) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }


            // Random generating SubscriptionID
            String subscriptionId = FrameWorkFunction.getUniqueID().toString();
            String notificationURI = nnwdafEventsSubscription.getNotificationURI();


            // New Subscriber Entry information
            repository.subscriberRegister(new SubscriptionID(subscriptionId, notificationURI));


            // Subscribe for all EventIDs provided by the subscriber as per RequestBody payload
            for(EventSubscription eventSubscription: nnwdafEventsSubscription.getEventSubscriptions())
            {
                NwdafEvent event = eventSubscription.getEvent();
                EventCounter[event.ordinal()].incrementSubscriptionsReceived();


                if(event == NwdafEvent.UE_MOBILITY)
                {
                    SubscriptionTable subscriptionTable = new SubscriptionTable(eventSubscription, subscriptionId);
                    repository.subscribeNF(subscriptionTable);

                    return perform_UEMobility(eventSubscription, subscriptionId, notificationURI);
                }

                else
                {
                    Object obj = subscribeForEvent(eventSubscription, subscriptionId);

                    if(obj instanceof ResponseEntity)
                    { return obj; }
                }

                EventCounter[event.ordinal()].incrementSubscriptionsResponse();
            }


            logger.info("sending response to NF");
            // function to send response header to NF
            HttpHeaders responseHeaders = send_response_header_to_NF(subscriptionId);


            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
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





/*
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
*/



    public ResponseEntity<?> unsubscription_nf(String subscriptionId)  {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {
            // If subscriptionId doesn't exist
            if (!repository.subscriptionExists(subscriptionId)) {

                logger.warn("subscriptionID= not found");

                ErrorCounters.incrementDataNotFound();

                ProblemDetails problemDetails = new ProblemDetails();

                problemDetails.setTitle("Nnwdaf_EventsSubscription_Unsubscribe service operation");
                problemDetails.setStatus(HttpStatus.NOT_FOUND.value());
                problemDetails.setCause("SUBSCRIPTION_NOT_FOUND");

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

                return new ResponseEntity<>(problemDetails, headers, HttpStatus.NOT_FOUND);
            }


            // Get all the eventIDs subscribed by the NF consumer
            List<Integer> subscribedEvents = repository.getAllSubscribedEvents(subscriptionId);


            // Un-Subscribe for subscribed eventIDs
            for(Integer eventId: subscribedEvents)
            {
                EventCounter[eventId].incrementUnSubscriptionsReceived();

                NwdafEvent event = NwdafEvent.values()[eventId];

                if(event == NwdafEvent.LOAD_LEVEL_INFORMATION)
                {
                    String snssais;

                    if((snssais = repository.unsubscribeNF_SliceLoadLevel(subscriptionId)) != null)
                    {
                        EventCounter[event.ordinal()].incrementUnSubscriptionsSent();
                        unsubscribeFromNWDAF(snssais);

                        repository.deleteEntry_SliceLoadLevelSubscriptionTable(snssais);
                        repository.deleteEntry_SliceLoadLevelInformation(snssais);

                        EventCounter[event.ordinal()].incrementUnSubscriptionsResponse();
                    }
                }


                else if(event == NwdafEvent.QOS_SUSTAINABILITY)
                {
                    QosSustainabilitySubscriptionData qosData;

                    if((qosData = repository.unsubscribeNF_QosSustainability(subscriptionId)) != null)
                    {
                        QosSustainabilitySubscriptionTable qosRightSideData = repository.getCorrelation_UnSubCorrelation_QosSustainability(qosData.getTai(), qosData.getSnssai());

                        unSubscribeRightSide(DELETE_OAM_URL, qosRightSideData.getCorrelationId(), qosRightSideData.getSubscriptionId(), NwdafEvent.QOS_SUSTAINABILITY);

                        repository.deleteEntry_QosSustainabilitySubscriptionTable(qosRightSideData.getCorrelationId());
                        repository.deleteEntry_QosSustainabilityInformation(qosData.getTai(), qosData.getSnssai());

                        EventCounter[event.ordinal()].incrementUnSubscriptionsResponse();
                    }
                }


                else if (event == NwdafEvent.UE_MOBILITY) {
                    unsubscribeNF_forUEMobility(subscriptionId);
                }


                else if(event == NwdafEvent.SERVICE_EXPERIENCE)
                {
                    ServiceExperienceSubscriptionData svcExp;

                    if((svcExp = repository.unsubscribeNF_ServiceExperience(subscriptionId)) != null)
                    {
                        ServiceExperienceSubscriptionTable svcExpRightSideData = repository.getCorrelation_UnSubCorrelation_ServiceExperience(svcExp.getSupi(), svcExp.getSnssai());

                        unSubscribeRightSide(DELETE_NF_URL, svcExpRightSideData.getCorrelationId(), svcExpRightSideData.getSubscriptionId(), NwdafEvent.SERVICE_EXPERIENCE);

                        repository.deleteEntry_ServiceExperienceSubscriptionTable(svcExp.getSupi(), svcExp.getSnssai());
                        repository.deleteEntry_ServiceExperienceInformation(svcExp.getSupi(), svcExp.getSnssai());

                        EventCounter[event.ordinal()].incrementUnSubscriptionsResponse();
                    }
                }


                else if(event == NwdafEvent.NETWORK_PERFORMANCE)
                {
                    NetworkPerformanceSubscriptionData nwPerfSubData;

                    if((nwPerfSubData = repository.unsubscribeNF_NetworkPerformance(subscriptionId)) != null)
                    {
                        NetworkPerformanceSubscriptionTable nwPerfSubTable = repository.getCorrelation_UnSubCorrelation_NetworkPerformance(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType());

                        unSubscribeRightSide(DELETE_OAM_URL, nwPerfSubTable.getCorrelationId(), nwPerfSubTable.getSubscriptionId(), NwdafEvent.NETWORK_PERFORMANCE);

                        repository.deleteEntry_NetworkPerformanceSubscriptionTable(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType());
                        repository.deleteEntry_NetworkPerformanceInformation(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType());

                        EventCounter[event.ordinal()].incrementUnSubscriptionsResponse();
                    }
                }


                else if(event == NwdafEvent.USER_DATA_CONGESTION)
                {
                    UserDataCongestionSubscriptionData usrDataCongSubData;

                    if((usrDataCongSubData = repository.unsubscribeNF_UserDataCongestion(subscriptionId)) != null)
                    {
                        UserDataCongestionSubscriptionTable usrDataCongSubTable = repository.getCorrelation_UnSubCorrelation_UserDataCongestion(usrDataCongSubData.getSupi(), usrDataCongSubData.getCongType());

                        unSubscribeRightSide(DELETE_OAM_URL, usrDataCongSubTable.getCorrelationId(), usrDataCongSubTable.getSubscriptionId(), NwdafEvent.USER_DATA_CONGESTION);

                        repository.deleteEntry_UserDataCongestionSubscriptionTable(usrDataCongSubTable.getCorrelationId());
                        repository.deleteEntry_UserDataCongestionInformation(usrDataCongSubData.getSupi(), usrDataCongSubData.getCongType());

                        EventCounter[event.ordinal()].incrementUnSubscriptionsResponse();
                    }
                }


                else if(event == NwdafEvent.ABNORMAL_BEHAVIOUR)
                {
                    String supi = repository.getSupi_AbnormalBehaviour(subscriptionId);
                    List<Integer> excepIds = repository.getExcepIds_AbnormalBehaviour(subscriptionId);

                    repository.unsubscribeNF_AbnormalBehaviourSubscriptionEntry(subscriptionId);

                    for(Integer excepId: excepIds)
                    {
                        AbnormalBehaviourSubscriptionData abnorBehavrSubData;

                        if((abnorBehavrSubData = repository.unsubscribeNF_AbnormalBehaviour(supi, excepId)) != null)
                        {
                            AbnormalBehaviourSubscriptionTable abnorBehavrSubTable = repository.getCorrelation_UnSubCorrelation_AbnormalBehaviour(abnorBehavrSubData.getSupi(), abnorBehavrSubData.getExcepId());

                            unSubscribeRightSide(DELETE_SMF_URL, abnorBehavrSubTable.getCorrelationId(), abnorBehavrSubTable.getSubscriptionId(), NwdafEvent.ABNORMAL_BEHAVIOUR);

                            repository.deleteEntry_AbnormalBehaviourSubscriptionTable(abnorBehavrSubTable.getCorrelationId());
                            repository.deleteEntry_AbnormalBehaviourInformation(abnorBehavrSubData.getSupi(), abnorBehavrSubData.getExcepId());

                            EventCounter[event.ordinal()].incrementUnSubscriptionsResponse();
                        }
                    }
                }


                else if(event == NwdafEvent.UE_COMM)
                {
                    String supi;

                    if((supi = repository.unsubscribeNF_UeComm(subscriptionId)) != null)
                    {
                        UeCommSubscriptionTable ueCommSubscriptionTable = repository.getCorrelation_UnSubCorrelation_UeComm(supi);

                        unSubscribeRightSide(DELETE_SMF_URL, ueCommSubscriptionTable.getCorrelationId(), ueCommSubscriptionTable.getSubscriptionId(), NwdafEvent.UE_COMM);

                        repository.deleteEntry_UeCommSubscriptionTable(ueCommSubscriptionTable.getCorrelationId());
                        repository.deleteEntry_UeCommInformation(supi);

                        EventCounter[event.ordinal()].incrementUnSubscriptionsResponse();
                    }
                }


                else if(event == NwdafEvent.NF_LOAD)
                {
                    NfLoadSubscriptionData nfLoadSubscriptionData;

                    if((nfLoadSubscriptionData = repository.unsubscribeNF_NfLoad(subscriptionId)) != null)
                    {
                        NfLoadSubscriptionTable nfLoadSubscriptionTable = repository.getCorrelation_UnSubCorrelation_NfLoad(nfLoadSubscriptionData.getNfType(), nfLoadSubscriptionData.getNfInstanceId());

                        unSubscribeRightSide(DELETE_OAM_URL, nfLoadSubscriptionTable.getCorrelationId(), nfLoadSubscriptionTable.getSubscriptionId(), NwdafEvent.NF_LOAD);

                        repository.deleteEntry_NfLoadSubscriptionTable(nfLoadSubscriptionTable.getCorrelationId());
                        repository.deleteEntry_NfLoadInformation(nfLoadSubscriptionData.getNfType(), nfLoadSubscriptionData.getNfInstanceId());

                        EventCounter[event.ordinal()].incrementUnSubscriptionsResponse();
                    }
                }

            }

            // Finally Delete the subscriber entry in the nwdafSubscriptionID table
            repository.deleteSubscriberEntry(subscriptionId);

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
            String correlationID = json.getString("correlationId");


            String snssais = repository.getSnssais_SliceLoadLevelSubscriptionTable(correlationID);


            if (repository.getRefCount_SliceLoadLevel(snssais) == 0) {
                repository.deleteEntry_SliceLoadLevelSubscriptionTable(snssais);
            }


            repository.updateCurrentLoadLevel(currentLoadLevel, snssais);
            EventCounter[NwdafEvent.LOAD_LEVEL_INFORMATION.ordinal()].incrementSubscriptionNotificationsReceived();

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





    public ResponseEntity<?> perform_UEMobility(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws IOException, JSONException, URISyntaxException {

        //System.out.println("in UE-Mobility");

        System.out.println(eventSubscription.getTgtUe().getSupi());

        // updating UE-Mobility Table if event id UE_mobility
        UEMobilitySubscriptionModel ue_mobilitySubscriptionModel = new UEMobilitySubscriptionModel();

        ue_mobilitySubscriptionModel.setSubscriptionId(subscriptionId);
        ue_mobilitySubscriptionModel.setEvent(eventSubscription.getEvent().ordinal());
        ue_mobilitySubscriptionModel.setNotificationURI(notificationURI);
        ue_mobilitySubscriptionModel.setNotifMethod(eventSubscription.getNotificationMethod().ordinal());
        ue_mobilitySubscriptionModel.setRepetitionPeriod(eventSubscription.getRepetitionPeriod());
        ue_mobilitySubscriptionModel.setSupi(eventSubscription.getTgtUe().getSupi());


        //  System.out.println(ue_mobilitySubscriptionModel.toString());

        repository.add_data_into_nwdaf_UeMobilitySubscriptionData(ue_mobilitySubscriptionModel);
        repository.add_data_into_nwdafUEmobility(ue_mobilitySubscriptionModel);


        Object obj = check_For_data_for_UE_Mobility(eventSubscription, false);

        if (obj instanceof ResponseEntity) {
            return (ResponseEntity<?>) obj;
        }

        logger.info("sending response to NF");

        // function to send response header to NF
        HttpHeaders responseHeaders = send_response_header_to_NF(subscriptionId);


        EventCounter[NwdafEvent.UE_MOBILITY.ordinal()].incrementSubscriptionsResponse();
        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);

    }


    public void notificationHandlerForUEMobility(String response)  {


        try
        {
            EventCounter[NwdafEvent.UE_MOBILITY.ordinal()].incrementSubscriptionNotificationsReceived();

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

    public Object nwdaf_analyticsUEmobility(String supi) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        NnwdafEventsSubscriptionUEmobility nnwdafEventsSubscriptionUEmobility = new NnwdafEventsSubscriptionUEmobility();
        nnwdafEventsSubscriptionUEmobility.setSupi(supi);

        Object supiDataList = check_For_dataUEmobility(nnwdafEventsSubscriptionUEmobility, true);

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return supiDataList;
    }



    private void unsubscribeNF_forUEMobility(String subscriptionId) throws IOException, JSONException {

        String supi;

        if ((supi = repository.unsubscribeNFForUE(subscriptionId)) != null) {

            unsubscribeFromNWDAF_forUEMobility(supi);

            repository.deleteEntry_UEMobilitySubscriptionTable(supi);

            EventCounter[NwdafEvent.UE_MOBILITY.ordinal()].incrementUnSubscriptionsSent();
        }
    }



    protected void unsubscribeFromNWDAF_forUEMobility(String supi) throws IOException, JSONException {

        // here subscriptionID = unsubCorrealtionID

        // String subscriptionID =  repository.getUnSubCorrelationID(snssais);

        URL obj = new URL(DELETE_AMF_URL);
        //  out.println(DELETE_NRF_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();


        String unSubCorrelationID = repository.getUnSubCorrelationID_UEMobility(supi);
        String correlationID = repository.getCorrelationID_UEMobility(unSubCorrelationID);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", NwdafEvent.UE_MOBILITY.ordinal());
        jsonObject.put("unSubCorrelationId", unSubCorrelationID);
        jsonObject.put("correlationId", correlationID);


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

            String correlationID = notificationData.getString("correlationId");
            ServiceExperienceSubscriptionTable svcExp_supi_snssais = repository.getSupi_snssais_ServiceExperienceSubscriptionTable(correlationID);

            SvcExperience svcExperience = new SvcExperience(mos, upperRange, lowerRange);

            repository.updateServiceExperienceInformation(svcExperience, svcExp_supi_snssais.getSupi(), svcExp_supi_snssais.getSnssai());

            String subscriptionId = repository.getSubscriptionID_ServiceExperienceSubscriptionData(svcExp_supi_snssais.getSupi(), svcExp_supi_snssais.getSnssai());
            String notificationURI = repository.getNotificationURI(subscriptionId);

            ServiceExperienceNotification svcExpNotifyData = new ServiceExperienceNotification();

            svcExpNotifyData.setSubscriptionId(subscriptionId);
            svcExpNotifyData.setNotificationURI(notificationURI);
            svcExpNotifyData.setSvcExpInfo(svcExperience);
            svcExpNotifyData.setSupi(svcExp_supi_snssais.getSupi());
            svcExpNotifyData.setSnssai(svcExp_supi_snssais.getSnssai());

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


    public void notificationHandler_QosSustainability(JSONObject jsonData)
    {
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);
        EventCounter[NwdafEvent.QOS_SUSTAINABILITY.ordinal()].incrementSubscriptionNotificationsReceived();


        try
        {
            String correlationId = jsonData.getString("correlationId");
            QosThresholdType qosThresholdType;
            Integer threshold;

            if(jsonData.has("ranUeThrou"))
            {
                qosThresholdType = QosThresholdType.RAN_UE_THROUGHPUT;
                threshold = jsonData.getInt("ranUeThrou");
            }

            else
            {
                qosThresholdType = QosThresholdType.QOS_FLOW_RETAIN;
                threshold = jsonData.getInt("qosFlowRet");
            }


            QosSustainabilitySubscriptionData qosSustainabilitySubscriptionData = repository.getTaiSnssai_ByCorrelationId_QosSustainability(correlationId);

            String tai = qosSustainabilitySubscriptionData.getTai();
            String snssai = qosSustainabilitySubscriptionData.getSnssai();

            repository.updateQosSustainabilityThresholdLevel(tai, snssai, threshold, qosThresholdType);

            qosSustainabilityNotificationManager(tai, snssai, threshold, qosThresholdType);

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




    /*************************************NETWORK_PERFORMANCE***********************************************************/


    public void notificationHandler_NetworkPerformance(String response)
    {
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);
        EventCounter[NwdafEvent.NETWORK_PERFORMANCE.ordinal()].incrementSubscriptionNotificationsReceived();

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

            String correlationId = notificationData.getString("correlationId");
            NetworkPerformanceSubscriptionData nwPerfSubData = repository.getSupi_nwPerfType_byCorrelationID(correlationId);

            NetworkPerfType nwPerfType = NetworkPerfType.values()[nwPerfSubData.getNwPerfType()];
            NetworkPerfThreshold networkPerfThreshold = NetworkPerfValue.getThresholdType(nwPerfType);


            // DELETE Analytics entry in SubscriptionTable (refCount = 0)
            repository.deleteAnalyticsEntry_NetworkPerformance(correlationId);


            if(networkPerfThreshold == NetworkPerfThreshold.RELATIVE_RATIO)
            {
                repository.updateRelativeRatio_NetworkPerf(reportingThreshold, nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType());
                NetworkPerformanceSubscriptionData crossedThresholdData = repository.getCrossedNwPerfType_subscriptionID_NetworkPerf(nwPerfSubData.getSupi(), nwPerfSubData.getNwPerfType(), reportingThreshold, NetworkPerfThreshold.RELATIVE_RATIO);

                if(crossedThresholdData != null)
                {
                    NetworkPerformanceNotification nwPerfNotifyData = new NetworkPerformanceNotification();

                    nwPerfNotifyData.setSubscriptionId(crossedThresholdData.getSubscriptionId());
                    nwPerfNotifyData.setNotificationURI(repository.getNotificationURI(crossedThresholdData.getSubscriptionId()));
                    nwPerfNotifyData.setNwPerfType(crossedThresholdData.getNwPerfType());
                    nwPerfNotifyData.setRelativeRatio(reportingThreshold);

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

                    nwPerfNotifyData.setSubscriptionId(crossedThresholdData.getSubscriptionId());
                    nwPerfNotifyData.setNotificationURI(repository.getNotificationURI(crossedThresholdData.getSubscriptionId()));
                    nwPerfNotifyData.setNwPerfType(crossedThresholdData.getNwPerfType());
                    nwPerfNotifyData.setAbsoluteNum(reportingThreshold);


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
        EventCounter[NwdafEvent.USER_DATA_CONGESTION.ordinal()].incrementSubscriptionNotificationsReceived();


        try
        {
            Integer congLevel = notifyData.getInt("congLevel");
            String correlationID = notifyData.getString("correlationId");

            UserDataCongestionSubscriptionTable usrDataCongDetails = repository.getUserDataCongestionDetails_byCorrelationID(correlationID);
            repository.updateCongestionLevel_UserDataCongestion(congLevel, usrDataCongDetails.getSupi(), usrDataCongDetails.getCongType(), usrDataCongDetails.getTai());

            // DELETE Analytics entry in SubscriptionTable (refCount = 0)
            repository.deleteAnalyticsEntry_UserDataCongestion(correlationID);

            if(repository.congestionLevelThresholdCrossed(usrDataCongDetails.getSupi(), usrDataCongDetails.getCongType(), congLevel))
            {
                UserDataCongestionNotification usrDataCongNotifyData = new UserDataCongestionNotification();

                usrDataCongNotifyData.setSubscriptionId(repository.getSubscriptionID_UserDataCongestion(usrDataCongDetails.getSupi(), usrDataCongDetails.getCongType()));
                usrDataCongNotifyData.setNotificationURI(repository.getNotificationURI(usrDataCongNotifyData.getSubscriptionId()));

                String areaInfo[] = usrDataCongDetails.getTai().split("-");

                usrDataCongNotifyData.setTai(new Tai(new PlmnId(areaInfo[0], areaInfo[1]), areaInfo[2]));
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



    /*************************************ABNORMAl_BEHAVIOUR***********************************************************/



    public void notificationHandler_AbnormalBehaviour(JSONObject jsonData) throws JSONException, IOException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);
        EventCounter[NwdafEvent.ABNORMAL_BEHAVIOUR.ordinal()].incrementSubscriptionNotificationsReceived();


        String correlationId =  jsonData.getString("correlationId");
        Integer excepLevel = jsonData.getInt("excepLevel");

        AbnormalBehaviourSubscriptionData abnorBehavrData = repository.getSupi_ExcepId_ByCorrelationId_AbnormalBehaviour(correlationId);

        String supi = abnorBehavrData.getSupi();
        Integer excepId = abnorBehavrData.getExcepId();

        repository.updateExcepLevel_AbnormalBehaviour(excepLevel, supi, excepId);

        if(repository.thresholdCrossed_AbnormalBehaviour(supi, excepId, excepLevel))
        {
            String subscriptionId = repository.getSubscriptionId_AbnormalBehaviour(supi, excepId);
            String notificationURI = repository.getNotificationURI(subscriptionId);

            AbnormalBehaviourNotification abnorBehavrNotification = new AbnormalBehaviourNotification();

            abnorBehavrNotification.setSubscriptionId(subscriptionId);
            abnorBehavrNotification.setNotificationURI(notificationURI);
            abnorBehavrNotification.getSupi().add(supi);

            abnorBehavrNotification.getExceps().setExcepId(ExceptionId.values()[excepId]);
            abnorBehavrNotification.getExceps().setExcepLevel(excepLevel);

            sendAbnormalBehaviourNotification(abnorBehavrNotification);
        }
    }





    /*************************************UE_COMM***********************************************************/


    public void notificationHandler_UeComm(JSONObject jsonData) throws JSONException, IOException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);
        EventCounter[NwdafEvent.UE_COMM.ordinal()].incrementSubscriptionNotificationsReceived();


        String correlationId = jsonData.getString("correlationId");

        UeCommInformation ueCommInformation = new UeCommInformation();

        ueCommInformation.setCommDur(jsonData.getInt("commDur"));
        ueCommInformation.setTs(jsonData.getString("ts"));
        ueCommInformation.setUlVol(jsonData.getInt("ulVol"));
        ueCommInformation.setDlVol(jsonData.getInt("dlVol"));

        String supi = repository.getSupiByCorrelationId_UeComm(correlationId);
        ueCommInformation.setSupi(supi);


        // DELETE analytics entry if(refCount == 0)
        repository.deleteAnalyticsEntry_UeComm(correlationId);


        Integer currentAnaEntry = repository.getSupiTimeSlotsCount_UeComm(supi);

        if(currentAnaEntry > 0 && repository.maxAnaEntryReached(supi, currentAnaEntry))
        { repository.deleteLRUtimeSlot_UeComm(supi); }


        repository.addUeCommInformation(ueCommInformation);

        String subscriptionId = repository.getSubscriptionBySupi_UeComm(supi);
        String notificationURI = repository.getNotificationURI(subscriptionId);

        UeCommNotification ueCommNotification = new UeCommNotification();

        ueCommNotification.setSubscriptionId(subscriptionId);
        ueCommNotification.setNotificationURI(notificationURI);
        ueCommNotification.setCommDur(ueCommInformation.getCommDur());
        ueCommNotification.setTs(ueCommInformation.getTs());
        ueCommNotification.setUlVol(ueCommInformation.getUlVol());
        ueCommNotification.setDlVol(ueCommInformation.getDlVol());

        sendUeCommNotification(ueCommNotification);
    }




    /*************************************NF_LOAD***********************************************************/


    public void notificationHandler_NfLoad(JSONObject jsonData) throws JSONException, IOException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);
        EventCounter[NwdafEvent.NF_LOAD.ordinal()].incrementSubscriptionNotificationsReceived();

        String correlationId = jsonData.getString("correlationId");
        Integer threshold = jsonData.getInt("threshold");
        NfThresholdType thresholdType = NfThresholdType.values()[jsonData.getInt("thresholdType")];

        NfLoadSubscriptionData nfLoadSubscriptionData = repository.getNfType_NfInstanceId_ByCorrelationId(correlationId);

        Integer nfType = nfLoadSubscriptionData.getNfType();
        String nfInstanceId = nfLoadSubscriptionData.getNfInstanceId();

        repository.updateNfThresholdLevel(nfType, nfInstanceId, threshold, thresholdType);

        nfLoadNotificationManager(nfType, nfInstanceId, threshold, thresholdType);
    }





    /********************************************************************************************************************/




    public void notificationHandlerOAM(String response) throws JSONException, IOException {

        JSONObject json = new JSONObject(response);

        NwdafEvent event = NwdafEvent.values()[json.getInt("event")];

        switch(event)
        {
            case QOS_SUSTAINABILITY: notificationHandler_QosSustainability(json);
                                     break;

            case NETWORK_PERFORMANCE: notificationHandler_NetworkPerformance(response);
                                      break;

            case USER_DATA_CONGESTION: notificationHandler_UserDataCongestion(json);
                                       break;

            case NF_LOAD: notificationHandler_NfLoad(json);
                          break;
        }
    }



    public void notificationHandlerSMF(String response) throws JSONException, IOException {

        JSONObject json = new JSONObject(response);

        NwdafEvent event = NwdafEvent.values()[json.getInt("event")];

        switch(event)
        {
            case ABNORMAL_BEHAVIOUR: notificationHandler_AbnormalBehaviour(json);
                                     break;

            case UE_COMM: notificationHandler_UeComm(json);
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