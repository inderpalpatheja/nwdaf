package com.nwdaf.AMF;

import com.nwdaf.AMF.Repository.CollectorRepository;
import com.nwdaf.AMF.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.USER_AGENT;
import static java.lang.System.out;

@RestController
public class AMFController extends Functionality {


    @Autowired
    CollectorRepository collectorRepository;



    @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_Subscribe")
    public ResponseEntity<String> show(@RequestBody String response) throws JSONException, IOException {

       JSONObject json = new JSONObject(response);

        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();

        obj.setCorrelationId(json.getString("correlationId"));
        obj.setNotificationTargetAddress(json.getString("notificationTargetAddress"));



        // Adding unSubCorrelationId into database;

        UUID unSubCorrelationId = UUID.randomUUID();

        sendNotification(obj, obj.getNotificationTargetAddress() + "/" + obj.getCorrelationId());



        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }




    private void sendNotification(Namf_EventExposure_Subscribe namf_eventExposure_subscribe, String NOTIFICATION_URL) throws IOException, JSONException {


            URL url = new URL(NOTIFICATION_URL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();


            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");


            String notificationString = "\n\nSending Notification to " + namf_eventExposure_subscribe.getNotificationTargetAddress() + "/" +
                    namf_eventExposure_subscribe.getCorrelationId();

            System.out.println(notificationString);

            // For POST only - START
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = notificationString.getBytes("utf-8");

               // System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + notificationString);

                os.write(input, 0, input.length);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // For POST only - END

            int responseCode = con.getResponseCode();
            String responseMessage = con.getResponseMessage();
            System.out.println("POST Response Code :: " + responseCode);
              System.out.println("POST Response Message :: " + responseMessage);

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

