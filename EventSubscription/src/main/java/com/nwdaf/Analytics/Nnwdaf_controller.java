package com.nwdaf.Analytics;

import com.nwdaf.Analytics.MetaData.Counters;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelInformationModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelSubscriptionDataModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSubscriptionTableModel;
import com.nwdaf.Analytics.model.APIBuildInformation;
import com.nwdaf.Analytics.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import org.springframework.http.HttpHeaders;

import static java.lang.System.out;
import static org.springframework.http.HttpHeaders.USER_AGENT;


@RestController
public class Nnwdaf_controller {

    public static Logger logger = Logger.getLogger(Nnwdaf_controller.class.getName());

    int refcountForsnssais = 0;

    String getAnalytics = "False";

    Set<String> subID_SET = new HashSet<String>();

    String updated_POST_AMF_URL = null;


    public int subThValue = 0;
    public int currentLoadLevel = 0;

    List<UUID> subIDs;


    public List<UUID> getSubIDs() {
        return subIDs;
    }

    public void setSubIDs(List<UUID> subIDs) {
        this.subIDs = subIDs;
    }

    public Nnwdaf_controller() {

        logger.debug("Entered Nnwdaf_controller()");
        logger.info("Thread started");

        // Object of NwdafNotification Thread
        //    NwdafNotificationThread nwdafNotificationThread = new NwdafNotificationThread();

        // Starting thread
        //  nwdafNotificationThread.start();

        logger.debug("Exit Nnwdaf_controller()");


    }


    public Nnwdaf_controller(List<UUID> subIDs) {
        this.subIDs = subIDs;
    }

    final String PATH = "/nnwdaf-eventssubscription/v1";

    @Value("${spring.MAIN.url}")
    String URI;

    @Value("${spring.NotificationTarget.Url}")
    String notificationTargetUrl;

    @Autowired
    Nnwdaf_repository repository;


    @Autowired
    BuildProperties buildProperties;


    String unSubCorrelationID;


    @Value("${spring.AMF_EVENT_EXPOSURE_SUBSCRIBE.url}")
    String POST_AMF_URL;


    // Get Request For Analytics Information
    @GetMapping("apiroot/nnwdaf-analyticsinfo/v1/{snssais}/{anySlice}/{eventID}")
    public Object getALLAnalyticsInformation(@PathVariable("snssais") String snssais, @PathVariable("anySlice") Boolean anySlice, @PathVariable("eventID") int eventID) throws IOException, JSONException {

        getAnalytics = "True";

        logger.debug("Entered getAllAnalyticsInformation");

        // Fetching all data from database
        List<EventConnection> connect = repository.getData(snssais, anySlice);

        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
        nnwdafEventsSubscription.setSnssais(snssais);

        // if data is not found -> calling collector function to collect data
        if (connect.isEmpty()) {

            logger.warn("Data not found ");

            // Calling collector function
            UUID correlationID = collectorFunction(nnwdafEventsSubscription, getAnalytics);

            // Adding snssais into database
            repository.addDataIntoLoadLevelInformaitionTable(snssais);

            EventConnection eventConnection = new EventConnection();

            eventConnection.setMessage("Data not Found for " + snssais);
            eventConnection.setCurrentLoadLevelInfo(0);
            eventConnection.setSnssais(snssais);
            eventConnection.setDataStatus(false);

            //throw new NullPointerException("Data not found!");
            return eventConnection;

        } else {

            logger.debug("Exit getALLAnalyticsInformation");

            UUID correlationID = collectorFunction(nnwdafEventsSubscription, getAnalytics);
            // if Data found returning JSON
            return connect;
        }

    }

    @PostMapping(PATH + "/subscriptions")
    public ResponseEntity<String> subscribeNF(@RequestBody NnwdafEventsSubscription nnwdafEventsSubscription) throws SQLIntegrityConstraintViolationException, URISyntaxException, IOException, JSONException {

        logger.debug("NF Subscription | Entered subscribeNF()");

        logger.info(" eventID  " + nnwdafEventsSubscription.getEventID() +
                " notificationURI  " + nnwdafEventsSubscription.getNotificationURI() +
                " snssais  " + nnwdafEventsSubscription.getSnssais() +
                " notifMethod  " + nnwdafEventsSubscription.getNotifMethod() +
                " repetitionPeriod  " + nnwdafEventsSubscription.getRepetitionPeriod() +
                " loadLevelThreshold " + nnwdafEventsSubscription.getLoadLevelThreshold());

        // Random generating SubscriptionID
        UUID subID = UUID.randomUUID();


        // setting subscription ID
        nnwdafEventsSubscription.setSubscriptionID(String.valueOf(subID));

        // Adding data into NwdafSubscriptionTable   [ Database ]
        repository.subscribeNF(nnwdafEventsSubscription);


        // Incrementing Subscription Counter
        Counters.incrementSubscriptions();
        showCounters();

        // Object of NwdafSliceLoadLevelSubscirptionDataModel Class
        NwdafSliceLoadLevelSubscriptionDataModel nwdafSliceLoadLevelSubscriptionDataModel = new
                NwdafSliceLoadLevelSubscriptionDataModel();

        // Setting values in NwdafSliceLoadLevelSubscriptionData object
        nwdafSliceLoadLevelSubscriptionDataModel.setSubscriptionID(String.valueOf(subID));
        nwdafSliceLoadLevelSubscriptionDataModel.setSnssais(nnwdafEventsSubscription.getSnssais());
        nwdafSliceLoadLevelSubscriptionDataModel.setLoadLevelThreshold(nnwdafEventsSubscription.getLoadLevelThreshold());


        // Adding nwdafSliceLoadLevelSubscriptionDatamodel into NwdafSliceLoadLevelSubscriptionData Table [ Database ]
        repository.addDataIntoNwdafSliceLoadLevelSubscriptionDataTable(nwdafSliceLoadLevelSubscriptionDataModel);


        // Storing data into loadlevelInformation Table
        repository.addDataIntoLoadLevelInformaitionTable(nnwdafEventsSubscription.getSnssais());


        // Storing values into subscription Table
        // repository.subscribeNF(nnwdafEventsSubscription);


        // JSON payload send by NF to NWDAF
        logger.info("\n" + "SubID : " + nnwdafEventsSubscription.getSubscriptionID() +
                " NotificationURI : " + nnwdafEventsSubscription.getEventID() +
                " snssais : " + nnwdafEventsSubscription.getSnssais() +
                " anySlice : " + nnwdafEventsSubscription.getAnySlice() +
                " notifMethod : " + nnwdafEventsSubscription.getNotifMethod() +
                " repetition Method : " + nnwdafEventsSubscription.getRepetitionPeriod() +
                " LoadLevelThreshold : " + nnwdafEventsSubscription.getLoadLevelThreshold());


        getAnalytics = "false";
        //calling Collector Function
        UUID correlationID = collectorFunction(nnwdafEventsSubscription, getAnalytics);


        // Returning Response header [ subscription ID ] for NF to unsubscribe
        URI location = new URI(URI + "subscriptions/" + String.valueOf(subID));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);


        logger.debug("Exit subscribe NF");
        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);
    }


    // In Collector Function
    private UUID collectorFunction(NnwdafEventsSubscription nnwdafEventsSubscription, String getAnalytics) throws IOException, JSONException {

        logger.debug("Entered Collector Function");

        // Generating CorrelationID
        UUID correlationID = UUID.randomUUID();

        Namf_EventExposure_Subscribe namf_eventExposure_subscribe = new Namf_EventExposure_Subscribe();

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

        JSONObject json = new JSONObject();

        json.put("correlationID", correlationID);
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
                repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(correlationID,
                        response.toString(), nnwdafEventsSubscription.getSnssais(), getAnalytics);
            } catch (Exception e) {
                out.println(e.getMessage());
            }
            in.close();


            // Save correlationID and unSubCorrelationID into nwdafIDTable /[ here is have to update unSubCorrelationID]

           /*
            try {
                repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(correlationID,
                        response.toString(), nnwdafEventsSubscription.getSnssais());
            } catch (Exception e) {
                out.println(e.getMessage());
            }*/
        } else {
            logger.error(" POST request not worked");
        }

        logger.debug("Exit Collector Function");

        return correlationID;
    }


    @PutMapping(PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> updateNF(@PathVariable("subscriptionID") String id, @RequestBody NnwdafEventsSubscription nnwdafEventsSubscription) {

        logger.debug("Entered UpdateNF() ");

        logger.info(" eventID  " + nnwdafEventsSubscription.getEventID() +
                " snssais  " + nnwdafEventsSubscription.getSnssais() +
                " notifMethod  " + nnwdafEventsSubscription.getNotifMethod() +
                " repetitionPeriod  " + nnwdafEventsSubscription.getRepetitionPeriod() +
                " loadLevelThreshold " + nnwdafEventsSubscription.getLoadLevelThreshold());

        NwdafSubscriptionTableModel nwdafSubscriptionTableModel = repository.findById_subscriptionID(id);


        if (nnwdafEventsSubscription == null) {
            logger.warn("no Content");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
        }

        // Updating user via subscriptionID
        repository.updateNF(nnwdafEventsSubscription, id);


        // Incrementing update counter
        Counters.incrementSubscriptionUpdates();
        showCounters();


        logger.debug("Exit UpdateNF()");
        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.OK);
    }


    @DeleteMapping(value = PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> unsubscribeNF(@PathVariable("subscriptionID") String subID) {

        logger.debug("Entered UnsubscribeNf()");
        NwdafSubscriptionTableModel sub = repository.findById_subscriptionID(subID);

        if (sub == null) {
            logger.warn("Data not found");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND);
        }

        repository.unsubscribeNF(subID);

        Counters.incrementUnSubscriptions();
        showCounters();

        logger.debug("Exit unsubscribe NF");
        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> getNF(@PathVariable("subscriptionID") String id) {

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
    public void acceptingNotification(@RequestBody String response) throws JSONException {

        // Here Data will be received and nwdafSliceLoadLevelInformation Table will get populated.
        logger.debug("Entered Accepting Notification");


        // here subID == correlationID

        JSONObject json = new JSONObject(response);

        // String correlationID = json.getString("correlationID");
        int currentLoadLevel = json.getInt("currentLoadLevel");
        String correlationID = json.getString("correlationID");

        out.println("received from SIMULATOR - " + "currentLoadLevel - " + currentLoadLevel + correlationID);

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


    public class NwdafNotificationThread extends Thread {

        public void run() {


            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {

                    // Calling notification Manager Function
                    callNotificationManagerFunction();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        private void callNotificationManagerFunction() throws IOException {

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
                List<NwdafSliceLoadLevelSubscriptionDataModel> subscriptionList = getAllSubscriptionIds(list.get(indexOfSnssaisList).getSnssais());

                if (subscriptionList == null) {
                    logger.warn("No subscription List found");
                }

                // Iterating throw subscriptionIDs to check whose TH value has been reached
                for (int indexOfSubscriptionList = 0; indexOfSubscriptionList < subscriptionList.size(); indexOfSubscriptionList++) {

                    // Checking currentLoadLevel Of that snsssais if  current Load level greater than required TH Value
                    // then send notification
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
                                sendNotificationToNF(NotificationURI);

                            }

                        }

                    }

                }

            }

        }

        //Fetching all subscriptionID of particular snssais
        private List<NwdafSliceLoadLevelSubscriptionDataModel> getAllSubscriptionIds(String snssais) {
            logger.debug("Enter getALLSubscriptionIDs");

            List<NwdafSliceLoadLevelSubscriptionDataModel> subscriptionIDList = repository.getAllSubIdsbysnssais(snssais);

            if (subscriptionIDList == null) {
                logger.warn("No subscription List present");
            }

            logger.debug("Exit getALLsubscirptionIDs");
            return subscriptionIDList;
        }

        private void sendNotificationToNF(String notificationURI) throws IOException {
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

            // JSON  String sending to M1
            // String jsonInputString = "{\"id\":10000,\"name\":\" [ M2 -> M1 ]\"}";

            String jsonInputString = "Hey I am Notification";

            //    System.out.println("\n\nSending Notification From  NWDAF TO NF  - " + jsonInputString);

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

    }


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


    // Mapping to get all the counters
    @GetMapping("/stats/get")
    public HashMap<String, BigInteger> getStats() throws Exception {

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


    // Mapping to reset all the counters
    @PostMapping("/stats/clear")
    public String resetCounters() {

        logger.debug("Entere resetCounters");

        Counters.reset();

        logger.debug("Exit resetCounters");
        return "Counters set to 0.";
    }


    @RequestMapping("/apiDetails")
    public Object getNwdafAPIInformation() throws IOException {


        //  JarFile jf = new JarFile("/Users/sheetalkumar/Desktop/Demo3W/nwdaf/Simulator/AMF/target/Collector-0.0.1-SNAPSHOT.jar");
        //  ZipEntry manifest = jf.getEntry("META-INF/MANIFEST.MF");
        //  long manifestTime = manifest.getTime();
        //  Timestamp ts = new Timestamp(manifestTime);
        //  Date date = new Date(ts.getTime());
        //  return " Date - " + date;


        APIBuildInformation apiBuildInformation = new APIBuildInformation();

        LocalDate date = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault()).toLocalDate();
        LocalTime time = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault()).toLocalTime();


        apiBuildInformation.setAPI_VERSION(buildProperties.getVersion());
        apiBuildInformation.setAPI_NAME(buildProperties.getName());
        apiBuildInformation.setBUILD_DATE(date);
        apiBuildInformation.setBUILD_TIME(time);

        return apiBuildInformation;

    }

}
