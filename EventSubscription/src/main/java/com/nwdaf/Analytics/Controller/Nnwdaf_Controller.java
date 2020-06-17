package com.nwdaf.Analytics.Controller;


import com.nwdaf.Analytics.Model.CustomData.TargetUeInformation;
import com.nwdaf.Analytics.Model.EventFilter;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.MetaData.OperationInfo;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.NwdafEvent;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Nnwdaf_Service;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.*;
import java.util.HashMap;


@RestController
public class Nnwdaf_Controller {

    private static final Logger logger = LoggerFactory.getLogger(Nnwdaf_Controller.class);

    private final Nnwdaf_Service nwdaf_service;

    final String EVENT_SUB = "/nnwdaf-eventssubscription/v1";
    final String ANALYTICS ="/nnwdaf-analyticsinfo/v1/analytics";

    public static Counters EventCounter[] = new Counters[NwdafEvent.values().length];


    @Autowired
    public Nnwdaf_Controller(Nnwdaf_Service nwdaf_service) {

        logger.debug("Entered Nnwdaf_controller()");
        //logger.info("Thread started");

        for(int i = 0; i < EventCounter.length; i++)
        { EventCounter[i] = new Counters(); }

        this.nwdaf_service = nwdaf_service;

        logger.debug("Exit Nnwdaf_controller()");
    }





    /**
     * @param event
     * @param tgtUe
     * @param eventFilter
     * @return
     * @throws IOException
     * @throws JSONException
     * @desc this will hold functions for Analytics information
     */

    // getAnalytics for all eventIDs
    @GetMapping(ANALYTICS)
    @ApiOperation(value = "Get Analytics Details By snssais or anySlice Details",
            notes = "Provide snssais, anySlice and eventID to look up specific Analytics Information from NWDAF API",
            response = Object.class)
    public Object nwdaf_analytics(@RequestParam("event-id") NwdafEvent event, TargetUeInformation tgtUe, EventFilter eventFilter) throws IOException, JSONException {


        return nwdaf_service.nwdaf_analytics(event, tgtUe, eventFilter);
    }



    // For testing purpose only
    @GetMapping("/testQueryParams")
    public Object getEventFilterData(@RequestParam("event-id") NwdafEvent eventId, TargetUeInformation tgtUe, EventFilter eventFilter)
    {
        HashMap<Object, Object> map = new HashMap<>();

        map.put("event-id", eventId);
        map.put("tgt-Ue", tgtUe);
        map.put("event-filter", eventFilter);

        return map;
    }


    @PostMapping("/test/restTemplate")
    public ResponseEntity<?> restTemplateTest(@RequestBody String response) throws JSONException, URISyntaxException {

        JSONObject json = new JSONObject(response);

        System.out.println("Name: " + json.getString("Name"));
        System.out.println("Age: " + json.getInt("Age"));

        URI location = new URI("http://localhost:8443/nikhil");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        httpHeaders.set("Message", "RestTemplate Up & working!");

        return new ResponseEntity("Its Done", httpHeaders, HttpStatus.ACCEPTED);
    }



  /*  /**
     * @param nnwdafEventsSubscription
     * @return
     * @throws SQLIntegrityConstraintViolationException
     * @throws URISyntaxException
     * @throws IOException
     * @throws JSONException
     * @desc this will hold functions to for event subscriptions
     */
    @PostMapping(EVENT_SUB + "/subscriptions")
    @ApiOperation(value = OperationInfo.SUBSCRIBE_INFO, notes = OperationInfo.SUBSCRIBE_NOTES, response = Object.class)
    public Object nwdaf_subscription(@RequestBody NnwdafEventsSubscription nnwdafEventsSubscription) {


        return nwdaf_service.nwdaf_subscription(nnwdafEventsSubscription);
    }




    /*
    /**
     * @param subscriptionID
     * @param nnwdafEventsSubscription
     * @return
     * @desc this function will update network function subscription
     */
    @PutMapping(EVENT_SUB + "/subscriptions/{subscriptionID}")
    @ApiOperation(value = OperationInfo.UPDATE_SUBSCRIPTION_INFO, notes = OperationInfo.UPDATE_SUBSCRIPTION_NOTES, response = Object.class)
    public ResponseEntity<?> update_nf_subscription(@PathVariable("subscriptionID") String subscriptionID, @RequestBody SubUpdateRawData updateData) {

        //return nwdaf_service.update_nf_subscription(subscriptionID, updateData);
        return null;
    }




    /**
     * @param subscriptionID
     * @return
     * @desc this function unsubscribe network function
     */
    @DeleteMapping(value = EVENT_SUB + "/subscriptions/{subscriptionID}")
    @ApiOperation(value = OperationInfo.UNSUBSCRICE_INFO, notes = OperationInfo.UNSUBSCRIBE_NOTES, response = Object.class)
    public ResponseEntity<?> unsubscription_nf(@PathVariable("subscriptionID") String subscriptionID) throws Exception {

        return nwdaf_service.unsubscription_nf(subscriptionID);
    }




    @GetMapping(EVENT_SUB + "/subscriptions/{subscriptionID}")
    public ResponseEntity<?> get_all_network_function(@PathVariable("subscriptionID") String id) {

        return nwdaf_service.get_all_network_function(id);
    }




    // Accepting Notification related to LOAD_LEVEL_INFORMATION [ from Simulator]
    @RequestMapping(method = RequestMethod.POST, value = "/Nnrf_NFManagement_NFStatusNotify/{correlationID}")
    public void acceptingNotification(@RequestBody String response) throws Exception {

        nwdaf_service.notificationHandler_LoadLevelInformation(response);

    }




    // Accepting Notification UE-Mobility [ from Simulator]
    @RequestMapping(method = RequestMethod.POST, value = "/Namf_Event_Exposure_Notify/{correlationID}")
    public void acceptingNotificationFromUEMobility(@RequestBody String response) throws Exception {


        nwdaf_service.notificationHandlerForUEMobility(response);

    }


    // Accepting Notification related to SERVICE_EXPERIENCE [ from Simulator]
    @PostMapping("/Nnf_EventExposure_Notify/{correlationID}")
    public void acceptingNotification_ServiceExperience(@RequestBody String response)
    {
        nwdaf_service.notificationHandler_ServiceExperience(response);
    }


    // Accepting Notification related to QOS_SUSTAINABILITY & NETWORK_PERFORMANCE [ from Simulator]
    @PostMapping("/Noam_EventExposure_Notify/{correlationId}")
    public void acceptingNotification_OAM(@RequestBody String response) throws JSONException, IOException {

        nwdaf_service.notificationHandlerOAM(response);
    }


    // Accepting Notification related to ABNORMAL_BEHAVIOUR [ from Simulator]
    @PostMapping("/Nsmf_EventExposure_Notify/{correlationId}")
    public void acceptingNotification_SMF(@RequestBody String response) throws JSONException, IOException {

        nwdaf_service.notificationHandlerSMF(response);
    }



 /*   @RequestMapping(method = RequestMethod.GET, value = "/Nnrf_NFManagement_NFStatusNotify/{unSubCorrelationId}")
    public String unSubscribeEvent(@PathVariable UUID unSubCorrelationId) {

        logger.debug("Enter unSubscribeEvent()");
        logger.debug("Exit unSubscribeEvent()");
        return " Event Deleted";
    }
*/

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
     * this function shows counters
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/counters")
    @ApiOperation(value = OperationInfo.COUNTERS_INFO, notes = OperationInfo.COUNTERS_NOTES, response = Object.class)
    public Object nwdaf_counters(@RequestParam(value = "eventID", required = false) Integer eventID) {

        return nwdaf_service.nwdaf_counters(eventID);
    }


    @GetMapping("/error_counters")
    public Object nwdaf_error_counters() {

        return nwdaf_service.nwdaf_error_counters();
    }




    /**
     * @return String
     * @desc this function will reset all the counters
     */
    @PostMapping("/counters/reset")
    @ApiOperation(value = OperationInfo.COUNTERS_ZERO_INFO, notes = OperationInfo.COUNTERS_ZERO_NOTES, response = Object.class)
    public String nwdaf_reset_counter() {

        return nwdaf_service.nwdaf_reset_counter();
    }




    @RequestMapping(method = RequestMethod.GET, value = "/apiInfo")
    public Object check_api_details() throws IOException {

        return nwdaf_service.check_api_details();

    }


/*
    @PostMapping("test/restTemplate")
    public ResponseEntity<?> testCode(@RequestBody String user) throws JSONException
    {
        JSONObject json = new JSONObject(user);

        String name = json.getString("name");
        Integer age = json.getInt("age");
        String password = json.getString("password");

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Password: " + password);

        SubscriptionTable subscription = new SubscriptionTable();

        subscription.setSubscriptionID(UUID.randomUUID().toString());
        subscription.setEventID(0);
        subscription.setNotificationURI("http://www.gitlife.com");
        subscription.setNotifMethod(0);
        subscription.setRepetitionPeriod(22);

        return new ResponseEntity<SubscriptionTable>(subscription, HttpStatus.OK);
    } */

}

