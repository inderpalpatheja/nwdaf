package com.nwdaf.Analytics;

import com.nwdaf.Analytics.MetaData.Counters;
import com.nwdaf.Analytics.Model.APIBuildInformation;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelInformationModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelSubscriptionDataModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSubscriptionTableModel;
import com.nwdaf.Analytics.Model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static java.lang.System.out;
import static java.lang.System.setOut;
import static org.springframework.http.HttpHeaders.USER_AGENT;


@RestController

public class Nnwdaf_controller {

    public static Logger logger = Logger.getLogger(Nnwdaf_controller.class.getName());

    Boolean getAnalytics = true;
    Set<String> subID_SET = new HashSet<String>();
    String updated_POST_AMF_URL = null;
    public int subThValue = 0;
    public int currentLoadLevel = 0;
    int flag = 0;


    private final BuildProperties buildProperties;


    List<UUID> subIDs;

    public Nnwdaf_controller(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;

        logger.debug("Entered Nnwdaf_controller()");
        logger.info("Thread started");


        logger.debug("Exit Nnwdaf_controller()");
    }

    final String PATH = "/nnwdaf-eventssubscription/v1";

    @Value("${spring.MAIN.url}")
    String URI;

    @Value("${spring.NotificationTarget.Url}")
    String notificationTargetUrl;

    @Autowired
    Nnwdaf_repository repository;


    // @Autowired
    // BuildProperties buildProperties;

    @Value("${spring.AMF_EVENT_EXPOSURE_SUBSCRIBE.url}")
    String POST_AMF_URL;


    /**
     * @param snssais
     * @param anySlice
     * @param eventID
     * @return
     * @throws IOException
     * @throws JSONException
     * @desc this will hold functions for Analytics information
     */

    @GetMapping("apiroot/nnwdaf-analyticsinfo/v1/{snssais}/{anySlice}/{eventID}")
    @ApiOperation(value = "Get Analytics Details By snssais or anySlice Details",
            notes = "Provide snssais, anySlice and eventID to look up specific analytics Information from NWDAF API",
            response = Object.class)
    public Object nwdaf_analytics(@PathVariable("snssais") String snssais,
                                  @PathVariable("anySlice") Boolean anySlice,
                                  @PathVariable("eventID") int eventID) throws IOException, JSONException {

        getAnalytics = true;
        logger.debug("Entered getAllAnalyticsInformation");

        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
        nnwdafEventsSubscription.setSnssais(snssais);
        nnwdafEventsSubscription.setAnySlice(anySlice);

        Object snssaisDataList = check_For_data(nnwdafEventsSubscription, getAnalytics);

        return snssaisDataList;

    }


    /**
     * @param nnwdafEventsSubscription
     * @return
     * @throws SQLIntegrityConstraintViolationException
     * @throws URISyntaxException
     * @throws IOException
     * @throws JSONException
     * @desc this will hold functions to for event subscriptions
     */
    @PostMapping(PATH + "/subscriptions")
    @ApiOperation(value = "Consumers can subscribe for an event by providing eventID ",
            notes = "Provide Event-Id to subscribe for a particular event",
            response = String.class)
    public ResponseEntity<String> nwdaf_subscription(@RequestBody NnwdafEventsSubscription nnwdafEventsSubscription) throws SQLIntegrityConstraintViolationException, URISyntaxException, IOException, JSONException {

        logger.debug("NF Subscription | Entered subscribeNF()");
        logger.info(" eventID  " + nnwdafEventsSubscription.getEventID() + " notificationURI  " + nnwdafEventsSubscription.getNotificationURI() +
                " snssais  " + nnwdafEventsSubscription.getSnssais() + " notifMethod  " + nnwdafEventsSubscription.getNotifMethod() +
                " repetitionPeriod  " + nnwdafEventsSubscription.getRepetitionPeriod() + " loadLevelThreshold " + nnwdafEventsSubscription.getLoadLevelThreshold());

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
        logger.info("\n" + "SubID : " + nnwdafEventsSubscription.getSubscriptionID() + " NotificationURI : " + nnwdafEventsSubscription.getEventID() +
                " snssais : " + nnwdafEventsSubscription.getSnssais() + " anySlice : " + nnwdafEventsSubscription.getAnySlice() +
                " notifMethod : " + nnwdafEventsSubscription.getNotifMethod() + " repetition Method : " + nnwdafEventsSubscription.getRepetitionPeriod() +
                " LoadLevelThreshold : " + nnwdafEventsSubscription.getLoadLevelThreshold());

        getAnalytics = false;

        // function to check snssais data
        check_For_data(nnwdafEventsSubscription, getAnalytics);

        // function to send response header to NF
        HttpHeaders responseHeaders = send_response_header_to_NF(subscriptionID);

        logger.debug("Exit subscribe NF");
        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);
    }


    /**
     * @param subscriptionID
     * @param snssais
     * @param loadLevelThreshold
     * @desc function to add values in subscription data table
     */
    private void add_values_into_subscriptionData(UUID subscriptionID, String snssais, int loadLevelThreshold) {

        NwdafSliceLoadLevelSubscriptionDataModel nwdafSliceLoadLevelSubscriptionDataModel = new
                NwdafSliceLoadLevelSubscriptionDataModel();

        // Setting values in NwdafSliceLoadLevelSubscriptionData object
        nwdafSliceLoadLevelSubscriptionDataModel.setSubscriptionID(String.valueOf(subscriptionID));
        nwdafSliceLoadLevelSubscriptionDataModel.setSnssais(snssais);
        nwdafSliceLoadLevelSubscriptionDataModel.setLoadLevelThreshold(loadLevelThreshold);

        // Adding nwdafSliceLoadLevelSubscriptionDatamodel into NwdafSliceLoadLevelSubscriptionData Table [ Database ]
        repository.addDataIntoNwdafSliceLoadLevelSubscriptionDataTable(nwdafSliceLoadLevelSubscriptionDataModel);
    }

    /**
     * @param subscriptionID
     * @return
     * @throws URISyntaxException
     * @desc function to send response header to NF consumers
     */
    private HttpHeaders send_response_header_to_NF(UUID subscriptionID) throws URISyntaxException {

        URI location = new URI(URI + "subscriptions/" + String.valueOf(subscriptionID));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return responseHeaders;
    }

    /**
     * @param nnwdafEventsSubscription
     * @throws IOException
     * @throws JSONException
     * @desc function to check snssais data
     */
    private Object check_For_data(NnwdafEventsSubscription nnwdafEventsSubscription, Boolean getAnalytics) throws IOException, JSONException {

        if (getAnalytics.equals(true)) {

            List<EventConnection> snssaisDataList = repository.checkForData(nnwdafEventsSubscription.getSnssais(),
                    nnwdafEventsSubscription.getAnySlice());

            // if data is not found -> calling collector function to collect data
            if (snssaisDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                UUID correlationID = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics, flag);

                // Adding snssais into database
                repository.add_data_into_load_level_table(nnwdafEventsSubscription.getSnssais());

                EventConnection eventConnection = new EventConnection();
                eventConnection.setMessage("Data not Found for " + nnwdafEventsSubscription.getSnssais());
                eventConnection.setCurrentLoadLevelInfo(0);
                eventConnection.setSnssais(nnwdafEventsSubscription.getSnssais());
                eventConnection.setDataStatus(false);

                //throw new NullPointerException("Data not found!");
                return eventConnection;

            } else {
                // flag to check if getAnalytics ref count will be updated or not
                flag = 1;
                UUID correlationID = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics, flag);

                // if Data found returning JSON
                return snssaisDataList;
            }

        } else {
            if (!repository.snsExists(nnwdafEventsSubscription.getSnssais())) {
                getAnalytics = true;
                UUID correlationID = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics, flag);
            } else {
                repository.increment_ref_count(nnwdafEventsSubscription.getSnssais());
            }
        }
        return null;
    }


    /**
     * @param nnwdafEventsSubscription
     * @param getAnalytics
     * @return
     * @throws IOException
     * @throws JSONException
     * @desc this will hold functions for data collection from network functions
     */
    private UUID nwdaf_data_collector(NnwdafEventsSubscription nnwdafEventsSubscription, Boolean getAnalytics, int flag) throws IOException, JSONException {

        logger.debug("Entered Collector Function");

        // Generating CorrelationID
        UUID correlationID = UUID.randomUUID();

        Namf_EventExposure_Subscribe namf_eventExposure_subscribe = new Namf_EventExposure_Subscribe();
        namf_eventExposure_subscribe.setCorrelationId(String.valueOf(correlationID));

        // POST_AMF_URL : POST_AMF_URL + subId;
        // POST_AMF_URL : http::/localhost:8082/Namf_EventExposure_Subscribe/subID [ Reading from file ]

        // Updated URL For NWDAF to Subscribe
        updated_POST_AMF_URL = POST_AMF_URL + "/" + correlationID;

        URL obj = new URL(updated_POST_AMF_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        int responseCode = send_data_receieve_response(namf_eventExposure_subscribe, con);

        response_handler(nnwdafEventsSubscription, responseCode, correlationID, con);


        return correlationID;
    }


    /**
     * @param nnwdafEventsSubscription
     * @param responseCode
     * @param correlationID
     * @param con
     * @throws IOException
     * @desc function to send response header to NF consumers when they subscribe
     */
    private void response_handler(NnwdafEventsSubscription nnwdafEventsSubscription,
                                  int responseCode, UUID correlationID,
                                  HttpURLConnection con) throws IOException {

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            // incrementing Counter
            Counters.incrementCollectorSubscriptions();
            showCounters();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            try {
                if (!repository.snsExists(nnwdafEventsSubscription.getSnssais())) {
                    repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(correlationID,
                            response.toString(), nnwdafEventsSubscription.getSnssais(), getAnalytics);
                } else {
                    if (!repository.isGetAnalytics(nnwdafEventsSubscription.getSnssais())) {
                        repository.setGetAnalytics(nnwdafEventsSubscription.getSnssais(), true);
                        repository.increment_ref_count(nnwdafEventsSubscription.getSnssais());
                    }

                }
            } catch (Exception e) {
                out.println(e.getMessage());
            }
            in.close();
        } else {
            logger.error(" POST request not worked");
        }
    }


    /**
     * @param namf_eventExposure_subscribe
     * @param con
     * @return
     * @throws JSONException
     * @throws IOException
     * @desc function to receive response code
     */
    private int send_data_receieve_response(Namf_EventExposure_Subscribe namf_eventExposure_subscribe, HttpURLConnection con) throws JSONException, IOException {

        JSONObject json = new JSONObject();
        json.put("correlationID", namf_eventExposure_subscribe.getCorrelationId());
        json.put("unSubCorrelationId", namf_eventExposure_subscribe.getUnSubCorrelationId());
        // notificationTargetUrl = Namf_EventExposure_Notify
        json.put("notificationTargetAddress", notificationTargetUrl);

        // For POST only - START
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END
        int responseCode = con.getResponseCode();
        String responseMessage = con.getResponseMessage();
        System.out.println("\n");

        return responseCode;
    }


    /**
     * @param subscriptionID
     * @param nnwdafEventsSubscription
     * @return
     * @desc this function will update network function subscription
     */
    @PutMapping(PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> update_nf_subscription(@PathVariable("subscriptionID") String subscriptionID, @RequestBody NnwdafEventsSubscription nnwdafEventsSubscription) {

        logger.debug("Entered UpdateNF() ");
        logger.info(" eventID  " + nnwdafEventsSubscription.getEventID() + " snssais  " + nnwdafEventsSubscription.getSnssais() +
                " notifMethod  " + nnwdafEventsSubscription.getNotifMethod() + " repetitionPeriod  " + nnwdafEventsSubscription.getRepetitionPeriod() +
                " loadLevelThreshold " + nnwdafEventsSubscription.getLoadLevelThreshold());

        NwdafSubscriptionTableModel nwdafSubscriptionTableModel = repository.findById_subscriptionID(subscriptionID);
        if (nnwdafEventsSubscription == null) {
            logger.warn("no Content");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
        }

        // Updating user via subscriptionID
        repository.updateNF(nnwdafEventsSubscription, subscriptionID);

        // Incrementing update counter
        Counters.incrementSubscriptionUpdates();
        showCounters();

        logger.debug("Exit UpdateNF()");
        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.OK);
    }

    /**
     * @param subscriptionID
     * @return
     * @desc this function unsubscribe network function
     */
    @DeleteMapping(value = PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> unsubscription_nf(@PathVariable("subscriptionID") String subscriptionID) {

        logger.debug("Entered UnsubscribeNf()");
        NwdafSubscriptionTableModel sub = repository.findById_subscriptionID(subscriptionID);

        if (sub == null) {
            logger.warn("Data not found");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND);
        }

        repository.unsubscribeNF(subscriptionID);

        Counters.incrementUnSubscriptions();
        showCounters();

        logger.debug("Exit unsubscribe NF");
        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
    }


    @GetMapping(PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> get_all_network_function(@PathVariable("subscriptionID") String id) {

        logger.debug("Entered getNF()");
        NwdafSubscriptionTableModel user = repository.findById_subscriptionID(id);

        if (user == null) {
            logger.warn("Data not found");
            return new ResponseEntity<NwdafSubscriptionTableModel>(HttpStatus.NOT_FOUND);
        }
        logger.debug("Exit getNF()");
        return new ResponseEntity<NwdafSubscriptionTableModel>(user, HttpStatus.OK);
    }


    // Accepting Notification [ from Simulator]
    @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_Notify/{correlationID}")
    public void acceptingNotification(@RequestBody String response) throws JSONException, IOException {

        // Here Data will be received and nwdafSliceLoadLevelInformation Table will get populated.
        logger.debug("Entered Accepting Notification");

        // here subID == correlationID

        JSONObject json = new JSONObject(response);

        // String correlationID = json.getString("correlationID");
        int currentLoadLevel = json.getInt("currentLoadLevel");
        String correlationID = json.getString("correlationID");

        //out.println("received from SIMULATOR - " + "currentLoadLevel - " + currentLoadLevel + correlationID);

        String snssais = repository.getSnssaisByCorrelationID(correlationID);

        if (repository.isGetAnalytics(snssais)) {
            repository.decrementRefCount(snssais);
            repository.setGetAnalytics(snssais, false);
        }

        repository.updateCurrentLoadLevel(snssais);
        nwdaf_notification_manager();

        //repository.getSnssaisViaSubID(correlationID);

        // incrementing notification Counter
        Counters.incrementCollectorSubscriptionNotifications();
        showCounters();
    }

    @RequestMapping("/Namf_EventExposure_UnSubscribe/{unSubCorrelationId}")
    public String unSubscribeEvent(@PathVariable UUID unSubCorrelationId) {

        logger.debug("Enter unSubscribeEvent()");
        logger.debug("Exit unSubscribeEvent()");
        return " Event Deleted";
    }


  /*  public class NwdafNotificationThread extends Thread {

        public void run() {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    // Calling notification Manager Function
                    nwdaf_notification_manager();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    /**
     * @throws IOException
     * @desc this function manages notifications for subscribers
     */
    private void nwdaf_notification_manager() throws IOException {

        //logger.debug("Entered callNotificationManagerFunction()");

        // Fetching all snssais list
        List<NwdafSliceLoadLevelInformationModel> list = repository.getALLsnssais();

        if (repository.getALLsnssais() == null) {
            logger.warn("no snssais Found [ Null object ]");
        }


        // Iterating throw list and getting all subscriptionID related to that snssais
        // i = indexOfsnssaisList || j = indexOfSubscriptionList

        for (int indexOfSnssaisList = 0; indexOfSnssaisList < list.size(); indexOfSnssaisList++) {

            // checking current Threshold value
            currentLoadLevel = repository.currentLoadLevel(list.get(indexOfSnssaisList).getSnssais());

            if (repository.getALLsnssais() == null) {
                logger.warn("Null object!");
            }
            //    if (currentLoadLevel == 0)
            //      continue;

            // Fetching all subscription ID of a particular snssais
            List<NwdafSliceLoadLevelSubscriptionDataModel> subscriptionList = fetch_all_subscitpionID(list.get(indexOfSnssaisList).getSnssais());

            if (subscriptionList == null) {
                logger.warn("No subscription List found");
            }

            // Iterating throw subscriptionIDs to check whose TH value has been reached
            for (int indexOfSubscriptionList = 0; indexOfSubscriptionList < subscriptionList.size(); indexOfSubscriptionList++) {

                // Checking currentLoadLevel Of that snsssais if  current Load level greater than required TH Value
                // then send notification
                subThValue = repository.getLoadLevelThreshold(String.valueOf(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID()));

                if (currentLoadLevel > subThValue) {

                        /* Checking Hash SET if notification has been sent on that particular subscriptionID/
                       Then we do not have to send the notification again.
                        */

                    if (!(subID_SET.contains(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID()))) {
                        subID_SET.add(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID());

                        if (subscriptionList.get(indexOfSubscriptionList).getSubscriptionID() == null) {
                            logger.warn("Null Object Found! [ No subscription ID is present ]");
                        } else {

                            // Fetching notification URL on which NWDAF will send notification
                            String NotificationURI = repository.getNotificationURI(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID());

                            if (repository.getNotificationURI(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID()) == null) {
                                logger.warn("No subscription ID is present -> can't get Notification URI ");
                            }

                            // Calling sendNotification Function
                            send_notificaiton_to_NF(NotificationURI);

                        }

                    }

                }

            }

        }

    }


    /**
     * @param snssais
     * @return
     * @desc this function will fetch all subscription IDs of a particular snssais
     */
    //Fetching all subscriptionID of particular snssais
    private List<NwdafSliceLoadLevelSubscriptionDataModel> fetch_all_subscitpionID(String snssais) {

        List<NwdafSliceLoadLevelSubscriptionDataModel> subscriptionIDList = repository.getAllSubIdsbysnssais(snssais);

        if (subscriptionIDList == null) {
            logger.warn("No subscription List present");
        }

        logger.debug("Exit getALLsubscirptionIDs");
        return subscriptionIDList;
    }

    /**
     * @param notificationURI
     * @throws IOException
     * @desc this function will send notification to network function
     */
    private void send_notificaiton_to_NF(String notificationURI) throws IOException {
        logger.debug("Entered sendNotificationToNF");

        Counters.incrementSubscriptionNotifications();
        showCounters();

        URL url = null;
        try {
            url = new URL(notificationURI);
        } catch (MalformedURLException e) {
            logger.warn("http connect exception found");
            e.printStackTrace();
        }

        // Opening connection;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = "Hey I am Notification";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");

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
        }
    }


    /**
     * @desc this function holds details of counters
     */
    public void showCounters() {
        logger.debug("Enter showCounters");

        logger.info("> # Event_Subscriptions: " + Counters.getSubscriptions());
        logger.info("> # Event_UnSubscriptions: " + Counters.getUnSubscriptions());
        logger.info("> # Event_SubscriptionUpdates: " + Counters.getSubscriptionUpdates());
        logger.info("> # Event_SubscriptionNotifications: " + Counters.getSubscriptionNotifications());

        logger.info("> # Collector_Subscriptions: " + Counters.getCollectorSubscriptions());
        logger.info("> # Collector_SubscriptionNotifications: " + Counters.getCollectorSubscriptionNotifications());
        logger.info("> # Collector_AnalyticsSubscriptions: " + Counters.getAnalyticsSubscriptions());
        logger.info("> # Collector_AnalyticsNotifications: " + Counters.getAnalyticsNotifications());

        logger.debug("Exit sendNotificationToNF");
    }


    /**
     * this function shows counters
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/nwdaf/counter")
    public HashMap<String, BigInteger> nwdaf_counters() throws Exception {

        logger.debug("Entered getStats");

        HashMap<String, BigInteger> map = new HashMap<String, BigInteger>();

        map.put("Event_Subscriptions", Counters.getSubscriptions());
        map.put("Event_UnSubscriptions", Counters.getUnSubscriptions());
        map.put("Event_SubscriptionUpdates", Counters.getSubscriptionUpdates());
        map.put("Event_SubscriptionNotifications", Counters.getSubscriptionNotifications());

        map.put("Collector_Subscriptions", Counters.getCollectorSubscriptions());
        map.put("Collector_SubscriptionNotifications", Counters.getCollectorSubscriptionNotifications());
        map.put("Collector_AnalyticsSubscriptions", Counters.getAnalyticsSubscriptions());
        map.put("Collector_AnalyticsNotifications", Counters.getAnalyticsNotifications());

        logger.debug("Exit getStats");
        return map;
    }

    /**
     * @return String
     * @desc this function will reset all the counters
     */
    @PostMapping("/nwdaf/counter")
    public String nwdaf_reset_counter() {

        logger.debug("Entere resetCounters");

        Counters.reset();

        logger.debug("Exit resetCounters");
        return "Counters set to 0.";
    }





    @RequestMapping(method = RequestMethod.GET,value = "/apiInfo")
    public Object check_api_details() throws IOException {

        APIBuildInformation apiBuildInformation = new APIBuildInformation();

        try (InputStream input = new FileInputStream("/Users/sheetalkumar/Desktop/Demo3W/nwdaf/EventSubscription/buildNumber.properties")) {

            Properties prop = new Properties();
            prop.load(input);


            apiBuildInformation.setAPI_VERSION(buildProperties.getVersion() + "." + prop.getProperty("buildNumber"));
            apiBuildInformation.setAPI_NAME(buildProperties.getArtifact());
            apiBuildInformation.setAPI_TIME(String.valueOf(buildProperties.getTime()));
            apiBuildInformation.setGROUP_NAME(buildProperties.getGroup());


            return apiBuildInformation;

        }
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

        InputStream input = new FileInputStream("/Users/sheetalkumar/Desktop/Demo3W/nwdaf/EventSubscription/buildNumber.properties");

        Properties prop = new Properties();
        prop.load(input);

        return new ApiInfo(
                "NWDAF API ( Network Data Analytics Function )",
                " Sample API for NWDAF in 5G",
                buildProperties.getVersion() + "." + prop.getProperty("buildNumber"),
                "Free to use",
                new springfox.documentation.service.Contact("Team NWDAF", "https://truminds.com/home", "sheetal.kumar@truminds.co.in"),
                "API License",
                "https://truminds.com/home",
                Collections.emptyList());
    }

}

