package com.nwdaf.Analytics;


import com.nwdaf.Analytics.Repository.CollectorRepository;
import com.nwdaf.Analytics.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import com.nwdaf.Analytics.model.analytics;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;

import static java.lang.System.out;
import static org.springframework.http.HttpHeaders.USER_AGENT;


@RestController
public class Nnwdaf_controller {

    public static Logger logger = Logger.getLogger(Nnwdaf_controller.class.getName());


    private int subscriptionCounter = 0;
    private int subscriptionResponse = 0;

    private Log log = LogFactory.getLog(Nnwdaf_controller.class);

    public int t_value = 0;

    List<UUID> subIDs;

    public List<UUID> getSubIDs() {
        return subIDs;
    }

    public void setSubIDs(List<UUID> subIDs) {
        this.subIDs = subIDs;
    }

    public Nnwdaf_controller() {
        logger.info("In Nnwdaf_controller class!");
        // logger.error("error log");
        // logger.fatal("fatal");

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


    @Value("${spring.AMF_EVENT_EXPOSURE_SUBSCRIBE.url}")
    String POST_AMF_URL;

    @Autowired
    CollectorRepository collectorRepository;

    @GetMapping(PATH + "/test")
    public String testing() {


        return "Working Properly!" + POST_AMF_URL;
    }







    


    @RequestMapping("/log")
    public String log() {
        // log.trace("This is a TRACE level message");
        //log.debug("This is a DEBUG level message");
        //log.info("This is an INFO level message");
        //log.warn("This is a WARN level message");
        //log.error("This is an ERROR level message");

        //  Logger logger = LogManager.getLogger("CONSOLE_JSON_APPENDER");
        // logger.debug("Debug message");

        return "See the log for details";
    }


    @GetMapping(PATH + "/allNFs")
    public List<NnwdafEventsSubscription> getAllNFs() {
        return repository.getAllNFs();
    }


    @PostMapping(PATH + "/subscriptions")
    public ResponseEntity<String> subscribeNF(@RequestBody NnwdafEventsSubscription nnwdafEventsSubscription) throws SQLIntegrityConstraintViolationException, URISyntaxException, IOException, JSONException {

        logger.info("new Subscriber!");
        logger.debug(nnwdafEventsSubscription.getEventID() + nnwdafEventsSubscription.getNotificationURI());

        UUID subID = UUID.randomUUID();
        logger.info("Subscription ID is generated - " + subID);

        // adding data to nwdafSubscriptionTable;

        repository.subscribeNF(nnwdafEventsSubscription, subID);

        logger.info("Subscriber data save into nwdafSubscriptionTable [ subscriptionID - " + subID
                + "eventID - " + nnwdafEventsSubscription.getEventID()
                + " notificationURI - " + nnwdafEventsSubscription.getNotificationURI()
                + "notifMethod - " + nnwdafEventsSubscription.getNotifMethod()
                + " repetitionPeriod - " + nnwdafEventsSubscription.getRepetitionPeriod()
                + "loadLevelThreshold - " + nnwdafEventsSubscription.getLoadLevelThreshold());


        // increasing subscriptionValue
        //subscriptionCounter = subscriptionCounter + 1;


        //adding subscription ID to nwdafLoadLevelInformation Table
        repository.addSubscriptionIdToLoadLevelInfo(nnwdafEventsSubscription, subID);

        logger.info("Generated subscriptionID save into nwdafLoadLevelInfo");

        // adding subsctiptionCounter to nwdafCounterTable;
        // repository.getSubscriptionCount();


        URI location = new URI(URI + "subscriptions/" + String.valueOf(subID));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);


        // Calling Collector Function
        logger.debug(nnwdafEventsSubscription);

        collectorFuntion(nnwdafEventsSubscription, subID);

        logger.info("Collector Function called! ");


        return new ResponseEntity<String>("Created", responseHeaders, HttpStatus.CREATED);
    }


    private String collectorFuntion(NnwdafEventsSubscription nnwdafEventsSubscription, UUID subId) throws IOException, JSONException {
        logger.info("In Collector Function");

        logger.info("EventID - " + nnwdafEventsSubscription.getEventID());
        Namf_EventExposure_Subscribe namf_eventExposure_subscribe = new Namf_EventExposure_Subscribe();

        // POST_AMF_URL = POST_AMF_URL + subId;

        //POST_AMF_URL = http::/localhost:8082/Namf_EventExposure_Subscribe/subID

        String updated_POST_AMF_URL = POST_AMF_URL + "/" + subId;
        System.out.println(updated_POST_AMF_URL);

        //logger.info(POST_AMF_URL + "/" +subId);

        URL obj = new URL(updated_POST_AMF_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        JSONObject json = new JSONObject();

        UUID correationId = UUID.randomUUID();

        Namf_EventExposure_Subscribe simulationDataObject = new Namf_EventExposure_Subscribe();


        simulationDataObject.setNfId(1234);

        // notificationTargetUrl = Namf_EventExposure_Notify
        simulationDataObject.setNotificationTargetAddress(notificationTargetUrl);
        simulationDataObject.setEventId(1);
        simulationDataObject.setCorrelationId(String.valueOf(correationId));


        json.put("subscriptionId", subId);
        json.put("correlationId", correationId);
        json.put("unSubCorrelationId", namf_eventExposure_subscribe.getUnSubCorrelationId());

        // notificationTargetUrl = Namf_EventExposure_Notify
        json.put("notificationTargetAddress", notificationTargetUrl);

        //  System.out.println("\n");
        logger.debug(" Data send by NWDAF to SIMULATOR :: " +
                " , nfID - " + simulationDataObject.getNfId() +
                " , notificationTargetAddress " + notificationTargetUrl +
                " , eventId " + simulationDataObject.getEventId() +
                " , correlationId " + simulationDataObject.getCorrelationId()
        );


        // Saving values into subTable [correlationId and subId]
        collectorRepository.saveInfo(String.valueOf(subId), correationId);


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

        logger.debug(" POST Response Code :: " + responseCode);

        logger.info("Response received!");

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            logger.debug("Collector subscription Worked!");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            // System.out.println("\n");
            logger.debug(" unSubCorrelationId [ Response from SIMULATOR ] :: " + response);

            logger.info("Response Received [unSubCorrelationID ]");

            // Saving unSubCorrelationId into subTable table

            collectorRepository.updatesubTableWithunSubCorrealtionId(response.toString(), correationId);

            logger.info("Stored response of collector into nwdafSubTable");

            // Start Thread
            logger.info("Starting thread");
            startThread();

            // checking loadLevelInfoTable if subscription ID not present then adding it.


            //checking th values associated with subscription IDs
            checkThresholdValuesAndSendNotification(nnwdafEventsSubscription);


        } else {
            logger.warn(" POST request not worked");
        }

        return String.valueOf(correationId);

    }

    private List<NnwdafEventsSubscription> checkThresholdValuesAndSendNotification(NnwdafEventsSubscription nnwdafEventsSubscription) {

        EventID eventID = EventID.values()[nnwdafEventsSubscription.getEventID()];
        return repository.getALLSubID(eventID.ordinal());


    }


    @PutMapping(PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> updateNF(@PathVariable("subscriptionID") String id, @RequestBody NnwdafEventsSubscription user) {
        NnwdafEventsSubscription check_user = repository.findById(id);

        if (check_user == null) {
            logger.warn("no Content");
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
        }

        repository.updateNF(user, id);

        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.OK);
    }


    @DeleteMapping(value = PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> unsubscribeNF(@PathVariable("subscriptionID") String id) {
        NnwdafEventsSubscription user = repository.findById(id);

        if (user == null) {
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND);
        }

        repository.unsubscribeNF(id);

        return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NO_CONTENT);
    }


    @GetMapping(PATH + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> getNF(@PathVariable("subscriptionID") String id) {
        NnwdafEventsSubscription user = repository.findById(id);

        if (user == null) {
            return new ResponseEntity<NnwdafEventsSubscription>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<NnwdafEventsSubscription>(user, HttpStatus.OK);
    }


    // Accepting Notification
    @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_Notify/{subscriptionID}")
    public void acceptingNotification(@PathVariable String subscriptionID) {
        logger.debug(" Data Received for : " + subscriptionID);
        System.out.println("Data received !!");


    }


    @RequestMapping("/Namf_EventExposure_UnSubscribe/{unSubCorrelationId}")
    public String unSubscribeEvent(@PathVariable UUID unSubCorrelationId) {

        collectorRepository.unSubscribeEventViaUnSubCorrelationId(unSubCorrelationId);
        return " Event Deleted";
    }


    public void startThread() {
        logger.info("In Start Thread function");
        TestThread testThread = new TestThread();
        testThread.start();
        logger.info("Thread started.");


    }


    public List<UUID> getAllSubIds(List<NnwdafEventsSubscription> nnwdafEventsSubscription) {

        logger.info("fetching all subIDs");

        List<UUID> allSubIds = new ArrayList<>();


        for (int i = 0; i < nnwdafEventsSubscription.size(); i++) {

            allSubIds.add(nnwdafEventsSubscription.get(i).subscriptionID);
        }

        return allSubIds;

    }


    class TestThread extends Thread {
        public void run() {
            logger.info("In Thread!");

            List<UUID> ids = getAllSubIds(repository.getALLSubID(0));  //eventID.ordinal();


            for (int i = 0; i < ids.size(); i++) {

                //logger.debug(ids.get(i)+ "iteration");

                t_value = repository.findById(String.valueOf(ids.get(i))).getLoadLevelThreshold();
                logger.debug(" \n Subscription ID [ "
                        + repository.findById(String.valueOf(ids.get(i))).getSubscriptionID() + " ||  Threshold Value  - " + t_value);

                logger.debug(" \n Current Load Level  in Load_Level_info Table [ " +
                        repository.findDataByuSubId(String.valueOf(ids.get(i))).getSubscriptionID() + " ] "
                        + " Available Load  " + repository.findDataByuSubId(String.valueOf(ids.get(i))).getLoadLevelThreshold());

                while (repository.findDataByuSubId(String.valueOf(ids.get(i))).getLoadLevelThreshold() < t_value) {


                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("Checking load level in thread!");
                    logger.debug(" Subscription ID [ " + repository.findDataByuSubId(String.valueOf(ids.get(i))).getSubscriptionID() + " ] "
                            + "||  Load Level " + repository.findDataByuSubId(String.valueOf(ids.get(i))).getLoadLevelThreshold());
                }

                logger.debug("\n TH Reached ! Notification Send to :: "
                        //  + repository.findById(repository.findDataByuSubId(String.valueOf(ids.get(i))).getSubscriptionID()).getNotificationURI() + "/"
                        + repository.findById(repository.findDataByuSubId(String.valueOf(ids.get(i))).getSubscriptionID()).getNotificationURI());

                try {
                    out.println("NF URI " + repository.findById(repository.findDataByuSubId(String.valueOf(ids.get(i))).getSubscriptionID()).getNotificationURI());

                    sendNotificationToNF(repository.findById(repository.findDataByuSubId(String.valueOf(ids.get(i))).getSubscriptionID()).getNotificationURI());     // URI from NIKHIL Table;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void sendNotificationToNF(String notificationURI) throws IOException {


        out.println(notificationURI);

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

        String jsonInputString = "Hey I am Notificaion";

        System.out.println("\n\nSending Notificaiton From  NWDAF TO NF  - " + jsonInputString);

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
            System.out.println(response.toString());
            out.println("Response code from NF - " + HttpStatus.valueOf(con.getResponseCode()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
