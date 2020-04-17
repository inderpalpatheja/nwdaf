package com.nwdaf.AMF;

import com.nwdaf.AMF.model.EventID;
import com.nwdaf.AMF.model.NotificationMethod;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class EventData {

    final static String notificationURI = "http://localhost:8082/notify";
    final static String[] snssais = {"AMF", "SMF", "PCF"};
    static Random random = new Random();


    public static JSONObject getData(EventID eventID) throws JSONException
    {

        switch(eventID)
        {
            case LOAD_LEVEL_INFORMATION: return getSliceLoadLevelData(1);

            case QOS_SUSTAINABILITY: return getQosSustainabilityData();

            case UE_MOBILITY: return getUeMobilityData();

        }

        return null;
    }



    public static JSONObject getSliceLoadLevelData(Integer notifMethod) throws JSONException
    {
        JSONObject json = new JSONObject();

        json.put("eventID", EventID.LOAD_LEVEL_INFORMATION.ordinal());
        json.put("notificationURI", notificationURI);
        json.put("snssais", snssais[random.nextInt(snssais.length)]);
        json.put("notifMethod", notifMethod);

        if(notifMethod == NotificationMethod.PERIODIC.ordinal())
        { json.put("repetitionPeriod", 20 + random.nextInt(20)); }

        else
        { json.put("loadLevelThreshold", 20 + random.nextInt(60)); }

        return json;
    }


    public static JSONObject getQosSustainabilityData() throws JSONException
    {
        JSONObject json = new JSONObject();

        json.put("eventID", EventID.QOS_SUSTAINABILITY.ordinal());
        json.put("notificationURI", notificationURI);
        json.put("snssais", snssais[random.nextInt(snssais.length)]);
        json.put("5Qi", random.nextInt(256));
        json.put("mcc", String.valueOf(100 + random.nextInt(900)));
        json.put("mnc", String.valueOf(10 + random.nextInt(990)));
        json.put("tac", RandomStringUtils.randomAlphanumeric(6).toUpperCase());

        int thresholdType = random.nextInt(2);

        if(thresholdType == 0)
        { json.put("ranUeThroughputThreshold", 20 + random.nextInt(60)); }

        else
        { json.put("qosFlowRetainThreshold", 20 + random.nextInt(60)); }

        return json;
    }


    public static JSONObject getUeMobilityData() throws JSONException
    {
        //  System.out.println("Subscribers Count - " + subList);
       // int MCC = random.nextInt(900) + 100;
        // System.out.println("MCC - " + MCC);
       // int MNC = random.nextInt(900) + 100;
        // System.out.println("MNC - " + MNC);
       // int IMSI = random.nextInt(900000000) + 100000000;
        // System.out.println("IMSI" + IMSI);
       // String supi = String.valueOf(MCC) + String.valueOf(MNC) + String.valueOf(IMSI);


        JSONObject json = new JSONObject();

        json.put("eventID", EventID.UE_MOBILITY.ordinal());
        json.put("notificationURI", notificationURI);
        json.put("notifMethod", 0);
        json.put("repetitionPeriod", 10 + random.nextInt(10));
        json.put("supi", RandomStringUtils.randomNumeric(7));

        return json;
    }


}
