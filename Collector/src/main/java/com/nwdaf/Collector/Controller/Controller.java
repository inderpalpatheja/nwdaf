package com.nwdaf.Collector.Controller;


import com.example.Collector.model.Nsmf_EventExposure.Nsmf_EventExposure_Notify;
import com.example.Collector.model.Nsmf_EventExposure.Nsmf_EventExposure_Subscribe;
import com.example.Collector.model.Nsmf_EventExposure.Nsmf_EventExposure_UnSubscribe;

import com.nwdaf.Collector.Repository.CollectorRepository;
import com.nwdaf.Collector.Repository.SubscriptionRepository;
import com.nwdaf.Collector.model.CollectorDataModel;
import com.nwdaf.Collector.model.Namf_EventExposure.Namf_EventExposure_Notify;
import com.nwdaf.Collector.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import com.nwdaf.Collector.model.Namf_EventExposure.Namf_EventExposure_UnSubscribe;
import com.nwdaf.Collector.model.nestedModel.EventReportingInformationInformation;
import com.nwdaf.Collector.model.nestedModel.NotificationEventIdModel;
import com.nwdaf.Collector.model.nestedModel.TargetEventReporting;
import com.sun.deploy.net.HttpResponse;
import org.hibernate.validator.constraints.pl.REGON;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import java.io.IOException;
import java.net.URI;


import static org.springframework.http.HttpHeaders.USER_AGENT;

@RestController
public class Controller {


    JdbcTemplate jdbcTemplate;


    private static final String POST_URL = "http://localhost:8082/Namf_EventExposure_Subscribe";

    @Autowired
    com.nwdaf.Collector.Repository.CollectorRepository CollectorRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;


    @RequestMapping("/s1")
    private static void sendPOST(@RequestBody Namf_EventExposure_Subscribe namf_eventExposure_subscribe) throws IOException, JSONException {

       // readDataFromFile();


        URL obj = new URL(POST_URL);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        // Converting json into String
        JSONObject json = new JSONObject();

        json.put("nfId", namf_eventExposure_subscribe.getNfId());
        json.put("eventId", namf_eventExposure_subscribe.getEventId());
        // json.put("targetEventReporting", namf_eventExposure_subscribe.getTargetEventReporting());
        json.put("notificationTargetAddress", namf_eventExposure_subscribe.getNotificationTargetAddress());
        json.put("unSubCorrelation", namf_eventExposure_subscribe.getUnSubCorrelationId());
        json.put("correlationId", namf_eventExposure_subscribe.getCorrelationId());

        System.out.println("\n\n\n");
        System.out.println(json.toString());

        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            System.out.println("\n\n");
            System.out.println("Sending NotificationTargetAddress to [ Collector -> AMF ] " + json.toString());

            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();
        String responseMessage = con.getResponseMessage();
        System.out.println("\n\n");
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

    // Working URL Reading From File.!
  /*  private static void readDataFromFile() {
        try {
            File myObj = new File("/Users/sheetalkumar/Desktop/Collector/src/main/resources/File/URL_DETAILS.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }*/



    // Accepting Notification.
    @RequestMapping("/Namf_EventExposure_Notify")
    public void acceptingNotification(@RequestBody String notificationString) {
        System.out.println(notificationString.toString());
    }


}
