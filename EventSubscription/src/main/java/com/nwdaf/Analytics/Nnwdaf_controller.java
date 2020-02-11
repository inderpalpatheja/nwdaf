package com.nwdaf.Analytics;

import com.nwdaf.Analytics.MetaData.Counters;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelInformationModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelSubscriptionDataModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSliceLoadLevelSubscriptionTableModel;
import com.nwdaf.Analytics.NwdafModel.NwdafSubscriptionTableModel;
import com.nwdaf.Analytics.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import org.springframework.http.HttpHeaders;
import sun.lwawt.macosx.CSystemTray;

import static java.lang.System.out;
import static org.springframework.http.HttpHeaders.USER_AGENT;


@RestController
public class Nnwdaf_controller {

    public static Logger logger = Logger.getLogger(Nnwdaf_controller.class.getName());


    Set<String> subID_SET = new HashSet<String>();

    String updated_POST_AMF_URL = null;

    private Log log = LogFactory.getLog(Nnwdaf_controller.class);

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
        TestThread testThread = new TestThread();
        testThread.start();
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

    String unSubCorrelationID;


    @Value("${spring.AMF_EVENT_EXPOSURE_SUBSCRIBE.url}")
    String POST_AMF_URL;


    // Get Request For Analytics Information
    @GetMapping("apiroot/nnwdaf-analyticsinfo/v1/{snssais}/{anySlice}/{eventID}")
    public List<events_connection> getALLAnalyticsInformation(@PathVariable("snssais") String snssais, @PathVariable("anySlice") Boolean anySlice, @PathVariable("eventID") int eventID) throws IOException, JSONException {


        // Fetching all data from database
        List<events_connection> connect = repository.getData(snssais, anySlice);

        NnwdafEventsSubscription nnwdafEventsSubscription = new NnwdafEventsSubscription();
        nnwdafEventsSubscription.setSnssais(snssais);

        // if data is not found -> calling collector function to collect data
        if (connect.isEmpty()) {

            logger.warn("Data not found ");

            // Calling collector function
            UUID correlationID = collectorFunction(nnwdafEventsSubscription);

            // Adding snssais into database
            repository.addDataIntoLoadLevelInformaitionTable(snssais);

            throw new NullPointerException("Data not found!");

        } else {

            // if Data found returning JSON
            return connect;
        }

    }

    @PostMapping(PATH + "/subscriptions")
    public ResponseEntity<String> subscribeNF(@RequestBody NnwdafEventsSubscription nnwdafEventsSubscription) throws SQLIntegrityConstraintViolationException, URISyntaxException, IOException, JSONException {


        // Random generating SubscriptionID
        UUID subID = UUID.randomUUID();

        // setting subscription ID
        nnwdafEventsSubscription.setSubscriptionID(String.valueOf(subID));

        // Adding data into NwdafSubscriptionTable   [ Database ]
        repository.subscribeNF(nnwdafEventsSubscription);


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
        out.println("\n" + "SubID : " + nnwdafEventsSubscription.getSubscriptionID() +
                " NotificationURI : " + nnwdafEventsSubscription.getEventID() +
                " snssais : " + nnwdafEventsSubscription.getSnssais() +
                " anySlice : " + nnwdafEventsSubscription.getAnySlice() +
                " notifMethod : " + nnwdafEventsSubscription.getNotifMethod() +
                " repetition Method : " + nnwdafEventsSubscription.getRepetitionPeriod() +
                " LoadLevelThreshold : " + nnwdafEventsSubscription.getLoadLevelThreshold());


        //calling Collector Function
        UUID correlationID = collectorFunction(nnwdafEventsSubscription);


        // Returning Response header [ subscription ID ] for NF to unsubscribe
        URI location = new URI(URI + "subscriptions/" + String.valueOf(subID));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);


        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);
    }


    // In Collector Function
    private UUID collectorFunction(NnwdafEventsSubscription nnwdafEventsSubscription) throws IOException, JSONException {


        // Generating CorrelationID
        UUID correlationID = UUID.randomUUID();


        Namf_EventExposure_Subscribe namf_eventExposure_subscribe = new Namf_EventExposure_Subscribe();


        // POST_AMF_URL = POST_AMF_URL + subId;
        //POST_AMF_URL = http::/localhost:8082/Namf_EventExposure_Subscribe/subID


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

            //   System.out.println("\n");

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

            Counters.incrementCollectorSubscriptions();
            showCounters();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            // Save correlationID and unSubCorrelationID into nwdafIDTable /[ here is have to update unSubCorrelationID]

            try {
                repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(correlationID,
                        response.toString(), nnwdafEventsSubscription.getSnssais());
            } catch (Exception e) {
                out.println(e.getMessage());
            }


        } else {
            logger.warn(" POST request not worked");
        }

        return correlationID;

    }


    @PutMapping(PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> updateNF(@PathVariable("subscriptionID") String id, @RequestBody NnwdafEventsSubscription user) {

        NwdafSubscriptionTableModel check_user = repository.findById_subscriptionID(id);


        if (check_user == null) {
            logger.warn("no Content");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
        }

        repository.updateNF(user, id);

        ///////////*******************//////////////

        Counters.incrementSubscriptionUpdates();
        showCounters();

        ///////////*******************//////////////

        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.OK);
    }


    @DeleteMapping(value = PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> unsubscribeNF(@PathVariable("subscriptionID") String subID) {

        NwdafSubscriptionTableModel sub = repository.findById_subscriptionID(subID);

        if (sub == null) {
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND);
        }

        repository.unsubscribeNF(subID);

        Counters.incrementUnSubscriptions();
        showCounters();

        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> getNF(@PathVariable("subscriptionID") String id) {

        NwdafSubscriptionTableModel user = repository.findById_subscriptionID(id);

        if (user == null) {
            return new ResponseEntity<NwdafSubscriptionTableModel>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<NwdafSubscriptionTableModel>(user, HttpStatus.OK);
    }


    // Accepting Notification
    @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_Notify/{correlationID}")
    public void acceptingNotification(@PathVariable String correlationID) {


        Counters.incrementCollectorSubscriptionNotifications();
        showCounters();
    }


    @RequestMapping("/Namf_EventExposure_UnSubscribe/{unSubCorrelationId}")
    public String unSubscribeEvent(@PathVariable UUID unSubCorrelationId) {

        return " Event Deleted";
    }


    public List<UUID> getAllSubIds(List<NnwdafEventsSubscription> nnwdafEventsSubscription) {

        //  logger.info("fetching all subIDs");

        List<UUID> allSubIds = new ArrayList<>();


        for (int i = 0; i < nnwdafEventsSubscription.size(); i++) {

            allSubIds.add(nnwdafEventsSubscription.get(i).subscriptionID);
        }

        return allSubIds;

    }


    public class TestThread extends Thread {

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

            // Fetching all snssais list
            List<NwdafSliceLoadLevelInformationModel> list = repository.getALLsnssais();


            // Iterating throw list and getting all subscriptionID related to that snssais
            for (int i = 0; i < list.size(); i++) {

                // checking current Threshold value
                currentLoadLevel = repository.currentLoadLevel(list.get(i).getSnssais());

                //    if (currentLoadLevel == 0)
                //      continue;

                // Fetching all subscription ID of a particular snssais
                List<NwdafSliceLoadLevelSubscriptionDataModel> subscriptionList = getAllSubscriptionIds(list.get(i).getSnssais());


                // Iterating throw subscriptionIDs to check whose TH value has been reached
                for (int j = 0; j < subscriptionList.size(); j++) {

                    // Checking currentLoadLevel Of that snsssais is greater than required TH Value
                    if (currentLoadLevel > subThValue) {

                        /* Checking SET if notification has been sent to that particular subscriptionID
                        if Notification has been sent to that subID we do not have to send the notification again.
                        */

                        if (!(subID_SET.contains(subscriptionList.get(j).getSubscriptionID()))) {
                            subID_SET.add(subscriptionList.get(j).getSubscriptionID());

                            if (subscriptionList.get(j).getSubscriptionID() == null) {
                                logger.warn("Null Object Found!");
                            } else {
                                // out.println("subID ------> " + subscriptionList.get(j).getSubscriptionID());

                                // Fetching notification URL on which NWDAF will send notification
                                String NotificationURI = repository.getNotificationURI(subscriptionList.get(j).getSubscriptionID());

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

            List<NwdafSliceLoadLevelSubscriptionDataModel> subscriptionIDList = repository.getAllSubIdsbysnssais(snssais);

            return subscriptionIDList;
        }

        private void sendNotificationToNF(String notificationURI) throws IOException {


            ///////////*******************//////////////

            Counters.incrementSubscriptionNotifications();
            showCounters();

            ///////////*******************//////////////

            URL url = null;
            try {
                url = new URL(notificationURI);
            } catch (MalformedURLException e) {
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
        log.info("> # Event_Subscriptions: " + Counters.getSubscriptions());
        log.info("> # Event_UnSubscriptions: " + Counters.getUnSubscriptions());
        log.info("> # Event_SubscriptionUpdates: " + Counters.getSubscriptionUpdates());
        log.info("> # Event_SubscriptionNotifications: " + Counters.getSubscriptionNotifications());

        log.info("> # Collector_Subscriptions: " + Counters.getCollectorSubscriptions());
        log.info("> # Collector_SubscriptionNotifications: " + Counters.getCollectorSubscriptionNotifications());
        log.info("> # Collector_AnalyticsSubscriptions: " + Counters.getAnalyticsSubscriptions());
        log.info("> # Collector_AnalyticsNotifications: " + Counters.getAnalyticsNotifications());
    }


    // Mapping to get all the counters
    @GetMapping("/stats/get")
    public HashMap<String, BigInteger> getStats() throws Exception {

        HashMap<String, BigInteger> map = new HashMap<String, BigInteger>();

        map.put("Event_Subscriptions", Counters.getSubscriptions());
        map.put("Event_UnSubscriptions", Counters.getUnSubscriptions());
        map.put("Event_SubscriptionUpdates", Counters.getSubscriptionUpdates());
        map.put("Event_SubscriptionNotifications", Counters.getSubscriptionNotifications());

        map.put("Collector_Subscriptions", Counters.getCollectorSubscriptions());
        map.put("Collector_SubscriptionNotifications", Counters.getCollectorSubscriptionNotifications());
        map.put("Collector_AnalyticsSubscriptions", Counters.getAnalyticsSubscriptions());
        map.put("Collector_AnalyticsNotifications", Counters.getAnalyticsNotifications());

        return map;
    }


    // Mapping to reset all the counters
    @PostMapping("/stats/clear")
    public String resetCounters() {
        Counters.reset();
        return "Counters set to 0.";
    }
}
