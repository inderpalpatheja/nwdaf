package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Controller.ConnectionCheck.MissingData;
import com.nwdaf.Analytics.Model.APIBuildInformation;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.TableType.SubscriptionTable;
import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
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
public class Nnwdaf_Service extends FrameWorkFunctions {


    @Autowired
    Nnwdaf_Repository repository;

    //@Autowired
    //BuildProperties buildProperties;


    private static final Logger logger = LoggerFactory.getLogger(Nnwdaf_Service.class);


    List<UUID> subIDs;
    String updated_POST_NRF_URL = null;






    /************************************************************************************************************************/



    public Object nwdaf_analytics(String snssais, boolean anySlice, int eventID) throws IOException, JSONException {

        logger.debug("Entered nwdaf_analytics");

        logger.debug("Entered getAllAnalyticsInformation");


        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
        nnwdafEventsSubscription.setSnssais(snssais);
        nnwdafEventsSubscription.setAnySlice(anySlice);

        Object snssaisDataList = check_For_data(nnwdafEventsSubscription, true);

        logger.debug("Exit nwdaf_analytics");
        return snssaisDataList;
    }






    public Object nwdaf_subscription(NnwdafEventsSubscription nnwdafEventsSubscription) throws SQLIntegrityConstraintViolationException, URISyntaxException, IOException, JSONException {

        //Test Simulator Connection
        if (!testConnection()) {
            return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
        }


        NnwdafEventsSubscription checkSubscription = nnwdafEventsSubscription;

        if((checkSubscription = Validator.check(checkSubscription)) == null)
        { return new ResponseEntity<MissingData>(new MissingData(nnwdafEventsSubscription), HttpStatus.NOT_ACCEPTABLE); }

        nnwdafEventsSubscription = checkSubscription;


        logger.debug("Enter nwdaf_subscription");
        logger.info("consumer is subscribing for an event and providing these details. eventID:  " + nnwdafEventsSubscription.getEventID() + "\n" + " notificationURI:  " + nnwdafEventsSubscription.getNotificationURI() + "\n" +
                " snssais:  " + nnwdafEventsSubscription.getSnssais() + "\n" + " notifMethod:  " + nnwdafEventsSubscription.getNotifMethod() + "\n" +
                " repetitionPeriod:  " + nnwdafEventsSubscription.getRepetitionPeriod() + "\n" + " loadLevelThreshold: " + nnwdafEventsSubscription.getLoadLevelThreshold());

        // Random generating SubscriptionID
        UUID subscriptionID = UUID.randomUUID();

        // setting subscription ID
        nnwdafEventsSubscription.setSubscriptionID(String.valueOf(subscriptionID));

        // Adding data into NwdafSubscriptionTable   [ Database ]
        repository.subscribeNF(nnwdafEventsSubscription);

        // Incrementing Subscription Counter
        Counters.incrementSubscriptions();
        showCounters();

        // adding values into subscriptionData
        add_values_into_subscriptionData(subscriptionID, nnwdafEventsSubscription.getSnssais(),
                nnwdafEventsSubscription.getLoadLevelThreshold());


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


        logger.info("sending response to NF");
        // function to send response header to NF
        HttpHeaders responseHeaders = send_response_header_to_NF(subscriptionID);

        logger.debug("Exit nwdaf_subscription");
        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);
    }






    public ResponseEntity<?> update_nf_subscription(String subscriptionID, NnwdafEventsSubscription nnwdafEventsSubscription) {

        logger.info("subscriptionID:  " + nnwdafEventsSubscription.getSubscriptionID() + "\n" +
                "eventID:  " + nnwdafEventsSubscription.getEventID() + "\n" +
                "notificationURI:  " + nnwdafEventsSubscription.getNotificationURI() + "\n" +
                "notifMethod:  " + nnwdafEventsSubscription.getNotifMethod() + "\n" +
                "repetitionPeriod:  " + nnwdafEventsSubscription.getRepetitionPeriod());

        SubscriptionTable nwdafSubscriptionTableModel = repository.findById_subscriptionID(subscriptionID);

        if (nwdafSubscriptionTableModel == null) {
            logger.warn("no Content");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
        }

        // Updating user via subscriptionID
        repository.updateNF(nnwdafEventsSubscription, subscriptionID);

        // Incrementing update counter
        Counters.incrementSubscriptionUpdates();
        showCounters();

        logger.debug("Exit update_nf_subscription()");
        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.OK);
    }




    public ResponseEntity<?> unsubscription_nf(String subscriptionID) throws Exception {

        Counters.incrementUnSubscriptions();
        showCounters();

        logger.debug("Entered UnsubscribeNf()");
        SubscriptionTable sub = repository.findById_subscriptionID(subscriptionID);
        String snssais = repository.getSnssais(subscriptionID);
        //String unsubId = repository.getUnSubCorrelationID(snssais);

       /* if(repository.getRefCount(snssais) < 1){
            out.println("Call Unsubscribe From NWDAF");
            unsubscribeFromNWDAF();

        }*/

        if (sub == null) {
            logger.warn("subscriptionID= not found");
            //  out.println("subscriptionID= not found");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND);
        }


        if (0 == repository.unsubscribeNF(subscriptionID)) {

            repository.RemoveNwDafSubscriptionEntry(snssais);
            unsubscribeFromNWDAF(snssais);
        }


        logger.debug("Exit unsubscribe NF");
        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
    }





    public ResponseEntity<?> get_all_network_function(String id) {

        logger.debug("Entered getNF()");
        SubscriptionTable user = repository.findById_subscriptionID(id);

        if (user == null) {
            logger.warn("subscriptionID= not found");
            return new ResponseEntity<SubscriptionTable>(HttpStatus.NOT_FOUND);
        }
        logger.debug("Exit getNF()");
        return new ResponseEntity<SubscriptionTable>(user, HttpStatus.OK);
    }





    public void acceptingNotification(String response) throws Exception {

        // Here Data will be received and nwdafSliceLoadLevelInformation Table will get populated.
        logger.debug("Entered Accepting Notification");

        // here subID == correlationID

        JSONObject json = new JSONObject(response);

        // String correlationID = json.getString("correlationID");
        int currentLoadLevel = json.getInt("currentLoadLevel");
        String correlationID = json.getString("correlationID");

        //out.println("received from SIMULATOR - " + "currentLoadLevel - " + currentLoadLevel + correlationID);

        String snssais = repository.getSnssaisByCorrelationID(correlationID);

        /*
         if(repository.isGetAnalytics(snssais)) {
            repository.decrementRefCount(snssais);
            repository.setGetAnalytics(snssais, false);
        } */

        if (repository.getRefCount(snssais) == 0) {
            repository.decrementRefCount(snssais);
        }


        repository.updateCurrentLoadLevel(currentLoadLevel, snssais);
        nwdaf_notification_manager();

        //repository.getSnssaisViaSubID(correlationID);

        // incrementing notification Counter
        Counters.incrementCollectorSubscriptionNotifications();
        showCounters();
    }





    public HashMap<String, BigInteger> nwdaf_counters() throws Exception {

        logger.debug("Entered nwdaf_counters()");

        HashMap<String, BigInteger> map = new HashMap<String, BigInteger>();

        map.put("Event_Subscriptions", Counters.getSubscriptions());
        map.put("Event_UnSubscriptions", Counters.getUnSubscriptions());
        map.put("Event_SubscriptionUpdates", Counters.getSubscriptionUpdates());
        map.put("Event_SubscriptionNotifications", Counters.getSubscriptionNotifications());

        map.put("Collector_Subscriptions", Counters.getCollectorSubscriptions());
        map.put("Collector_SubscriptionNotifications", Counters.getCollectorSubscriptionNotifications());
        map.put("Collector_AnalyticsSubscriptions", Counters.getAnalyticsSubscriptions());
        map.put("Collector_AnalyticsNotifications", Counters.getAnalyticsNotifications());

        logger.debug("Exit nwdaf_counters()");
        return map;
    }



    public String nwdaf_reset_counter() {

        logger.debug("Enter resetCounters()");

        Counters.reset();

        logger.debug("Exit resetCounters()");
        return "Counters set to 0.";
    }




   /* public Object check_api_details() throws IOException {

        APIBuildInformation apiBuildInformation = new APIBuildInformation();


        apiBuildInformation.setAPI_NAME(buildProperties.getArtifact());
        apiBuildInformation.setAPI_BUILD_TIME(buildProperties.getTime());
        apiBuildInformation.setAPI_VERSION(buildProperties.getVersion());


        return apiBuildInformation;

    }



    // Adding Swagger Configurations
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
    }*/





    /************************************************************************************************************************/



}
