package com.nwdaf.AMF;


import com.nwdaf.AMF.model.Namf_EventExposure.Namf_EventExposure_Subscribe;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.time.*;

import java.util.*;


import static org.springframework.http.HttpHeaders.USER_AGENT;
import static java.lang.System.out;

@RestController
public class AMFController extends Functionality {


    // @Autowired
    //  BuildProperties buildProperties;


    // testing HTTP2 [ Not working ]
    @RequestMapping("/testHttp2")
    public String testHttp2() {
        return "Http2 Check!";
    }

    // Post Method for HTTP for 8082
    @RequestMapping(method = RequestMethod.POST, value = "/notify")
    public void adddata(@RequestBody String string) throws Exception {
        System.out.println("\n\nReceived From NWDAF -" + string);


    }


 /*   @RequestMapping("/NWDAFJarDetails")
    public Object getNwdafAPIInformation() throws IOException {


        //  JarFile jf = new JarFile("/Users/sheetalkumar/Desktop/Demo3W/nwdaf/Simulator/AMF/target/Collector-0.0.1-SNAPSHOT.jar");
        //  ZipEntry manifest = jf.getEntry("META-INF/MANIFEST.MF");
        //  long manifestTime = manifest.getTime();
        //  Timestamp ts = new Timestamp(manifestTime);
        //  Date date = new Date(ts.getTime());
        //  return " Date - " + date;


        APIBuildInformation apiBuildInformation = new APIBuildInformation();


        LocalDate date = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault()).toLocalDate();

        LocalTime time = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault()).toLocalTime();


        apiBuildInformation.setAPI_VERSION(buildProperties.getVersion());
        apiBuildInformation.setAPI_NAME(buildProperties.getName());
        apiBuildInformation.setBUILD_DATE(date);
        apiBuildInformation.setBUILD_TIME(time);

        return apiBuildInformation;

    }*/


    @RequestMapping(method = RequestMethod.POST, value = "/Nnrf_NFManagement_NFStatusSubscribe/{correlationID}")
    public ResponseEntity<String> show(@RequestBody String response) throws JSONException, IOException {


        JSONObject json = new JSONObject(response);
        Namf_EventExposure_Subscribe obj = new Namf_EventExposure_Subscribe();

        obj.setCorrelationId(json.getString("correlationID"));
        obj.setNotificationTargetAddress(json.getString("notificationTargetAddress"));


        // Adding unSubCorrelationId into database;

        UUID unSubCorrelationId = UUID.randomUUID();

        // sendData(obj.getNotificationTargetAddress(), obj.getCorrelationId());

        String NOTIFICATOIN_URL = obj.getNotificationTargetAddress() + "/" + obj.getCorrelationId();

        // out.println(NOTIFICATOIN_URL);


        return new ResponseEntity<String>(String.valueOf(unSubCorrelationId), HttpStatus.OK);
    }


    // @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_notify/{correlationID}")


    @GetMapping("/updateCurrentLoadLevel/{correlationID}")
    private ResponseEntity<String> sendData(String notiTargetAddress, @PathVariable("correlationID") String correlationID) throws IOException, JSONException {

        notiTargetAddress = "http://localhost:8081/Nnrf_NFManagement_NFStatusNotify";
        //correlationID = "00987b27-9ec6-4834-a4ff-a777750eeb32";

        // NOTIFICATION URL = spring.AMF_NOTIFICATION.url = http://localhost:8081/Namf_EventExposure_Notify
        //   out.println("NotificaitonURL " + NOTIFICATION_URL);

        // String notiTargetAddress = "http://localhost:8081/Namf_EventExposure_Notify";

        String updated_URL = notiTargetAddress + "/" + correlationID;
        out.println("updated URl - " + updated_URL);
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

        json.put("currentLoadLevel", 50);
        json.put("correlationID", correlationID);

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
        //  return "Data send to " + updated_URL;
        return new ResponseEntity<String>("Send", HttpStatus.OK);
    }

}

