package com.nwdaf.AMF;

import com.nwdaf.AMF.model.NwdafEvent;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static java.lang.System.out;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Functionality {

    RestTemplate restTemplate;

    BufferedReader reader;

    final String NWDAF = "https://localhost:8081/nnwdaf-eventssubscription/v1";
    final String UNSUB = "https://localhost:8081/nnwdaf-eventssubscription/v1/subscriptions";


    @Autowired
    public Functionality()
    { restTemplate = new RestTemplate(); }


    public String subscribe(NwdafEvent event) throws Exception {

        String URL = NWDAF + "/subscriptions";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        JSONObject json = EventData.getData(event);

        HttpEntity<String> requestEntity = new HttpEntity<>(json.toString(), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);

        out.println("Response Code: " + responseEntity.getStatusCode());

        HttpHeaders responseHeaders = responseEntity.getHeaders();

        return responseHeaders.getLocation().toString();
    }


    public void update(String subID, Integer eventID, Integer notifMethod, Integer repLevel, Integer ldLevel) throws Exception {

        String line;
        StringBuffer responseContent = new StringBuffer();

        URL url = new URL(subID);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JSONObject json = new JSONObject();

        json.put("eventID", eventID);
        json.put("notifMethod", notifMethod);
        json.put("repetitionPeriod", repLevel);
        json.put("loadLevelThreshold", ldLevel);


        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            // out.println("Status: " + con.getResponseCode());
            //  out.println("Message: " + response.toString());
        } finally {
            con.disconnect();
        }
    }


    public void unsubscribe(String subscriptionId) {

        String URL = UNSUB + "/" + subscriptionId;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.DELETE, requestEntity, String.class);

        out.println("Response Code: " + responseEntity.getStatusCode());
    }



    /*

    public String subscribe(NwdafEvent event) throws Exception {

        String line;
        StringBuffer responseContent = new StringBuffer();

        URL url = new URL(NWDAF + "/subscriptions");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JSONObject json = EventData.getData(event);


        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            //  out.println("Status: " + con.getResponseCode());
            //  out.println("Message: " + response.toString());
        } finally {
            con.disconnect();
        }

        return con.getHeaderField("Location");
    }





    public void unsubscribe(String subscriptionID) throws Exception {
        // out.println("SubscriptionID to be deleted - " + subID);
        String line;
        // out.println("unSubscribe URL" + notificationURI + "/" + subID);

        String updatedURL = UNSUB + "/" + subscriptionID;
        out.println("updated URL - " + updatedURL);

        StringBuffer responseContent = new StringBuffer();

        URL url = new URL(updatedURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("DELETE");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        out.println("Status: " + con.getResponseCode());
        out.println("message:" + con.getResponseMessage());

        if(con != null)
        { con.disconnect(); }
    }



     */

}
