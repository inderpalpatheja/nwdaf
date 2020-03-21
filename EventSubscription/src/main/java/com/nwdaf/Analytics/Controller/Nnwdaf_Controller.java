package com.nwdaf.Analytics.Controller;


import com.nwdaf.Analytics.Model.MetaData.OperationInfo;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Nnwdaf_Service;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;




@RestController
public class Nnwdaf_Controller {

    private static final Logger logger = LoggerFactory.getLogger(Nnwdaf_Controller.class);

    private final Nnwdaf_Service nwdaf_service;

    final String EVENT_SUB = "/nnwdaf-eventssubscription/v1";
    final String ANALYTICS ="/apiroot/nnwdaf-analyticsinfo/v1";


    @Autowired
    public Nnwdaf_Controller(Nnwdaf_Service nwdaf_service) {

        logger.debug("Entered Nnwdaf_controller()");
        //logger.info("Thread started");

        this.nwdaf_service = nwdaf_service;

        logger.debug("Exit Nnwdaf_controller()");
    }





    /**
     * @param snssais
     * @param anySlice
     * @param eventID
     * @return
     * @throws IOException
     * @throws JSONException
     * @desc this will hold functions for Analytics information
     */

    @GetMapping(ANALYTICS + "/{snssais}/{anySlice}/{eventID}")
    @ApiOperation(value = "Get Analytics Details By snssais or anySlice Details",
            notes = "Provide snssais, anySlice and eventID to look up specific Analytics Information from NWDAF API",
            response = Object.class)
    public Object nwdaf_analytics(@PathVariable("snssais") String snssais,
                                  @PathVariable("anySlice") Boolean anySlice,
                                  @PathVariable("eventID") int eventID) throws IOException, JSONException {


        return nwdaf_service.nwdaf_analytics(snssais, anySlice, eventID);

    }

    @GetMapping(ANALYTICS + "/{supi}/{eventID}")
    @ApiOperation(value = "Get Analytics Details By supi or anySlice Details",
            notes = "Provide snssais, anySlice and eventID to look up specific Analytics Information from NWDAF API",
            response = Object.class)
    public Object nwdaf_analyticsForUEMobility(@PathVariable("supi") String supi,
                                   Boolean anySlice,
                                  @PathVariable("eventID") int eventID) throws IOException, JSONException {


        return nwdaf_service.nwdaf_analyticsForUEMobility(supi, false, eventID);

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
    public Object nwdaf_subscription(@RequestBody SubscriptionRawData subscriptionRawData) throws SQLIntegrityConstraintViolationException, URISyntaxException, IOException, JSONException {


        return nwdaf_service.nwdaf_subscription(subscriptionRawData);
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

        return nwdaf_service.update_nf_subscription(subscriptionID, updateData);
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




    // Accepting Notification [ from Simulator]
    @RequestMapping(method = RequestMethod.POST, value = "/Nnrf_NFManagement_NFStatusNotify/{correlationID}")
    public void acceptingNotification(@RequestBody String response) throws Exception {

        nwdaf_service.notificationHandler(response);

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
    @GetMapping("/nwdaf/counter")
    @ApiOperation(value = OperationInfo.COUNTERS_INFO, notes = OperationInfo.COUNTERS_NOTES, response = Object.class)
    public HashMap<String, BigInteger> nwdaf_counters() throws Exception {

        return nwdaf_service.nwdaf_counters();
    }




    /**
     * @return String
     * @desc this function will reset all the counters
     */
    @PostMapping("/nwdaf/counter")
    @ApiOperation(value = OperationInfo.COUNTERS_ZERO_INFO, notes = OperationInfo.COUNTERS_ZERO_NOTES, response = Object.class)
    public String nwdaf_reset_counter() {

        return nwdaf_service.nwdaf_reset_counter();
    }




    @RequestMapping(method = RequestMethod.GET, value = "/apiInfo")
    public Object check_api_details() throws IOException {

        return nwdaf_service.check_api_details();

    }


    // Accepting Notification UE-Mobility [ from Simulator]
    @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_Notify/{correlationID}")
    public void acceptingNotificationFromUEMobility(@RequestBody String response) throws Exception {



        nwdaf_service.notificationHandlerForUEMobility(response);



    }



}

