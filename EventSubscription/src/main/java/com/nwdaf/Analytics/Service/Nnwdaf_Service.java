package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Model.APIBuildInformation;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.NnwdafEventsSubscriptionUEmobility;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionModel;
import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.AnalyticsError;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.EventError;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.UeMobilityAnalyticsError;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator.EventValidator;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator.QosSustainabilityAnalyticsValidator;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator.SliceLoadLevelAnalyticsValidator;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator.UeMobilityAnalyticsValidator;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.EssentialsError;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.QosSustainabilityError;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.SliceLoadLevelError;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.UeMobilityError;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator.EssentialsValidator;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator.QosSustainabilityValidator;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator.SliceLoadLevelValidator;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator.UeMobilityValidator;
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
import java.math.BigInteger;
import java.net.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import static org.springframework.http.HttpHeaders.USER_AGENT;


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

    public Object nwdaf_analytics(AnalyticsRawData rawData) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
        Object validate;


        // Validate EventID
        if((validate = EventValidator.check(rawData, nnwdafEventsSubscription)) instanceof EventError)
        { return new ResponseEntity<EventError>((EventError)validate, HttpStatus.NOT_ACCEPTABLE); }

        nnwdafEventsSubscription = (NnwdafEventsSubscription)validate;



        // Validate other attributes for specific EventID
        if(nnwdafEventsSubscription.getEventID() == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        {
            if((validate = SliceLoadLevelAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof AnalyticsError)
            { return new ResponseEntity<AnalyticsError>((AnalyticsError)validate, HttpStatus.NOT_ACCEPTABLE); }
        }

        else if(nnwdafEventsSubscription.getEventID() == EventID.QOS_SUSTAINABILITY.ordinal())
        {
            if((validate = QosSustainabilityAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof AnalyticsError)
            { return new ResponseEntity<AnalyticsError>((AnalyticsError)validate, HttpStatus.NOT_ACCEPTABLE); }
        }

        else if(nnwdafEventsSubscription.getEventID() == EventID.UE_MOBILITY.ordinal())
        {
            if((validate = UeMobilityAnalyticsValidator.check(rawData, nnwdafEventsSubscription)) instanceof UeMobilityAnalyticsError)
            { return new ResponseEntity<UeMobilityAnalyticsError>((UeMobilityAnalyticsError)validate, HttpStatus.NOT_ACCEPTABLE); }
        }


        nnwdafEventsSubscription = (NnwdafEventsSubscription)validate;


        if(nnwdafEventsSubscription.getEventID() == EventID.UE_MOBILITY.ordinal())
        { return nwdaf_analyticsUEmobility(nnwdafEventsSubscription); }


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

        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
        Object validator;


        //Check for essential attributes
        if((validator = EssentialsValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof EssentialsError)
        { return new ResponseEntity<EssentialsError>((EssentialsError)validator, HttpStatus.NOT_ACCEPTABLE); }

        nnwdafEventsSubscription = (NnwdafEventsSubscription)validator;



        //Validate Event specific data
        if(nnwdafEventsSubscription.getEventID() == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        {
            if((validator = SliceLoadLevelValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof SliceLoadLevelError)
            { return new ResponseEntity<SliceLoadLevelError>((SliceLoadLevelError)validator, HttpStatus.NOT_ACCEPTABLE); }
        }

        else if(nnwdafEventsSubscription.getEventID() == EventID.QOS_SUSTAINABILITY.ordinal())
        {
            if((validator = QosSustainabilityValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof QosSustainabilityError)
            { return new ResponseEntity<QosSustainabilityError>((QosSustainabilityError)validator, HttpStatus.NOT_ACCEPTABLE); }
        }


        else if(nnwdafEventsSubscription.getEventID() == EventID.UE_MOBILITY.ordinal())
        {
            if((validator = UeMobilityValidator.check(subscriptionRawData, nnwdafEventsSubscription)) instanceof UeMobilityError)
            { return new ResponseEntity<UeMobilityError>((UeMobilityError)validator, HttpStatus.NOT_ACCEPTABLE); }
        }


        nnwdafEventsSubscription = (NnwdafEventsSubscription)validator;


        logger.info("consumer is subscribing for an event and providing these details. eventID:  " + nnwdafEventsSubscription.getEventID() + "\n" + " notificationURI:  " + nnwdafEventsSubscription.getNotificationURI() + "\n" +
                " snssais:  " + nnwdafEventsSubscription.getSnssais() + "\n" + " notifMethod:  " + nnwdafEventsSubscription.getNotifMethod() + "\n" +
                " repetitionPeriod:  " + nnwdafEventsSubscription.getRepetitionPeriod() + "\n" + " loadLevelThreshold: " + nnwdafEventsSubscription.getLoadLevelThreshold());

        // Random generating SubscriptionID
        UUID subscriptionID = FrameWorkFunction.getUniqueID();

        // setting subscription ID
        nnwdafEventsSubscription.setSubscriptionID(String.valueOf(subscriptionID));

        // Incrementing Subscription Counter
        Counters.incrementSubscriptions();

        int eventID = nnwdafEventsSubscription.getEventID();


        if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        {
            // Adding data into NwdafSubscriptionTable   [ Database ]
            repository.subscribeNF(nnwdafEventsSubscription, EventID.LOAD_LEVEL_INFORMATION);

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

            Object obj = check_For_data(nnwdafEventsSubscription, false);

            if (obj instanceof ResponseEntity) {
                return obj;
            }
        }


        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        {
            repository.subscribeNF(nnwdafEventsSubscription, EventID.QOS_SUSTAINABILITY);

            repository.addDataQosSustainability(nnwdafEventsSubscription);
            repository.addDataQosSustainabilitySubscriptionData(nnwdafEventsSubscription);
            repository.setNwdafQosSustainabilityInformation(nnwdafEventsSubscription.getSnssais());

            Object obj = check_For_data(nnwdafEventsSubscription, false);

            if (obj instanceof ResponseEntity) {
                return obj;
            }
        }


        else if(eventID == EventID.UE_MOBILITY.ordinal())
        {
            repository.subscribeNF(nnwdafEventsSubscription, EventID.UE_MOBILITY);
            return perform_UEMobility(nnwdafEventsSubscription);
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
                "notifMethod:  " + updateData.getNotifMethod() + "\n" +
                "repetitionPeriod:  " + updateData.getRepetitionPeriod() +
                "loadLevelThreshold:  " + updateData.getLoadLevelThreshold() + "\n" +
                "ranUeThroughputThreshold:  " + updateData.getRanUeThroughputThreshold() + "\n" +
                "qosFlowRetainThreshold:  " + updateData.getQosFlowRetainThreshold() + "\n");

        SubscriptionTable subscription = repository.findById_subscriptionID(subscriptionID);

        if (subscription == null) {
            logger.warn("no Content");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
        }

        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
        nnwdafEventsSubscription.setSubscriptionID(subscriptionID);

        Object validator;

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

        if (subscriber.getEventID() == EventID.UE_MOBILITY.ordinal()) {
            unsubscribeNF_forUEMobility(subscriber);
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

        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);

    }


    public void notificationHandlerForUEMobility(String response) throws JSONException, IOException {

        // System.out.println("In-Notification-Handler-for-UE-Mobility");
        //JSONArray jsonArray = new JSONArray(response);
        JSONArray jsonArray = new JSONArray(response);
        String correlationID = jsonArray.get(0).toString();
        String cellID;
        String TacValue;
        Integer MCC_value;
        Integer MNC_value;
        JSONObject plmnObject = new JSONObject();

        // At 0 position I am sending CorrelationID;
        for (int i = 1; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // String correlationID = jsonObject.getString("correlationID");
            Integer timeDurationValue = jsonObject.getInt("timeDuration");

            // System.out.println("TimeDurationValue " + timeDurationValue);

            JSONObject taiValue = jsonObject.getJSONObject("Tai");
            JSONObject ecqiValue = jsonObject.getJSONObject("Ecqi");

            cellID = ecqiValue.getString("cellID");
            TacValue = taiValue.getString("Tac");
            plmnObject = taiValue.getJSONObject("plmn");
            MCC_value = plmnObject.getInt("MCC");
            MNC_value = plmnObject.getInt("MNC");
            String TaiValue = MCC_value + "," + MNC_value + ":" + TacValue;

            // update all the location of supi received in JsonArray;
            add_value_to_userLocationTable(TaiValue, cellID, timeDurationValue);
        }

        String supi = repository.getSupiValueByCorrelationID(correlationID);


        // now time to update UE-MobilityTable;
        // First Fetch all ID
        updateUEMobilityTable(correlationID);

        if (repository.getRefCount_UEmobilitySubscriptionTable(supi) == 0) {
            repository.deleteEntry_UEMobilitySubscriptionTable(supi);
        }

       // repository.getRefCount_UEmobilitySubscriptionTable()
        nwdaf_notification_manager_ForUEMobility();

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



    private void unsubscribeNF_forUEMobility(SubscriptionTable subscriber) throws Exception {

        String supi;


        if ((supi = repository.unsubscribeNFForUE(subscriber)) != null) {

            Integer eventID = subscriber.getEventID();

            unsubscribeFromNWDAF_forUEMobility(supi, eventID);

            repository.deleteEntry_UEMobilitySubscriptionTable(supi);

        }
    }



    protected void unsubscribeFromNWDAF_forUEMobility(String supi, Integer eventID) throws Exception {

        // here subscriptionID = unsubCorrealtionID

        // String subscriptionID =  repository.getUnSubCorrelationID(snssais);

        URL obj = new URL(DELETE_AMF_URL);
        //  out.println(DELETE_NRF_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        String mCorrelationID = "";
        String correlationID = "";


        mCorrelationID = repository.getUnSubCorrelationID_UEMobility(supi);
        correlationID = repository.getCorrelationID_UEMobility(mCorrelationID);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mSubcorrelationID", mCorrelationID);
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




    /************************************************************************************************************************/

}
