package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.CustomData.UserDataCongestion.CongestionType;
import com.nwdaf.Analytics.Model.NotificationFormat.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationPayload {

    static SimpleDateFormat sim = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");


    // eventID: LOAD_LEVEL_INFORMATION
    public static JSONObject getLoadLevelInformationPayload(LoadLevelInformationNotification ldLevelNotifyData) throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();

        json.put("subscriptionId", ldLevelNotifyData.getSubscriptionID());

        JSONObject ldLevelData = new JSONObject();

        JSONObject sliceLoadLevelInfo = new JSONObject();

        JSONArray snssais_arr = new JSONArray();
        snssais_arr.put(ldLevelNotifyData.getSnssais());

        sliceLoadLevelInfo.put("loadLevelInformation", ldLevelNotifyData.getCurrentLoadLevel());
        sliceLoadLevelInfo.put("snssais", snssais_arr);

        ldLevelData.put("event", EventID.LOAD_LEVEL_INFORMATION.toString());
        ldLevelData.put("sliceLoadLevelInfo", sliceLoadLevelInfo);

        eventNotifications.put(ldLevelData);

        json.put("eventNotifications", eventNotifications);

        return json;
    }




    // eventID: QOS_SUSTAINABILITY
    public static JSONObject getQosSustainabilityPayload(QosSustainabilityNotification qosNotifyData) throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();

        json.put("subscriptionId", qosNotifyData.getSubscriptionID());

        JSONObject qosPayload = new JSONObject();

        String plmnID_info[] = qosNotifyData.getPlmnID().split("-");

        String mcc = plmnID_info[0];
        String mnc = plmnID_info[1];

        JSONArray qosSustainInfos = new JSONArray();

        JSONObject qosInfo = new JSONObject();
        JSONObject areaInfo = new JSONObject();
        JSONObject plmnID_json = new JSONObject();

        plmnID_json.put("mcc", mcc);
        plmnID_json.put("mnc", mnc);

        areaInfo.put("plmnId", plmnID_json);
        areaInfo.put("tac", qosNotifyData.getTac());

        qosInfo.put("areaInfo", areaInfo);


        JSONArray arrayThreshold = new JSONArray();
        arrayThreshold.put(qosNotifyData.getThreshold());

        if(qosNotifyData.getThresholdType() == QosType.RAN_UE_THROUGHPUT)
        { qosInfo.put("crossedRanUeThroughputThreshold", arrayThreshold); }

        else
        { qosInfo.put("crossedQosFlowRetainThreshold", arrayThreshold); }

        qosSustainInfos.put(qosInfo);

        qosPayload.put("event", EventID.QOS_SUSTAINABILITY.toString());
        qosPayload.put("qosSustainInfos", qosSustainInfos);

        eventNotifications.put(qosPayload);

        json.put("eventNotifications", eventNotifications);

        return json;
    }



    // eventID: SERVICE_EXPERIENCE
    public static JSONObject getServiceExperiencePayload(ServiceExperienceNotification svcExpNotifyData) throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();
        JSONObject serviceExperienceEvent = new JSONObject();

        JSONArray svcExps = new JSONArray();
        JSONObject svcExpEntry = new JSONObject();

        JSONObject svcExperience = new JSONObject();

        svcExperience.put("mos", svcExpNotifyData.getSvcExpInfo().getMos());
        svcExperience.put("upperRange", svcExpNotifyData.getSvcExpInfo().getUpperRange());
        svcExperience.put("lowerRange", svcExpNotifyData.getSvcExpInfo().getLowerRange());

        svcExpEntry.put("svcExprc", svcExperience);
        svcExpEntry.put("snssais", svcExpNotifyData.getSnssais());
        svcExpEntry.put("supi", svcExpNotifyData.getSupi());

        svcExps.put(svcExpEntry);

        serviceExperienceEvent.put("event", EventID.SERVICE_EXPERIENCE.toString());
        serviceExperienceEvent.put("svcExps", svcExps);

        eventNotifications.put(serviceExperienceEvent);

        json.put("subscriptionId", svcExpNotifyData.getSubscriptionID());
        json.put("eventNotifications", eventNotifications);

        return json;
    }



    public static JSONObject getNetworkPerformancePayload(NetworkPerformanceNotification nwPerfNotifyData, NetworkPerfThreshold threshold) throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();
        JSONObject networkPerfEvent = new JSONObject();

        JSONObject networkPerfEntry = new JSONObject();

        /*
        JSONObject tais = new JSONObject();
        JSONArray taiArray = new JSONArray();
        JSONObject tai_info = new JSONObject();

        JSONObject areaInfo = new JSONObject();
        areaInfo.put("mcc", nwPerfNotifyData.getMcc());
        areaInfo.put("mnc", nwPerfNotifyData.getMnc());

        tai_info.put("plmnId", areaInfo);
        tai_info.put("tac", nwPerfNotifyData.getTac());

        taiArray.put(tai_info);
        tais.put("tais", taiArray);

        networkPerfEntry.put("networkArea", tais);  */

        networkPerfEntry.put("nwPerfType", NetworkPerfType.values()[nwPerfNotifyData.getNwPerfType()].toString());

        if(threshold == NetworkPerfThreshold.RELATIVE_RATIO)
        { networkPerfEntry.put("relativeRatio", nwPerfNotifyData.getRelativeRatio()); }

        else if(threshold == NetworkPerfThreshold.ABSOLUTE_NUM)
        { networkPerfEntry.put("absoluteNum", nwPerfNotifyData.getAbsoluteNum()); }

        JSONArray networkPerfArray = new JSONArray();
        networkPerfArray.put(networkPerfEntry);

        networkPerfEvent.put("event", EventID.NETWORK_PERFORMANCE.toString());
        networkPerfEvent.put("nwPerfs", networkPerfArray);

        eventNotifications.put(networkPerfEvent);

        json.put("subscriptionId", nwPerfNotifyData.getSubscriptionID());
        json.put("eventNotifications", eventNotifications);

        return json;
    }




    // eventID: USER_DATA_CONGESTION
    public static JSONObject getUserDataCongestionPayload(UserDataCongestionNotification usrDataCongNotifyData) throws JSONException {

        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();

        JSONObject userDataCongInfos = new JSONObject();
        JSONObject userDataCongInfosEntry = new JSONObject();
        JSONArray userDataCongInfosArray = new JSONArray();


        JSONObject tais = new JSONObject();
        JSONArray taisArray = new JSONArray();

        JSONObject taiEntry = new JSONObject();

        JSONObject plmnID = new JSONObject();
        plmnID.put("mcc", usrDataCongNotifyData.getTai().getPlmnID().getMcc());
        plmnID.put("mnc", usrDataCongNotifyData.getTai().getPlmnID().getMnc());

        taiEntry.put("plmnId", plmnID);
        taiEntry.put("tac", usrDataCongNotifyData.getTai().getTac());

        taisArray.put(taiEntry);
        tais.put("tais", taisArray);

        JSONArray networkArea = new JSONArray();
        networkArea.put(tais);


        JSONObject timeIntev = new JSONObject();
        timeIntev.put("startTime", sim.format(new Date()));
        timeIntev.put("stopTime", sim.format(new Date()));


        JSONObject congestionInfo = new JSONObject();
        congestionInfo.put("congType", CongestionType.values()[usrDataCongNotifyData.getCongType()].toString());
        congestionInfo.put("timeIntev", timeIntev);

        JSONObject nsi = new JSONObject();
        nsi.put("congLevel", usrDataCongNotifyData.getCongLevel());

        userDataCongInfosEntry.put("networkArea", networkArea);
        userDataCongInfosEntry.put("congestionInfo", congestionInfo);
        userDataCongInfosEntry.put("nsi", nsi);

        userDataCongInfosArray.put(userDataCongInfosEntry);

        userDataCongInfos.put("event", EventID.USER_DATA_CONGESTION.toString());
        userDataCongInfos.put("userDataCongInfos",userDataCongInfosArray);

        eventNotifications.put(userDataCongInfos);

        json.put("subscriptionId", usrDataCongNotifyData.getSubscriptionID());
        json.put("eventNotifications", eventNotifications);

        return json;
    }
}
