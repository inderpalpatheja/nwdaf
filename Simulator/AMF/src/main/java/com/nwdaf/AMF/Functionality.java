package com.nwdaf.AMF;

import org.json.JSONObject;

import static java.lang.System.out;
import static java.lang.System.setOut;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Functionality {


    BufferedReader reader;

    final String NWDAF = "http://localhost:8081/nnwdaf-eventssubscription/v1";


    public String subscribe(Integer eventID,
                            String notificatoinURI,
                            String snssais,
                            Integer notifMethod,
                            Integer repPeriod,
                            String supi,
                            Integer ldLevel) throws Exception {
        String line;
        StringBuffer responseContent = new StringBuffer();

        URL url = new URL(NWDAF + "/subscriptions");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JSONObject json = new JSONObject();

        json.put("eventID", eventID);
        json.put("notificationURI", notificatoinURI);
        json.put("snssais", snssais);
        json.put("notifMethod", notifMethod);
        json.put("repetitionPeriod", repPeriod);
        json.put("loadLevelThreshold", ldLevel);
        json.put("supi", supi);

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


    public void unsubscribe(String notificationURI, String subID) throws Exception {
        // out.println("SubscriptionID to be deleted - " + subID);
        String line;
        // out.println("unSubscribe URL" + notificationURI + "/" + subID);

        String updatedURL = notificationURI + "/" + subID;
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


}
