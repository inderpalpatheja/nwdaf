package com.nwdaf.AMF;


import com.nwdaf.AMF.model.AmfEventType;
import com.nwdaf.AMF.model.Namf_EventExposure.Namf_EventExposure_Subscribe;

import com.nwdaf.AMF.model.NwdafEvent;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.*;



import static org.springframework.http.HttpHeaders.USER_AGENT;
import static java.lang.System.out;

@RestController
public class AMFController extends Functionality {

    final String ranUeThroughput = "ranuethroughput";
    final String qosFlowRetain = "qosflowretain";

    final String HTTP = "https";


    Random random = new Random();

    public static List<String> correlationIDList_LOAD_LEVEL_INFORMATION = new ArrayList<>();
    public static List<String> correlationIDList_UE_MOBILITY = new ArrayList<>();
    public static List<String> correlationIDList_QOS_SUSTAINABILITY = new ArrayList<>();
    public static List<String> correlationIDList_SERVICE_EXPERIENCE = new ArrayList<>();
    public static List<String> correlationIDList_NETWORK_PERFORMANCE = new ArrayList<>();
    public static List<String> correlationIDList_USER_DATA_CONGESTION = new ArrayList<>();
    public static List<String> correlationIDList_ABNORMAL_BEHAVIOUR = new ArrayList<>();


    public static List<String> getCorrelationIDList_LOAD_LEVEL_INFORMATION() {
        return correlationIDList_LOAD_LEVEL_INFORMATION;
    }

    public static List<String> getCorrelationIDList_UE_MOBILITY() {
        return correlationIDList_UE_MOBILITY;
    }

    public static List<String> getCorrelationIDList_QOS_SUSTAINABILITY() {
        return correlationIDList_QOS_SUSTAINABILITY;
    }

    public static List<String> getCorrelationIDList_SERVICE_EXPERIENCE() {
        return correlationIDList_SERVICE_EXPERIENCE;
    }

    public static List<String> getCorrelationIDList_NETWORK_PERFORMANCE() {
        return correlationIDList_NETWORK_PERFORMANCE;
    }

    public static List<String> getCorrelationIDList_USER_DATA_CONGESTION() {
        return correlationIDList_USER_DATA_CONGESTION;
    }

    public static List<String> getCorrelationIDList_ABNORMAL_BEHAVIOUR() {
        return correlationIDList_ABNORMAL_BEHAVIOUR;
    }


    @PostMapping("/sendDataToUEMobility/{correlationId}")
    public void sendUEData(@PathVariable String correlationId) throws IOException, JSONException {

        sendDataForUEMobility(HTTP + "://localhost:8081/Namf_Event_Exposure_Notify",
                correlationId);
    }


    // testing HTTP2 [ Not working ]
    @RequestMapping("/testHttp2")
    public String testHttp2() {
        return "Http2 Check!";
    }

    // Post Method for HTTP for 8082
    @RequestMapping(method = RequestMethod.POST, value = "/notify")
    public void addData(@RequestBody String string) throws Exception {

        //  out.println("in final-notification-test");
        System.out.println("\n\nNotification Received From NWDAF -" + string);


        JSONObject json = new JSONObject(string);
        String subscriptionID = json.getString("subscriptionId");


        unsubscribe(subscriptionID);
    }


    //  private CallBackForCorrelationID backForCorrelationID;

    @PostMapping("/testConnection")
    public ResponseEntity<?> connectionTest() {
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }


    //LOAD_LEVEL_INFORMATION
    @RequestMapping(method = RequestMethod.DELETE, value = "/Nnrf_NFManagement_NFStatusUnSubscribe")
    public ResponseEntity<String> unsubScribeFromNWDAFForSliceLoadLevel(@RequestBody String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        String unSubCorrelationID = jsonObject.getString("unSubCorrelationId");
        String correlationID = jsonObject.getString("correlationId");

        correlationIDList_LOAD_LEVEL_INFORMATION.remove(correlationID);
        // list.delete();
        //     out.println("Unsubscribed from NWDAF WORKED for " + response);


        return new ResponseEntity<String>("unSubscribed", HttpStatus.OK);
    }


    //UE_MOBILITY
    @RequestMapping(method = RequestMethod.DELETE, value = "/Namf_EventExposure_UnSubscribe")
    public ResponseEntity<String> unsubScribeFromNWDAFForUEMobility(@RequestBody String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        String unSubCorrelationID = jsonObject.getString("unSubCorrelationId");
        String correlationID = jsonObject.getString("correlationId");

        correlationIDList_UE_MOBILITY.remove(correlationID);
        // list.delete();
        //     out.println("Unsubscribed from NWDAF WORKED for " + response);


        return new ResponseEntity<String>("unSubscribed", HttpStatus.OK);
    }

    /*UE-Mobility*/


    @RequestMapping(method = RequestMethod.DELETE, value = "/Noam_EventExposure_UnSubscribe")
    public ResponseEntity<String> unsubscribeForQosSustainability(@RequestBody String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        String unSubCorrelationId = jsonObject.getString("unSubCorrelationId");
        String correlationId = jsonObject.getString("correlationId");

        NwdafEvent event = NwdafEvent.values()[jsonObject.getInt("event")];

        switch(event)
        {
            case QOS_SUSTAINABILITY: correlationIDList_QOS_SUSTAINABILITY.remove(correlationId);
                                     break;

            case NETWORK_PERFORMANCE: correlationIDList_NETWORK_PERFORMANCE.remove(correlationId);
                                      break;

            case USER_DATA_CONGESTION: correlationIDList_USER_DATA_CONGESTION.remove(correlationId);
                                       break;
        }


        return new ResponseEntity<String>("unSubscribed", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/Nnrf_NFManagement_NFStatusSubscribe")
    public ResponseEntity<String> sliceLoadLevelDataFunction(@RequestBody String response) throws JSONException, IOException {

        //  list.add();

        JSONObject json = new JSONObject(response);
        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();
        obj.setCorrelationId(json.getString("correlationId"));
        obj.setNotificationTargetAddress(json.getString("notificationTargetAddress"));

        // Adding unSubCorrelationId into database;
        UUID unSubCorrelationId = UUID.randomUUID();
        out.println("in-load-level----->> + " + obj.getNotificationTargetAddress() + "::" + obj.getCorrelationId());
        correlationIDList_LOAD_LEVEL_INFORMATION.add(obj.getCorrelationId());

        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.POST, value = "/Noam_EventExposure_Subscribe")
    public ResponseEntity<String> qosSustainabilityDataFunction(@RequestBody String response) throws JSONException, IOException {

        //  list.add();

        JSONObject json = new JSONObject(response);
        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();
        obj.setCorrelationId(json.getString("correlationId"));
        obj.setNotificationTargetAddress(json.getString("notificationTargetAddress"));

        // Adding unSubCorrelationId into database;
        UUID unSubCorrelationId = UUID.randomUUID();

        NwdafEvent event = NwdafEvent.values()[json.getInt("event")];

        if(event == NwdafEvent.QOS_SUSTAINABILITY)
        {
            out.println("in-QOS_SUSTAINABILITY----->> + " + obj.getNotificationTargetAddress() + "::" + obj.getCorrelationId());
            correlationIDList_QOS_SUSTAINABILITY.add(obj.getCorrelationId());
        }

        else if(event == NwdafEvent.NETWORK_PERFORMANCE)
        {
            out.println("in-NETWORK_PERFORMANCE----->> + " + obj.getNotificationTargetAddress() + "::" + obj.getCorrelationId());
            correlationIDList_NETWORK_PERFORMANCE.add(obj.getCorrelationId());
        }

        else if(event == NwdafEvent.USER_DATA_CONGESTION)
        {
            out.println("in-USER_DATA_CONGESTION----->> + " + obj.getNotificationTargetAddress() + "::" + obj.getCorrelationId());
            correlationIDList_USER_DATA_CONGESTION.add(obj.getCorrelationId());
        }

        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_Subscribe")
    public ResponseEntity<?> namfEventExposure_subscribe(@RequestBody String response) throws JSONException, IOException {

        JSONObject json = new JSONObject(response);

        NwdafEvent eventID = NwdafEvent.values()[json.getInt("event")];

        switch(eventID)
        {
            case UE_MOBILITY: return UE_MobilityDataFunction(json);

            case USER_DATA_CONGESTION: return userDataCongestion_UeLocation(json);

        }

        return null;
    }


    public ResponseEntity<?> userDataCongestion_UeLocation(JSONObject jsonData) throws JSONException, IOException {

        String supi = jsonData.getString("supi");
        AmfEventType amfEventType = AmfEventType.values()[jsonData.getInt("amfEventType")];

        if(amfEventType == AmfEventType.LOCATION_REPORT)
        {
            String mcc = String.valueOf(100 + random.nextInt(10));
            String mnc = String.valueOf(10);
            String tac = RandomStringUtils.randomAlphanumeric(6).toUpperCase();

            HttpHeaders responseHeaders = new HttpHeaders();

            responseHeaders.set("mcc", mcc);
            responseHeaders.set("mnc", mnc);
            responseHeaders.set("tac", tac);

            return new ResponseEntity(responseHeaders, HttpStatus.FOUND);
        }

        return null;
    }



    // SMF subscribe
    @RequestMapping(method = RequestMethod.POST, value = "/Nsmf_EventExposure_Subscribe")
    public ResponseEntity<String> nsmf_EventExposure_Subscribe(@RequestBody String response) throws JSONException, IOException {

        JSONObject json = new JSONObject(response);

        String correlationId = json.getString("correlationId");
        String notificationTargetAddress = json.getString("notificationTargetAddress");
        NwdafEvent event = NwdafEvent.values()[json.getInt("event")];


        // Adding unSubCorrelationId into database;
        UUID unSubCorrelationId = UUID.randomUUID();

        if(event == NwdafEvent.ABNORMAL_BEHAVIOUR)
        {
            out.println("in-ABNORMAL-BEHAVIOUR----->> + " + notificationTargetAddress + "::" + correlationId);
            correlationIDList_ABNORMAL_BEHAVIOUR.add(correlationId);
        }

        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }




    // SMF un-subscribe
    @RequestMapping(method = RequestMethod.DELETE, value = "/Nsmf_EventExposure_UnSubscribe")
    public ResponseEntity<String> nsmf_EventExposure_UnSubscribe(@RequestBody String response) throws JSONException, IOException {

        JSONObject json = new JSONObject(response);
        String unSubCorrelationID = json.getString("unSubCorrelationId");
        String correlationID = json.getString("correlationId");
        NwdafEvent event = NwdafEvent.values()[json.getInt("event")];

        switch(event)
        {
            case ABNORMAL_BEHAVIOUR: correlationIDList_ABNORMAL_BEHAVIOUR.remove(correlationID);
                                     break;
        }


        return new ResponseEntity<String>("unSubscribed", HttpStatus.OK);
    }






    public ResponseEntity<String> UE_MobilityDataFunction(JSONObject jsonData) throws JSONException, IOException {

        //  list.add();

        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();
        obj.setCorrelationId(jsonData.getString("correlationId"));
        obj.setNotificationTargetAddress(jsonData.getString("notificationTargetAddress"));

        // Adding unSubCorrelationId into database;
        UUID unSubCorrelationId = UUID.randomUUID();

        out.println("in-UE-Mobility----->> + " + obj.getNotificationTargetAddress() + "::" + obj.getCorrelationId());
        correlationIDList_UE_MOBILITY.add(obj.getCorrelationId());

        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }






    // @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_notify/{correlationID}")


    @PostMapping("/updateCurrentLoadLevel/{correlationId}")
    public ResponseEntity<String> sendData(String notiTargetAddress,
                                           @PathVariable("correlationId") String correlationId) throws IOException, JSONException {

        //  out.println("send Data check1");

        notiTargetAddress = HTTP + "://localhost:8081/Nnrf_NFManagement_NFStatusNotify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationId;
        //  out.println("updated URl - " + updated_URL);
        URL url = new URL(updated_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        // String notificationString = "\n\nSending Notification to " + notiTargetAddress + "/" +
        //      correlationID;

        // int currnetLoadLevel =50;

        JSONObject json = new JSONObject();

        json.put("currentLoadLevel", 1 + random.nextInt(85));
        json.put("correlationId", correlationId);

        // out.println("check " + correlationID);

        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");

            // System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + notificationString);

            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();
        //String responseMessage = con.getResponseMessage();
        // System.out.println("POST Response Code :: " + HttpStatus.valueOf(responseCode).toString());
        //System.out.println("POST Response Message :: " + responseMessage);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            // System.out.println("\n\n");
            //  System.out.println(response);
        } else {
            // System.out.println("\n\n");
            // System.out.println("POST request not worked");
        }
        //  return "Data send to " + updated_URL;

        if (con != null) {
            con.disconnect();
        }

        return new ResponseEntity<String>("Send", HttpStatus.OK);
    }


    //  @Override
    //public void getCorrelationList(List<String> correlationList) {
    //  out.println(correlationList.size());
    // }


    @PostMapping("/sendQosSustainabilityData/{qosLoadType}/{correlationId}")
    public ResponseEntity<String> sendData(String notiTargetAddress, @PathVariable("qosLoadType") String loadType, @PathVariable("correlationId") String correlationId) throws IOException, JSONException {

        //  out.println("send Data check1");

        notiTargetAddress = HTTP + "://localhost:8081/Noam_EventExposure_Notify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationId;
        //  out.println("updated URl - " + updated_URL);
        URL url = new URL(updated_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        // String notificationString = "\n\nSending Notification to " + notiTargetAddress + "/" +
        //      correlationID;

        // int currnetLoadLevel =50;

        JSONObject json = new JSONObject();

        loadType = loadType.toLowerCase();

        if (loadType.equals(ranUeThroughput)) {
            json.put("ranUeThrou", 1 + random.nextInt(85));
        } else if (loadType.equals(qosFlowRetain)) {
            json.put("qosFlowRet", 1 + random.nextInt(85));
        } else {
            return new ResponseEntity<String>("Invalid load value", HttpStatus.NOT_ACCEPTABLE);
        }

        json.put("correlationId", correlationId);
        json.put("event", NwdafEvent.QOS_SUSTAINABILITY.ordinal());

        // out.println("check " + correlationID);

        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");

            // System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + notificationString);

            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();
        //String responseMessage = con.getResponseMessage();
        // System.out.println("POST Response Code :: " + HttpStatus.valueOf(responseCode).toString());
        //System.out.println("POST Response Message :: " + responseMessage);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            // System.out.println("\n\n");
            //  System.out.println(response);
        } else {
            // System.out.println("\n\n");
            // System.out.println("POST request not worked");
        }
        //  return "Data send to " + updated_URL;

        if (con != null) {
            con.disconnect();
        }

        return new ResponseEntity<String>("Send", HttpStatus.OK);
    }


    public ResponseEntity<String> sendDataForUEMobility(String notiTargetAddress,
                                                        String correlationID) throws IOException, JSONException {

        //  out.println("In send Data for UE-Mobility");

        notiTargetAddress = HTTP + "://localhost:8081/Namf_Event_Exposure_Notify";


        // initialize a Random object somewhere; you should only need one
        Random random = new Random();

        // generate a random integer from 0 to 899, then add 100
        int threeDigitRandomValueForOneLocaiton = random.nextInt(900) + 100;
        int twoDigitRandomValueForOneLocation = random.nextInt(90) + 10;

        int threeDigitRandomValueForTwoLocaiton = random.nextInt(900) + 100;
        int twoDigitRandomValueForTwoLocation = random.nextInt(90) + 10;

        int randomTime1 = random.nextInt(90) + 10;
        int randomTime2 = random.nextInt(90) + 10;

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationID;
        //  out.println("updated URl - " + updated_URL);
        URL url = new URL(updated_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        JSONObject plmnObject = new JSONObject();
        plmnObject.put("MNC", String.valueOf(threeDigitRandomValueForOneLocaiton));
        plmnObject.put("MCC", String.valueOf(twoDigitRandomValueForOneLocation));

        JSONObject EcgiObject = new JSONObject();
        EcgiObject.put("plmn", plmnObject);
        EcgiObject.put("cellID", "cellID-String-value");


        JSONObject taiObject = new JSONObject();
        taiObject.put("plmn", plmnObject);
        taiObject.put("Tac", "TacID-value");


        JSONObject userLocation = new JSONObject();
        // userLocation.put("correlationID", correlationID);
        userLocation.put("timeDuration", randomTime1);
        userLocation.put("Tai", taiObject);
        userLocation.put("Ecgi", EcgiObject);

        JSONObject plmnObject1 = new JSONObject();
        plmnObject1.put("MNC", String.valueOf(threeDigitRandomValueForTwoLocaiton));
        plmnObject1.put("MCC", String.valueOf(twoDigitRandomValueForTwoLocation));

        JSONObject EcgiObject1 = new JSONObject();
        EcgiObject1.put("plmn", plmnObject1);
        EcgiObject1.put("cellID", "cellID-String-value-1");


        JSONObject taiObject1 = new JSONObject();
        taiObject1.put("plmn", plmnObject1);
        taiObject1.put("Tac", "TacID-value-1");


        JSONObject userLocation1 = new JSONObject();
        userLocation1.put("timeDuration", randomTime2);
        userLocation1.put("Tai", taiObject1);
        userLocation1.put("Ecgi", EcgiObject1);

        JSONArray USER_LOCATION_ARRAY = new JSONArray();
        USER_LOCATION_ARRAY.put(correlationID);
        USER_LOCATION_ARRAY.put(userLocation);
        USER_LOCATION_ARRAY.put(userLocation1);

        //out.println(USER_LOCATION_ARRAY);


        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = USER_LOCATION_ARRAY.toString().getBytes("utf-8");


            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }


        if (con != null) {
            con.disconnect();
        }

        // return new ResponseEntity<String>(USER_LOCATION_ARRAY.toString(), HttpStatus.OK);
        return new ResponseEntity<>("Created", HttpStatus.OK);
    }


    public void testJSONData() throws JSONException {

        Random random = new Random();

        /*JSON NOTIFICATION ACCORDING 3GPP START*/

        // generate a random integer from 0 to 899, then add 100
        //  int threeDigitRandomValueForOneLocaiton = random.nextInt(900) + 100;
        //  int twoDigitRandomValueForOneLocation = random.nextInt(90) + 10;

        int threeDigitRandomValueForTwoLocaiton = random.nextInt(900) + 100;
        int twoDigitRandomValueForTwoLocation = random.nextInt(90) + 10;

        //  int randomTime1 = random.nextInt(90)+10;
        // int randomTime2 = random.nextInt(90)+10;

        JSONArray eventNotificationArray = new JSONArray();
        JSONObject eventNotificationFinalObject = new JSONObject();
        JSONObject eventNotificationObject = new JSONObject();
        JSONArray uEMobilityArray = new JSONArray();
        JSONObject sliceLoadLevelInfo = new JSONObject();
        JSONArray snssaisArray = new JSONArray();
        JSONObject ueMobilityObject = new JSONObject();


        JSONObject plmnObject1 = new JSONObject();
        plmnObject1.put("MNC", String.valueOf(threeDigitRandomValueForTwoLocaiton));
        plmnObject1.put("MCC", String.valueOf(twoDigitRandomValueForTwoLocation));


        JSONObject EcgiObject1 = new JSONObject();
        EcgiObject1.put("plmn", plmnObject1);
        EcgiObject1.put("cellID", "cellID-String-value-1");


        JSONObject taiObject1 = new JSONObject();
        taiObject1.put("plmn", plmnObject1);
        taiObject1.put("Tac", "TacID-value-1");


        JSONArray ueTrajectoryArray = new JSONArray();
        JSONArray locationInfoArray = new JSONArray();


        JSONObject locationInfoObject = new JSONObject();
        locationInfoObject.put("timeDuration", 29);
        locationInfoObject.put("Tai", taiObject1);
        locationInfoObject.put("Ecgi", EcgiObject1);

        locationInfoArray.put(locationInfoObject);


        JSONObject ueTrajectoryObject = new JSONObject();

        ueTrajectoryObject.put("ts", "Date-time-value");
        ueTrajectoryObject.put("recurringTime", "recurringTime-value");
        ueTrajectoryObject.put("duration", 30);
        ueMobilityObject.put("supi", "supi-value");
        ueTrajectoryObject.put("locInfo", locationInfoArray);

        ueTrajectoryArray.put(ueTrajectoryObject);

        ueMobilityObject.put("ueTraj", ueTrajectoryArray);


        uEMobilityArray.put(ueMobilityObject);

        snssaisArray.put("snssais");
        sliceLoadLevelInfo.put("loadLevelInformation", 100);
        sliceLoadLevelInfo.put("snssais", snssaisArray);

        eventNotificationObject.put("NwdafEvent", "0,5");
        eventNotificationObject.put("SliceLoadLevelInformation", sliceLoadLevelInfo);
        eventNotificationObject.put("ueMobs", uEMobilityArray);

        eventNotificationArray.put(eventNotificationObject);

        eventNotificationFinalObject.put("eventNotifications", eventNotificationArray);
        eventNotificationFinalObject.put("subscriptionId", 12345);

        out.println(eventNotificationFinalObject.toString());





        /*JSON NOTIFICATION ACCORDING 3GPP ENDS*/
    }



    // SERVICE_EXPERIENCE subscription
    @RequestMapping(method = RequestMethod.POST, value = "/Nnf_EventExposure_Subscribe")
    public ResponseEntity<String> subscribe_ServiceExperience(@RequestBody String response) throws JSONException, IOException {

        //  list.add();

        JSONObject json = new JSONObject(response);

        String correlationId = json.getString("correlationId");
        String notificationTargetAddress = json.getString("notificationTargetAddress");

        // Adding unSubCorrelationId into database;
        UUID unSubCorrelationId = UUID.randomUUID();

        out.println("In SERVICE_EXPERIENCE----->> + " + notificationTargetAddress + "::" + correlationId);

        correlationIDList_SERVICE_EXPERIENCE.add(correlationId);

        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }


    // SERVICE_EXPERIENCE un-subscription
    @RequestMapping(method = RequestMethod.DELETE, value = "/Nnf_EventExposure_UnSubscribe")
    public ResponseEntity<String> unSubscribe_ServiceExperience(@RequestBody String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        String unSubCorrelationId = jsonObject.getString("unSubCorrelationId");
        String correlationId = jsonObject.getString("correlationId");

        correlationIDList_SERVICE_EXPERIENCE.remove(correlationId);

        return new ResponseEntity<String>("un-subscribed SERVICE_EXPERIENCE", HttpStatus.OK);
    }



    @PostMapping("/sendServiceExperienceData/{correlationId}")
    public ResponseEntity<String> sendServiceExperienceData(String notiTargetAddress, @PathVariable("correlationId") String correlationId) throws IOException, JSONException {

        //  out.println("send Data check1");

        notiTargetAddress = HTTP + "://localhost:8081/Nnf_EventExposure_Notify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationId;
        //  out.println("updated URl - " + updated_URL);
        URL url = new URL(updated_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        /***************************************************

        JSONObject json = new JSONObject();

        List<Float> svcExpData = new ArrayList<>();
        int dataSet = 10 + random.nextInt(6);

        for(int i = 0; i < dataSet; i++)
        { svcExpData.add(10 * random.nextFloat()); }

        json.put("correlationID", correlationID);
        json.put("svcExpData", svcExpData.toString());

        ***************************************************/

        JSONObject json = new JSONObject();

        json.put("mos", String.valueOf(10 * random.nextFloat()));
        json.put("upperRange", String.valueOf(10 * random.nextFloat()));
        json.put("lowerRange", String.valueOf(10 * random.nextFloat()));

        json.put("correlationId", correlationId);


        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");

            // System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + notificationString);

            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();
        //String responseMessage = con.getResponseMessage();
        // System.out.println("POST Response Code :: " + HttpStatus.valueOf(responseCode).toString());
        //System.out.println("POST Response Message :: " + responseMessage);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            // System.out.println("\n\n");
            //  System.out.println(response);
        } else {
            // System.out.println("\n\n");
            // System.out.println("POST request not worked");
        }
        //  return "Data send to " + updated_URL;

        if (con != null) {
            con.disconnect();
        }

        return new ResponseEntity<String>("Sent", HttpStatus.OK);
    }


    @PostMapping("sendNetworkPerformanceData/{correlationId}")
    public ResponseEntity<String> sendNetworkPerformanceData(String notiTargetAddress, @PathVariable("correlationId") String correlationId) throws IOException, JSONException
    {
        //  out.println("send Data check1");

        notiTargetAddress = HTTP + "://localhost:8081/Noam_EventExposure_Notify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationId;
        //  out.println("updated URl - " + updated_URL);
        URL url = new URL(updated_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        JSONObject json = new JSONObject();

        json.put("reportingThreshold", 20 + random.nextInt(80));
        json.put("correlationId", correlationId);
        json.put("event", NwdafEvent.NETWORK_PERFORMANCE.ordinal());


        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");

            // System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + notificationString);

            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();
        //String responseMessage = con.getResponseMessage();
        // System.out.println("POST Response Code :: " + HttpStatus.valueOf(responseCode).toString());
        //System.out.println("POST Response Message :: " + responseMessage);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            // System.out.println("\n\n");
            //  System.out.println(response);
        } else {
            // System.out.println("\n\n");
            // System.out.println("POST request not worked");
        }
        //  return "Data send to " + updated_URL;

        if (con != null) {
            con.disconnect();
        }

        return new ResponseEntity<String>("Sent NETWORK_PERFORMANCE data", HttpStatus.OK);
    }



    @PostMapping("sendUserDataCongestionLevel/{correlationId}")
    public ResponseEntity<String> sendUserDataCongestionLevel(String notiTargetAddress, @PathVariable("correlationId") String correlationId) throws IOException, JSONException {
        //  out.println("send Data check1");

        notiTargetAddress = HTTP + "://localhost:8081/Noam_EventExposure_Notify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationId;
        //  out.println("updated URl - " + updated_URL);
        URL url = new URL(updated_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        JSONObject json = new JSONObject();

        json.put("congLevel", 20 + random.nextInt(80));
        json.put("correlationId", correlationId);
        json.put("event", NwdafEvent.USER_DATA_CONGESTION.ordinal());


        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");

            // System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + notificationString);

            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();
        //String responseMessage = con.getResponseMessage();
        // System.out.println("POST Response Code :: " + HttpStatus.valueOf(responseCode).toString());
        //System.out.println("POST Response Message :: " + responseMessage);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            // System.out.println("\n\n");
            //  System.out.println(response);
        } else {
            // System.out.println("\n\n");
            // System.out.println("POST request not worked");
        }
        //  return "Data send to " + updated_URL;

        if (con != null) {
            con.disconnect();
        }

        return new ResponseEntity<String>("Sent USER_DATA_CONGESTION data", HttpStatus.OK);
    }






    @PostMapping("sendAbnormalBehaviourData/{correlationId}")
    public ResponseEntity<String> sendAbnormalBehaviourData(String notiTargetAddress, @PathVariable("correlationId") String correlationId) throws IOException, JSONException
    {
        //  out.println("send Data check1");

        notiTargetAddress = HTTP + "://localhost:8081/Nsmf_EventExposure_Notify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationId;
        //  out.println("updated URl - " + updated_URL);
        URL url = new URL(updated_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        JSONObject json = new JSONObject();

        json.put("excepLevel", 20 + random.nextInt(80));
        json.put("correlationId", correlationId);
        json.put("event", NwdafEvent.ABNORMAL_BEHAVIOUR.ordinal());


        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");

            // System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + notificationString);

            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();
        //String responseMessage = con.getResponseMessage();
        // System.out.println("POST Response Code :: " + HttpStatus.valueOf(responseCode).toString());
        //System.out.println("POST Response Message :: " + responseMessage);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            // System.out.println("\n\n");
            //  System.out.println(response);
        } else {
            // System.out.println("\n\n");
            // System.out.println("POST request not worked");
        }
        //  return "Data send to " + updated_URL;

        if (con != null) {
            con.disconnect();
        }

        return new ResponseEntity<String>("Sent ABNORMAL_BEHAVIOUR data", HttpStatus.OK);
    }



}

