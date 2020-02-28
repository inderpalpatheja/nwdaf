package com.nwdaf.AMF;


import com.nwdaf.AMF.model.Namf_EventExposure.Namf_EventExposure_Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
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


    Random rand = new Random();

    public static List<String> correlationIDList = new ArrayList<>();

    public List<String> getCorrelationIDList() {
        return correlationIDList;
    }

    // testing HTTP2 [ Not working ]
    @RequestMapping("/testHttp2")
    public String testHttp2() {
        return "Http2 Check!";
    }

    // Post Method for HTTP for 8082
    @RequestMapping(method = RequestMethod.POST, value = "/notify")
    public void addData(@RequestBody String string) throws Exception {

        JSONObject jsonObject = new JSONObject(string);

        String subId = jsonObject.getString("subscriptionID");
        String notificationURI = jsonObject.getString("notificaionURI");
        Integer loadLevel = jsonObject.getInt("currentLoadLevel");
        String snssais = jsonObject.getString("snssais");

        System.out.println("\n\nNotification Received From NWDAF -" + string);
        //  out.println(subId + " loadLevel " + loadLevel + "snssais" + snssais + "NotifcaionURI" + notificationURI);

        // Unsubscribe
        unsubscribe(notificationURI, subId);

    }

    //  private CallBackForCorrelationID backForCorrelationID;

    @PostMapping("/testConnection")
    public ResponseEntity<?> connectionTest() {
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/Nnrf_NFManagement_NFStatusUnSubscribe")
    public ResponseEntity<String> unsubScribeFromNWDAF(@RequestBody String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        String unSubCorrelationID = jsonObject.getString("mSubcorrelationID");
        String correlationID = jsonObject.getString("correlationID");

        correlationIDList.remove(correlationID);
        // list.delete();
        //     out.println("Unsubscribed from NWDAF WORKED for " + response);


        return new ResponseEntity<String>("unSubscribed", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/Nnrf_NFManagement_NFStatusSubscribe")
    public ResponseEntity<String> show(@RequestBody String response) throws JSONException, IOException {

        //  list.add();

        JSONObject json = new JSONObject(response);
        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();
        obj.setCorrelationId(json.getString("correlationID"));
        obj.setNotificationTargetAddress(json.getString("notificationTargetAddress"));


        //   out.println("Correlation ID Received From nwdaf as JSON - " + obj.getCorrelationId());

        // Adding unSubCorrelationId into database;

        UUID unSubCorrelationId = UUID.randomUUID();

        //   sendData(obj.getNotificationTargetAddress(), obj.getCorrelationId());

        correlationIDList.add(obj.getCorrelationId());
        // backForCorrelationID.getCorrelationList(correlationIDList);

        //   out.println("correlationList size - " + correlationIDList.size());

        //String NOTIFICATOIN_URL = obj.getNotificationTargetAddress() + "/" + obj.getCorrelationId();

        // out.println(NOTIFICATOIN_URL);


        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }


    // @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_notify/{correlationID}")


    // @PostMapping("/updateCurrentLoadLevel/{correlationID}")
    public ResponseEntity<String> sendData(String notiTargetAddress,
                                           String correlationID) throws IOException, JSONException {

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

        if(con != null)
        { con.disconnect(); }

        return new ResponseEntity<String>("Send", HttpStatus.OK);
    }


    //  @Override
    //public void getCorrelationList(List<String> correlationList) {
    //  out.println(correlationList.size());
    // }
}

