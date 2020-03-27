package com.nwdaf.AMF;


import com.nwdaf.AMF.model.Namf_EventExposure.Namf_EventExposure_Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.*;


import static java.lang.System.setOut;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static java.lang.System.out;

@RestController
public class AMFController extends Functionality {

    final String ranUeThroughput = "ranuethroughput";
    final String qosFlowRetain = "qosflowretain";


    Random rand = new Random();

    public static List<String> correlationIDList = new ArrayList<>();

    public List<String> getCorrelationIDList() {
        return correlationIDList;
    }


    @GetMapping("/sendDataToUEMobility/{correlationID}")
    public void sendUEData(@PathVariable String correlationID) throws IOException, JSONException {
        for (int i = 0; i < correlationIDList.size(); i++) {
           // sendDataForUEMobility("http://localhost:8081/Namf_EventExposure_Notify",
             //       correlationIDList.get(i));

            sendDataForUEMobility("http://localhost:8081/Namf_EventExposure_Notify",
                    correlationID);

        }
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

        // JSONArray jsonArray = new JSONArray(string);

        JSONObject jsonObject = new JSONObject(string);

        //String subId = jsonObject.getString("subscriptionID");
        //String notificationURI = jsonObject.getString("notificaionURI");
        //Integer loadLevel = jsonObject.getInt("currentLoadLevel");
        //String snssais = jsonObject.getString("snssais");


        //  out.println(subId + " loadLevel " + loadLevel + "snssais" + snssais + "NotifcaionURI" + notificationURI);
        //  out.println("notification URI - " + notificationURI);
        // Unsubscribe

        //unsubscribe(notificationURI, subId);

        //  out.println("subListSize - " + AmfApplication.getSubIDList().size());

        // Stirn
        Integer size= AmfApplication.getUnSubURIMap().size();
       // out.println("size of map " + size);


        // using for-each loop for iteration over Map.entrySet()
        for (Map.Entry<String, String> entry : AmfApplication.getUnSubURIMap().entrySet()) {

            if (entry.getKey().equals(suscriptionIDFromFinalNotificaion)) {
               // out.println("URI found -- > " + entry.getValue());
              //  out.println("ID found --- > " + entry.getKey());
                String UnSubURI = entry.getValue() + entry.getKey();
                out.println("Un-SubURI - - - > " + UnSubURI);


                unsubscribe(entry.getValue().trim(),entry.getKey().trim());
            }
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());
//
        }


        // unsubscribe();

        // out.println("subID - " + subId);
    }


    //  private CallBackForCorrelationID backForCorrelationID;

    @PostMapping("/testConnection")
    public ResponseEntity<?> connectionTest() {
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/Nnrf_NFManagement_NFStatusUnSubscribe")
    public ResponseEntity<String> unsubScribeFromNWDAFForSliceLoadLevel(@RequestBody String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        String unSubCorrelationID = jsonObject.getString("mSubcorrelationID");
        String correlationID = jsonObject.getString("correlationID");

        correlationIDList.remove(correlationID);
        // list.delete();
        //     out.println("Unsubscribed from NWDAF WORKED for " + response);


        return new ResponseEntity<String>("unSubscribed", HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.DELETE, value = "/Namf_Event_Exposure_UnSubscribe")
    public ResponseEntity<String> unsubScribeFromNWDAFForUEMobility(@RequestBody String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        String unSubCorrelationID = jsonObject.getString("mSubcorrelationID");
        String correlationID = jsonObject.getString("correlationID");

        correlationIDList.remove(correlationID);
        // list.delete();
        //     out.println("Unsubscribed from NWDAF WORKED for " + response);


        return new ResponseEntity<String>("unSubscribed", HttpStatus.OK);
    }


 /*   @RequestMapping(method = RequestMethod.DELETE, value = "/Nnrf_NFManagement_NFStatusUnSubscribe")
    public ResponseEntity<String> unsubScribeFromNWDAFForUEMobility(@RequestBody String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        String unSubCorrelationID = jsonObject.getString("mSubcorrelationID");
        String correlationID = jsonObject.getString("correlationID");

        correlationIDList.remove(correlationID);
        // list.delete();
        //     out.println("Unsubscribed from NWDAF WORKED for " + response);


        return new ResponseEntity<String>("unSubscribed", HttpStatus.OK);
    } */


    @RequestMapping(method = RequestMethod.POST, value = "/Nnrf_NFManagement_NFStatusSubscribe")
    public ResponseEntity<String> sliceLoadLevelDataFunction(@RequestBody String response) throws JSONException, IOException {

        //  list.add();

        JSONObject json = new JSONObject(response);
        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();
        obj.setCorrelationId(json.getString("correlationID"));
        obj.setNotificationTargetAddress(json.getString("notificationTargetAddress"));

        // Adding unSubCorrelationId into database;
        UUID unSubCorrelationId = UUID.randomUUID();
        correlationIDList.add(obj.getCorrelationId());

        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.POST, value = "/Namf_Event_Exposure_Subscribe")
    public ResponseEntity<String> UE_MobilityDataFunction(@RequestBody String response) throws JSONException, IOException {

        //  list.add();

        JSONObject json = new JSONObject(response);
        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();
        obj.setCorrelationId(json.getString("correlationID"));
        obj.setNotificationTargetAddress(json.getString("notificationTargetAddress"));

        // Adding unSubCorrelationId into database;
        UUID unSubCorrelationId = UUID.randomUUID();
        correlationIDList.add(obj.getCorrelationId());

        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }

    // @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_notify/{correlationID}")


    @PostMapping("/updateCurrentLoadLevel/{correlationID}")
    public ResponseEntity<String> sendData(String notiTargetAddress,
                                           @PathVariable("correlationID") String correlationID) throws IOException, JSONException {

        //  out.println("send Data check1");

        notiTargetAddress = "http://localhost:8081/Nnrf_NFManagement_NFStatusNotify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationID;
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

        json.put("currentLoadLevel", 1 + rand.nextInt(85));
        json.put("correlationID", correlationID);

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





    @PostMapping("/update/{qosLoadType}/{correlationID}")
    public ResponseEntity<String> sendData(String notiTargetAddress, @PathVariable("qosLoadType") String loadType, @PathVariable("correlationID") String correlationID) throws IOException, JSONException {

        //  out.println("send Data check1");

        notiTargetAddress = "http://localhost:8081/Noam_NFManagement_NFStatusNotify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationID;
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

        if(loadType.equals(ranUeThroughput))
        { json.put("ranUeThroughput", 1 + rand.nextInt(85)); }

        else if(loadType.equals(qosFlowRetain))
        { json.put("qosFlowRetain", 1 + rand.nextInt(85)); }

        else
        { return new ResponseEntity<String>("Invalid load value", HttpStatus.NOT_ACCEPTABLE); }

        json.put("correlationID", correlationID);

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

        if(con != null)
        { con.disconnect(); }

        return new ResponseEntity<String>("Send", HttpStatus.OK);
    }




    @GetMapping("/sendDataToUEMobility/{correlationID}")
    public void sendUEData(@PathVariable String correlationID) throws IOException, JSONException {
        for (int i = 0; i < correlationIDList.size(); i++) {
            // sendDataForUEMobility("http://localhost:8081/Namf_EventExposure_Notify",
            //       correlationIDList.get(i));

            sendDataForUEMobility("http://localhost:8081/Namf_EventExposure_Notify",
                    correlationID);

        }
    }




    public ResponseEntity<String> sendDataForUEMobility(String notiTargetAddress,
                                                        String correlationID) throws IOException, JSONException {

        //  out.println("In send Data for UE-Mobility");

        notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";


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

        JSONObject EcqiObject = new JSONObject();
        EcqiObject.put("plmn", plmnObject);
        EcqiObject.put("cellID", "cellID-String-value");


        JSONObject taiObject = new JSONObject();
        taiObject.put("plmn", plmnObject);
        taiObject.put("Tac", "TacID-value");


        JSONObject userLocation = new JSONObject();
        // userLocation.put("correlationID", correlationID);
        userLocation.put("timeDuration", randomTime1);
        userLocation.put("Tai", taiObject);
        userLocation.put("Ecqi", EcqiObject);

        JSONObject plmnObject1 = new JSONObject();
        plmnObject1.put("MNC", String.valueOf(threeDigitRandomValueForTwoLocaiton));
        plmnObject1.put("MCC", String.valueOf(twoDigitRandomValueForTwoLocation));

        JSONObject EcqiObject1 = new JSONObject();
        EcqiObject1.put("plmn", plmnObject1);
        EcqiObject1.put("cellID", "cellID-String-value-1");


        JSONObject taiObject1 = new JSONObject();
        taiObject1.put("plmn", plmnObject1);
        taiObject1.put("Tac", "TacID-value-1");


        JSONObject userLocation1 = new JSONObject();
        userLocation1.put("timeDuration", randomTime2);
        userLocation1.put("Tai", taiObject1);
        userLocation1.put("Ecqi", EcqiObject1);

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


        JSONObject EcqiObject1 = new JSONObject();
        EcqiObject1.put("plmn", plmnObject1);
        EcqiObject1.put("cellID", "cellID-String-value-1");


        JSONObject taiObject1 = new JSONObject();
        taiObject1.put("plmn", plmnObject1);
        taiObject1.put("Tac", "TacID-value-1");


        JSONArray ueTrajectoryArray = new JSONArray();
        JSONArray locationInfoArray = new JSONArray();


        JSONObject locationInfoObject = new JSONObject();
        locationInfoObject.put("timeDuration", 29);
        locationInfoObject.put("Tai", taiObject1);
        locationInfoObject.put("Ecqi", EcqiObject1);

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


}

