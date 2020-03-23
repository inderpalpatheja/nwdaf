package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Controller.ConnectionCheck.MissingData;
import com.nwdaf.Analytics.Model.APIBuildInformation;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SubscriptionTable;
import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
import com.nwdaf.Analytics.Service.Validator.InvalidType;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator;
import com.nwdaf.Analytics.Service.Validator.TypeChecker;
import com.nwdaf.Analytics.Service.Validator.UpdateValidator;
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
import java.math.BigInteger;
import java.net.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;




@Service
public class Nnwdaf_Service extends BusinessLogic {


    @Autowired
    Nnwdaf_Repository repository;

    @Autowired
    BuildProperties buildProperties;


    private static final Logger logger = LoggerFactory.getLogger(Nnwdaf_Service.class);
    List<UUID> subIDs;
    String updated_POST_NRF_URL = null;

    /************************************************************************************************************************/

    public Object nwdaf_analytics(String snssais, boolean anySlice, int eventID) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();

        nnwdafEventsSubscription.setSnssais(snssais);
        nnwdafEventsSubscription.setAnySlice(anySlice);
        nnwdafEventsSubscription.setEventID(eventID);

        Object snssaisDataList = check_For_data(nnwdafEventsSubscription, true);

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return snssaisDataList;
    }


    public Object nwdaf_subscription(SubscriptionRawData subscriptionRawData) throws SQLIntegrityConstraintViolationException, URISyntaxException, IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        //Test Simulator Connection
        if (!testConnection()) {
            return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
        }

        NnwdafEventsSubscription nnwdafEventsSubscription;
        Object checkForData;


        // Check for DataType
        if((checkForData = TypeChecker.checkForSubscription(subscriptionRawData)) instanceof NnwdafEventsSubscription)
        { nnwdafEventsSubscription = (NnwdafEventsSubscription)checkForData; }

        else
        { return new ResponseEntity<InvalidType>((InvalidType)checkForData, HttpStatus.NOT_ACCEPTABLE); }


        //Check for Validity
        Object checkSubscription;

        if((checkSubscription = SubscriptionValidator.check(nnwdafEventsSubscription, subscriptionRawData)) instanceof MissingData)
        { return new ResponseEntity<MissingData>((MissingData)checkSubscription, HttpStatus.NOT_ACCEPTABLE); }

        nnwdafEventsSubscription = (NnwdafEventsSubscription)checkSubscription;


        logger.info("consumer is subscribing for an event and providing these details. eventID:  " + nnwdafEventsSubscription.getEventID() + "\n" + " notificationURI:  " + nnwdafEventsSubscription.getNotificationURI() + "\n" +
                " snssais:  " + nnwdafEventsSubscription.getSnssais() + "\n" + " notifMethod:  " + nnwdafEventsSubscription.getNotifMethod() + "\n" +
                " repetitionPeriod:  " + nnwdafEventsSubscription.getRepetitionPeriod() + "\n" + " loadLevelThreshold: " + nnwdafEventsSubscription.getLoadLevelThreshold());

        // Random generating SubscriptionID
        UUID subscriptionID = FrameWorkFunction.getUniqueID();

        // setting subscription ID
        nnwdafEventsSubscription.setSubscriptionID(String.valueOf(subscriptionID));

        // Adding data into NwdafSubscriptionTable   [ Database ]
        repository.subscribeNF(nnwdafEventsSubscription);

        // Incrementing Subscription Counter
        Counters.incrementSubscriptions();

        int eventID = nnwdafEventsSubscription.getEventID();


        if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        {
            // adding values into subscriptionData
            add_values_into_subscriptionData(subscriptionID, nnwdafEventsSubscription.getSnssais(),
                    nnwdafEventsSubscription.getLoadLevelThreshold());


            // if eventId is set to UEMobility then add values into UEMobility Table;
            // repository.add_data_into_UE_mobilityTable();

            // Storing data into loadlevelInformation Table
            repository.add_data_into_load_level_table(nnwdafEventsSubscription.getSnssais());


            // JSON payload send by NF to NWDAF
        /* logger.info("\n" + "SubID : " + nnwdafEventsSubscription.getSubscriptionID() + " NotificationURI : " + nnwdafEventsSubscription.getEventID() +
                " snssais : " + nnwdafEventsSubscription.getSnssais() + " anySlice : " + nnwdafEventsSubscription.getAnySlice() +
                " notifMethod : " + nnwdafEventsSubscription.getNotifMethod() + " repetition Method : " + nnwdafEventsSubscription.getRepetitionPeriod() +
                " LoadLevelThreshold : " + nnwdafEventsSubscription.getLoadLevelThreshold()); */
            //getAnalytics = false;
            // function to check snssais data
        }


        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        {
            nnwdafEventsSubscription.set_5Qi((Integer)subscriptionRawData.get_5Qi());
            nnwdafEventsSubscription.setMcc((String)subscriptionRawData.getMcc());
            nnwdafEventsSubscription.setMnc((String)subscriptionRawData.getMnc());
            nnwdafEventsSubscription.setPlmnID(nnwdafEventsSubscription.getMcc(), nnwdafEventsSubscription.getMnc());
            nnwdafEventsSubscription.setTac((String)subscriptionRawData.getTac());

            if(subscriptionRawData.getRanUeThroughputThreshold() != null)
            { nnwdafEventsSubscription.setRanUeThroughputThreshold((Integer)subscriptionRawData.getRanUeThroughputThreshold()); }

            if(subscriptionRawData.getQosFlowRetainThreshold() != null)
            { nnwdafEventsSubscription.setQosFlowRetainThreshold((Integer)subscriptionRawData.getQosFlowRetainThreshold());; }

            repository.addDataQosSustainability(nnwdafEventsSubscription);
            repository.addDataQosSustainabilitySubscriptionData(nnwdafEventsSubscription);
            repository.setNwdafQosSustainabilityInformation(nnwdafEventsSubscription);

        }

        Object obj = check_For_data(nnwdafEventsSubscription, false);

        if (obj instanceof ResponseEntity) {
            return obj;
        }


        logger.info("sending response to NF");
        // function to send response header to NF
        HttpHeaders responseHeaders = send_response_header_to_NF(subscriptionID);

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);
    }






    public ResponseEntity<?> update_nf_subscription(String subscriptionID, SubUpdateRawData updateData) {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        logger.info("subscriptionID:  " + subscriptionID + "\n" +
                "eventID:  " + updateData.getEventID() + "\n" +
                "notifMethod:  " + updateData.getNotifMethod() + "\n" +
                "repetitionPeriod:  " + updateData.getRepetitionPeriod());

        SubscriptionTable nwdafSubscriptionTableModel = repository.findById_subscriptionID(subscriptionID);

        if (nwdafSubscriptionTableModel == null) {
            logger.warn("no Content");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
        }

        NnwdafEventsSubscription nnwdafEventsSubscription;
        Object checkForData;


        if((checkForData = TypeChecker.checkForUpdateSub(updateData)) instanceof NnwdafEventsSubscription)
        { nnwdafEventsSubscription = (NnwdafEventsSubscription)checkForData; }

        else
        { return new ResponseEntity<InvalidType>((InvalidType)checkForData, HttpStatus.NOT_ACCEPTABLE); }


        Integer loadLevelThreshold = repository.getLoadLevelThreshold(subscriptionID);

        if((checkForData = UpdateValidator.check(nnwdafEventsSubscription, updateData, nwdafSubscriptionTableModel, loadLevelThreshold)) instanceof MissingData)
        { return new ResponseEntity<MissingData>((MissingData)checkForData, HttpStatus.NOT_ACCEPTABLE); }

        nnwdafEventsSubscription = (NnwdafEventsSubscription)checkForData;

        // Updating user via subscriptionID
        repository.updateNF(nnwdafEventsSubscription, subscriptionID);

        // Incrementing update counter
        Counters.incrementSubscriptionUpdates();


        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.OK);
    }




    public ResponseEntity<?> unsubscription_nf(String subscriptionID) throws Exception {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        Counters.incrementUnSubscriptions();

        SubscriptionTable subscriber = repository.findById_subscriptionID(subscriptionID);
        //String snssais = repository.getSnssais_LoadLevelSubscriptionData(subscriptionID);
        //String unsubId = repository.getUnSubCorrelationID(snssais);

       /* if(repository.getRefCount(snssais) < 1){
            out.println("Call Unsubscribe From NWDAF");
            unsubscribeFromNWDAF();

        }*/

        if (subscriber == null) {
            logger.warn("subscriptionID= not found");
            //  out.println("subscriptionID= not found");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND);
        }

        String snssais;

        if ((snssais = repository.unsubscribeNF(subscriber)) != null) {

            Integer eventID = subscriber.getEventID();

            unsubscribeFromNWDAF(snssais, eventID);

            if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
            { repository.deleteEntry_SliceLoadLevelSubscriptionTable(snssais); }

            else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
            { repository.deleteEntry_QosSustainabilitySubscriptionTable(snssais); }

        }


        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
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





    public void notificationHandler(String response, EventID eventID) throws Exception {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if(eventID == EventID.LOAD_LEVEL_INFORMATION)
        {
            // here subID == correlationID

            JSONObject json = new JSONObject(response);

            // String correlationID = json.getString("correlationID");
            int currentLoadLevel = json.getInt("currentLoadLevel");
            String correlationID = json.getString("correlationID");

            //out.println("received from SIMULATOR - " + "currentLoadLevel - " + currentLoadLevel + correlationID);

            String snssais = repository.getSnssaisByCorrelationID(correlationID, eventID);

        /*
         if(repository.isGetAnalytics(snssais)) {
            repository.decrementRefCount(snssais);
            repository.setGetAnalytics(snssais, false);
        } */

            if (repository.getRefCount(snssais, eventID) == 0) {
                repository.deleteEntry_SliceLoadLevelSubscriptionTable(snssais);
            }


            repository.updateCurrentLoadLevel(currentLoadLevel, snssais);
            nwdaf_notification_manager();

            //repository.getSnssaisViaSubID(correlationID);
        }


        else if(eventID == EventID.QOS_SUSTAINABILITY)
        {
            JSONObject json = new JSONObject(response);

            Integer ranUeThroughput = null;
            Integer qosFlowRetain = null;

            if(json.has("ranUeThroughput"))
            { ranUeThroughput = json.getInt("ranUeThroughput"); }

            else
            { qosFlowRetain = json.getInt("qosFlowRetain"); }

            String correlationID = json.getString("correlationID");
            String snssais = repository.getSnssaisByCorrelationID(correlationID, eventID);

            if(repository.getRefCount(snssais, eventID) == 0)
            { repository.deleteEntry_QosSustainabilitySubscriptionTable(snssais); }


            if(ranUeThroughput != null)
            {
                repository.updateRanUeThroughput(ranUeThroughput, snssais);
                qosNotificationManager(QosType.RAN_UE_THROUGHPUT);
            }

            else if(qosFlowRetain != null)
            {
                repository.updateQosFlowRetain(qosFlowRetain, snssais);
                qosNotificationManager(QosType.QOS_FLOW_RETAIN);
            }
        }


        // incrementing notification Counter
        Counters.incrementCollectorSubscriptionNotifications();

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }





    public HashMap<String, BigInteger> nwdaf_counters() throws Exception {

        return FrameWorkFunction.getStats();
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

}
