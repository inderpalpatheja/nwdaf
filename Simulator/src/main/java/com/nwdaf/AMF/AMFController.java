package com.nwdaf.AMF;

import com.nwdaf.AMF.Repository.CollectorRepository;
import com.nwdaf.AMF.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Base64;
import java.util.Random;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@RestController
public class AMFController {


    private static final String NOTIFICATION_URL = "http://localhost:8081/Namf_EventExposure_Notify";

    @Autowired
    CollectorRepository collectorRepository;

    @RequestMapping("/Amf")
    public String getData() {
        return "Hey AMF!!!!";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_Subscribe")
    public ResponseEntity<Integer> show(@RequestBody String response) throws JSONException, IOException {

        JSONObject json = new JSONObject(response);
        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();


        obj.setNfId(json.getInt("nfId"));
        obj.setEventId(json.getInt("eventId"));
        // obj.setTargetEventReporting(json.getJSONObject("targetEventReporting"));// ;
        obj.setNotificationTargetAddress(json.getString("notificationTargetAddress"));


        // Genrating random number to unsub
        Random rand = new Random();
        // Generate random integers in range 0 to 999
        int rand_UnSubCorrelation = rand.nextInt(1000);
        // Setting rand number in JSON
        obj.setUnSubCorrelationId(rand_UnSubCorrelation);
        obj.setCorrelationId(json.getInt("correlationId"));

        collectorRepository.saveInfo(obj);

        // Sending Notification when ever an event is created.
        sendNotification(obj);

        return new ResponseEntity<Integer>(obj.getUnSubCorrelationId(), HttpStatus.OK);
    }



    // Se
    private void sendNotification(Namf_EventExposure_Subscribe namf_eventExposure_subscribe) throws IOException, JSONException {


            URL obj = new URL(NOTIFICATION_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();


            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");


            String notificationString = "Sending Notifcation to " + namf_eventExposure_subscribe.getNotificationTargetAddress() +
                    namf_eventExposure_subscribe.getCorrelationId() + "!";

           // System.out.println(json.toString());

            // For POST only - START
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = notificationString.getBytes("utf-8");

                System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + notificationString);

                os.write(input, 0, input.length);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // For POST only - END

            int responseCode = con.getResponseCode();
            String responseMessage = con.getResponseMessage();
            System.out.println("POST Response Code :: " + responseCode);
            //  System.out.println("POST Response Message :: " + responseMessage);

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
                System.out.println("\n\n");
                System.out.println(response);
            } else {
                System.out.println("\n\n");
                System.out.println("POST request not worked");
            }
        }
    }

