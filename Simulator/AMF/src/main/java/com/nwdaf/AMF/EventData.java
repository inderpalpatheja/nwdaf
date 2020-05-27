package com.nwdaf.AMF;

import com.nwdaf.AMF.model.*;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;


public class EventData {

    final static String notificationURI = "https://localhost:8082/notify";
    final static String[] snssais = {"AMF", "SMF", "PCF"};
    static Random random = new Random();


    public static JSONObject getData(NwdafEvent event) throws JSONException
    {

        switch(event)
        {
            case LOAD_LEVEL_INFORMATION: return getSliceLoadLevelData(1);

            case QOS_SUSTAINABILITY: return getQosSustainabilityData();

            case UE_MOBILITY: return getUeMobilityData();

            case SERVICE_EXPERIENCE: return getServiceExperienceData();

            case NETWORK_PERFORMANCE: return getNetworkPerformanceData();

            case USER_DATA_CONGESTION: return getUserDataCongestion();

            case ABNORMAL_BEHAVIOUR: return getAbnormalBehaviour();

            case UE_COMM: return getUeCommData();

            case NF_LOAD: return getNfLoadData();
        }

        return null;
    }



    public static JSONObject getSliceLoadLevelData(Integer notifMethod) throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventSubscriptions = new JSONArray();

        json.put("notificationURI", notificationURI);

        JSONObject event_entry = new JSONObject();

        event_entry.put("event", NwdafEvent.LOAD_LEVEL_INFORMATION.ordinal());
        event_entry.put("notificationMethod", NotificationMethod.THRESHOLD.ordinal());
        event_entry.put("loadLevelThreshold", 20 + random.nextInt(10));

        JSONArray snssais = new JSONArray();

        JSONObject snssai_entry = new JSONObject();
        snssai_entry.put("sst", random.nextInt(100));
        snssai_entry.put("sd", RandomStringUtils.randomAlphanumeric(3));

        snssais.put(snssai_entry);

        event_entry.put("snssais", snssais);
        eventSubscriptions.put(event_entry);

        json.put("eventSubscriptions", eventSubscriptions);

        return json;
    }


    public static JSONObject getQosSustainabilityData() throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventSubscriptions = new JSONArray();

        json.put("notificationURI", notificationURI);

        JSONObject event_entry = new JSONObject();

        event_entry.put("event", NwdafEvent.QOS_SUSTAINABILITY.ordinal());
        event_entry.put("notificationMethod", NotificationMethod.THRESHOLD.ordinal());
        event_entry.put("loadLevelThreshold", 20 + random.nextInt(10));

        JSONObject networkArea = new JSONObject();
        JSONArray tais = new JSONArray();

        JSONObject tai = new JSONObject();

        JSONObject plmnId = new JSONObject();
        plmnId.put("mcc", 100 + random.nextInt(900));
        plmnId.put("mnc", 10 + random.nextInt(990));

        tai.put("plmnId", plmnId);
        tai.put("tac", RandomStringUtils.randomAlphanumeric(4));
        tais.put(tai);

        networkArea.put("tais", tais);

        JSONObject qosRequ = new JSONObject();
        qosRequ.put("5Qi", random.nextInt(100));


        JSONArray snssais = new JSONArray();

        JSONObject snssai_entry = new JSONObject();
        snssai_entry.put("sst", random.nextInt(100));
        snssai_entry.put("sd", RandomStringUtils.randomAlphanumeric(3));

        snssais.put(snssai_entry);


        event_entry.put("networkArea", networkArea);
        event_entry.put("qosRequ", qosRequ);
        event_entry.put("snssais", snssais);


        int randomize = random.nextInt(2);

        if(randomize == 0)
        {
            JSONArray qosFlowRetThrds = new JSONArray();
            qosFlowRetThrds.put(20 + random.nextInt(10));

            event_entry.put("qosFlowRetThrds", qosFlowRetThrds);
        }

        else
        {
            JSONArray ranUeThrouThrds = new JSONArray();
            ranUeThrouThrds.put(20 + random.nextInt(10));

            event_entry.put("ranUeThrouThrds", ranUeThrouThrds);
        }


        eventSubscriptions.put(event_entry);

        json.put("eventSubscriptions", eventSubscriptions);

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

        json.put("event", NwdafEvent.UE_MOBILITY.ordinal());
        json.put("notificationURI", notificationURI);
        json.put("notifMethod", 0);
        json.put("repetitionPeriod", 10 + random.nextInt(10));
        json.put("supi", RandomStringUtils.randomNumeric(15));

        return json;
    }



    public static JSONObject getServiceExperienceData() throws JSONException
    {

        JSONObject json = new JSONObject();
        JSONArray eventSubscriptions = new JSONArray();

        json.put("notificationURI", notificationURI);

        JSONObject event_entry = new JSONObject();

        event_entry.put("event", NwdafEvent.SERVICE_EXPERIENCE.ordinal());
        event_entry.put("notificationMethod", NotificationMethod.THRESHOLD.ordinal());
        event_entry.put("loadLevelThreshold", 20 + random.nextInt(10));

        JSONObject tgtUe = new JSONObject();
        tgtUe.put("supi", RandomStringUtils.randomNumeric(15));

        JSONArray snssais = new JSONArray();

        JSONObject snssai_entry = new JSONObject();
        snssai_entry.put("sst", random.nextInt(100));
        snssai_entry.put("sd", RandomStringUtils.randomAlphanumeric(3));

        snssais.put(snssai_entry);

        event_entry.put("tgtUe", tgtUe);
        event_entry.put("snssais", snssais);
        eventSubscriptions.put(event_entry);

        json.put("eventSubscriptions", eventSubscriptions);

        return json;
    }


    public static JSONObject getNetworkPerformanceData() throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventSubscriptions = new JSONArray();

        json.put("notificationURI", notificationURI);

        JSONObject event_entry = new JSONObject();

        event_entry.put("event", NwdafEvent.NETWORK_PERFORMANCE.ordinal());
        event_entry.put("notificationMethod", NotificationMethod.THRESHOLD.ordinal());
        event_entry.put("loadLevelThreshold", 20 + random.nextInt(10));

        JSONObject tgtUe = new JSONObject();
        tgtUe.put("supi", RandomStringUtils.randomNumeric(15));


        JSONArray nwPerfRequs = new JSONArray();

        JSONObject nwPerfRequs_entry = new JSONObject();
        Integer nwPerfType = random.nextInt(NetworkPerfType.values().length);

        nwPerfRequs_entry.put("nwPerfType", nwPerfType);

        if(nwPerfType == NetworkPerfType.GNB_ACTIVE_RATIO.ordinal() || nwPerfType == NetworkPerfType.SESS_SUCC_RATIO.ordinal() || nwPerfType == NetworkPerfType.HO_SUCC_RATIO.ordinal())
        { nwPerfRequs_entry.put("relativeRatio", 20 + random.nextInt(30)); }

        else
        { nwPerfRequs_entry.put("absoluteNum", 20 + random.nextInt(30)); }

        nwPerfRequs.put(nwPerfRequs_entry);

        event_entry.put("tgtUe", tgtUe);
        event_entry.put("nwPerfRequs", nwPerfRequs);

        eventSubscriptions.put(event_entry);

        json.put("eventSubscriptions", eventSubscriptions);

        return json;
    }


    public static JSONObject getUserDataCongestion() throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventSubscriptions = new JSONArray();

        json.put("notificationURI", notificationURI);

        JSONObject event_entry = new JSONObject();

        event_entry.put("event", NwdafEvent.USER_DATA_CONGESTION.ordinal());
        event_entry.put("notificationMethod", NotificationMethod.THRESHOLD.ordinal());
        event_entry.put("loadLevelThreshold", 20 + random.nextInt(10));

        JSONObject tgtUe = new JSONObject();
        tgtUe.put("supi", RandomStringUtils.randomNumeric(15));

        JSONArray congThresholds = new JSONArray();
        JSONObject congThresholds_entry = new JSONObject();
        congThresholds_entry.put("congLevel", 20 + random.nextInt(10));

        congThresholds.put(congThresholds_entry);

        event_entry.put("tgtUe", tgtUe);
        event_entry.put("congType", random.nextInt(CongestionType.values().length));
        event_entry.put("congThresholds", congThresholds);

        eventSubscriptions.put(event_entry);

        json.put("eventSubscriptions", eventSubscriptions);

        return json;
    }


    public static JSONObject getAbnormalBehaviour() throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventSubscriptions = new JSONArray();

        json.put("notificationURI", notificationURI);

        JSONObject event_entry = new JSONObject();

        event_entry.put("event", NwdafEvent.ABNORMAL_BEHAVIOUR.ordinal());
        event_entry.put("notificationMethod", NotificationMethod.THRESHOLD.ordinal());
        event_entry.put("loadLevelThreshold", 20 + random.nextInt(10));

        JSONObject tgtUe = new JSONObject();
        tgtUe.put("supi", RandomStringUtils.randomNumeric(15));

        JSONArray excepRequs = new JSONArray();

        JSONObject excepRequs_entry = new JSONObject();
        excepRequs_entry.put("excepId", random.nextInt(ExceptionId.values().length));
        excepRequs_entry.put("excepLevel", 20 + random.nextInt(10));

        excepRequs.put(excepRequs_entry);

        event_entry.put("tgtUe", tgtUe);
        event_entry.put("excepRequs", excepRequs);

        eventSubscriptions.put(event_entry);

        json.put("eventSubscriptions", eventSubscriptions);

        return json;

    }



    public static JSONObject getUeCommData() throws JSONException {


        JSONObject json = new JSONObject();
        JSONArray eventSubscriptions = new JSONArray();

        json.put("notificationURI", notificationURI);

        JSONObject event_entry = new JSONObject();

        event_entry.put("event", NwdafEvent.UE_COMM.ordinal());
        event_entry.put("notificationMethod", NotificationMethod.THRESHOLD.ordinal());
        event_entry.put("loadLevelThreshold", 20 + random.nextInt(10));

        JSONObject tgtUe = new JSONObject();
        tgtUe.put("supi", RandomStringUtils.randomNumeric(15));

        event_entry.put("tgtUe", tgtUe);
        event_entry.put("maxAnaEntry", 1);

        eventSubscriptions.put(event_entry);

        json.put("eventSubscriptions", eventSubscriptions);

        return json;

    }


    public static JSONObject getNfLoadData() throws JSONException {


        JSONObject json = new JSONObject();
        JSONArray eventSubscriptions = new JSONArray();

        json.put("notificationURI", notificationURI);

        JSONObject event_entry = new JSONObject();

        event_entry.put("event", NwdafEvent.NF_LOAD.ordinal());


        JSONArray nfLoadLvlThds = new JSONArray();
        JSONObject nfLoadLvlThds_entry = new JSONObject();

        nfLoadLvlThds_entry.put("nfLoadLevel", 20 + random.nextInt(20));
        nfLoadLvlThds_entry.put("nfCpuUsage", 20 + random.nextInt(20));
        nfLoadLvlThds_entry.put("nfMemoryUsage", 20 + random.nextInt(20));
        nfLoadLvlThds_entry.put("nfStorageUsage", 20 + random.nextInt(20));

        nfLoadLvlThds.put(nfLoadLvlThds_entry);

        event_entry.put("nfLoadLvlThds", nfLoadLvlThds);


        if(random.nextInt(2) == 0)
        {
            JSONArray nfTypes = new JSONArray();
            nfTypes.put(random.nextInt(NFType.values().length));

            JSONArray nfInstanceIds = new JSONArray();
            nfInstanceIds.put(UUID.randomUUID().toString());

            event_entry.put("nfTypes", nfTypes);
            event_entry.put("nfInstanceIds", nfInstanceIds);
        }

        else
        {
            JSONObject tgtUe = new JSONObject();
            tgtUe.put("supi", RandomStringUtils.randomNumeric(15));


            JSONArray snssais = new JSONArray();

            JSONObject snssai_entry = new JSONObject();
            snssai_entry.put("sst", random.nextInt(100));
            snssai_entry.put("sd", RandomStringUtils.randomAlphanumeric(3));

            snssais.put(snssai_entry);


            event_entry.put("tgtUe", tgtUe);
            event_entry.put("snssais", snssais);
        }


        eventSubscriptions.put(event_entry);

        json.put("eventSubscriptions", eventSubscriptions);

        return json;

    }

}
