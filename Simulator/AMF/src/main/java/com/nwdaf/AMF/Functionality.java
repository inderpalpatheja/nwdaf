package com.nwdaf.AMF;

import org.json.JSONObject;
import static java.lang.System.out;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Functionality {


    BufferedReader reader;

    final String NWDAF = "http://localhost:8081/nnwdaf-eventssubscription/v1/";


    public String subscribe(int eventID, int notifMethod, int repPeriod, int ldLevel) throws Exception
    {
        String line;
        StringBuffer responseContent = new StringBuffer();

        URL url = new URL(NWDAF + "/subscriptions");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JSONObject json = new JSONObject();

        json.put("eventID", eventID);
        json.put("notificationURI", "http://localhost:8082/");
        json.put("notifMethod", notifMethod);
        json.put("repetitionLevel", repPeriod);
        json.put("loadLevelThreshold", ldLevel);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            out.println("Status: " + con.getResponseCode());
            out.println("Message: " + response.toString());
        }

        return con.getHeaderField("Location");
    }



    public void update(String subID, int eventID, int notifMethod, int repLevel, int ldLevel) throws Exception
    {

        String line;
        StringBuffer responseContent = new StringBuffer();

        URL url = new URL(subID);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JSONObject json = new JSONObject();

        json.put("eventID", eventID);
        json.put("notifMethod", notifMethod);
        json.put("repetitionLevel", repLevel);
        json.put("loadLevelThreshold", ldLevel);


        try(OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            out.println("Status: " + con.getResponseCode());
            out.println("Message: " + response.toString());
        }

    }


    public void unsubscribe(String subID) throws Exception
    {
        String line;
        StringBuffer responseContent = new StringBuffer();

        URL url = new URL(subID);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("DELETE");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        out.println("Status: " + con.getResponseCode());
    }

}
