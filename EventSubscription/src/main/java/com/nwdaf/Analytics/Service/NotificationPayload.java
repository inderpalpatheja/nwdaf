package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.AdditionalData.FlowDirection;
import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.ExceptionId;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.CustomData.NfLoad.NFType;
import com.nwdaf.Analytics.Model.CustomData.NfLoad.NfThresholdType;
import com.nwdaf.Analytics.Model.CustomData.QosSustainability.QosThresholdType;
import com.nwdaf.Analytics.Model.CustomData.UserDataCongestion.CongestionType;
import com.nwdaf.Analytics.Model.NotificationFormat.*;
import com.nwdaf.Analytics.Model.NwdafEvent;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NotificationPayload {

    static SimpleDateFormat sim = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    static Random random = new Random();
    static String flowDir[] = {"IN", "OUT"};


    // eventID: LOAD_LEVEL_INFORMATION
    public static JSONObject getLoadLevelInformationPayload(LoadLevelInformationNotification ldLevelNotifyData) throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();

        json.put("subscriptionId", ldLevelNotifyData.getSubscriptionId());

        JSONObject ldLevelData = new JSONObject();

        JSONObject sliceLoadLevelInfo = new JSONObject();

        JSONArray snssais_arr = new JSONArray();
        snssais_arr.put(ldLevelNotifyData.getSnssai());

        sliceLoadLevelInfo.put("loadLevelInformation", ldLevelNotifyData.getCurrentLoadLevel());
        sliceLoadLevelInfo.put("snssais", snssais_arr);

        ldLevelData.put("event", NwdafEvent.LOAD_LEVEL_INFORMATION.toString());
        ldLevelData.put("sliceLoadLevelInfo", sliceLoadLevelInfo);

        eventNotifications.put(ldLevelData);

        json.put("eventNotifications", eventNotifications);

        return json;
    }




    // eventID: QOS_SUSTAINABILITY
    public static JSONObject getQosSustainabilityPayload(QosSustainabilityNotification qosNotifyData, QosThresholdType qosThresholdType) throws JSONException
    {
        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();


        JSONObject qosSustainInfos = new JSONObject();
        JSONArray qosSustainInfosArray = new JSONArray();
        JSONObject qosSustainInfos_entry = new JSONObject();


        String areaInfo_data[] = qosNotifyData.getTai().split("-");

        String mcc = areaInfo_data[0];
        String mnc = areaInfo_data[1];
        String tac = areaInfo_data[2];

        JSONObject areaInfo = new JSONObject();

        JSONArray tais = new JSONArray();

        JSONObject tai_entry = new JSONObject();

        JSONObject plmnId = new JSONObject();
        plmnId.put("mcc", mcc);
        plmnId.put("mnc", mnc);

        tai_entry.put("plmnId", plmnId);
        tai_entry.put("tac", tac);

        tais.put(tai_entry);
        areaInfo.put("tais", tais);


        qosSustainInfos_entry.put("areaInfo", areaInfo);


        switch(qosThresholdType)
        {
            case QOS_FLOW_RETAIN: JSONObject qosFlowRetThd = new JSONObject();
                                  qosFlowRetThd.put("relFlowNum", qosNotifyData.getQosFlowRet());
                                  qosFlowRetThd.put("relTimeUnit", qosNotifyData.getRelTimeUnit());

                                  qosSustainInfos_entry.put("qosFlowRetThd", qosFlowRetThd);
                                  break;



            case RAN_UE_THROUGHPUT: qosSustainInfos_entry.put("ranUeThrouThd", qosNotifyData.getRanUeThrou());
                                    break;
        }

        qosSustainInfosArray.put(qosSustainInfos_entry);

        qosSustainInfos.put("event", NwdafEvent.QOS_SUSTAINABILITY.toString());
        qosSustainInfos.put("qosSustainInfos", qosSustainInfosArray);

        eventNotifications.put(qosSustainInfos);

        json.put("subscriptionId", qosNotifyData.getSubscriptionId());
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
        svcExpEntry.put("snssais", svcExpNotifyData.getSnssai());
        svcExpEntry.put("supi", svcExpNotifyData.getSupi());

        svcExps.put(svcExpEntry);

        serviceExperienceEvent.put("event", NwdafEvent.SERVICE_EXPERIENCE.toString());
        serviceExperienceEvent.put("svcExps", svcExps);

        eventNotifications.put(serviceExperienceEvent);

        json.put("subscriptionId", svcExpNotifyData.getSubscriptionId());
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

        networkPerfEvent.put("event", NwdafEvent.NETWORK_PERFORMANCE.toString());
        networkPerfEvent.put("nwPerfs", networkPerfArray);

        eventNotifications.put(networkPerfEvent);

        json.put("subscriptionId", nwPerfNotifyData.getSubscriptionId());
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
        plmnID.put("mcc", usrDataCongNotifyData.getTai().getPlmnId().getMcc());
        plmnID.put("mnc", usrDataCongNotifyData.getTai().getPlmnId().getMnc());

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

        userDataCongInfos.put("event", NwdafEvent.USER_DATA_CONGESTION.toString());
        userDataCongInfos.put("userDataCongInfos",userDataCongInfosArray);

        eventNotifications.put(userDataCongInfos);

        json.put("subscriptionId", usrDataCongNotifyData.getSubscriptionId());
        json.put("eventNotifications", eventNotifications);

        return json;
    }



    public static JSONObject getAbnormalBehaviourPayload(AbnormalBehaviourNotification abnorBehavrNotifyData) throws JSONException {

        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();


        JSONObject abnorBehavrs = new JSONObject();
        JSONArray abnorBehavrsArray = new JSONArray();
        JSONObject abnorBehavrs_entry = new JSONObject();


        JSONArray supiArray = new JSONArray();

        for(String supi: abnorBehavrNotifyData.getSupi())
        { supiArray.put(supi); }


        JSONObject exceps = new JSONObject();

        ExceptionId excepId = abnorBehavrNotifyData.getExceps().getExcepId();
        exceps.put("excepId", excepId.toString());
        exceps.put("excepLevel", abnorBehavrNotifyData.getExceps().getExcepLevel());


        JSONObject addtMeasInfo = new JSONObject();

        if(excepId == ExceptionId.UNEXPECTED_UE_LOCATION)
        {
            JSONObject areaInfo = new JSONObject();
            JSONArray taisArray = new JSONArray();
            JSONObject tai_entry = new JSONObject();

            JSONObject plmnId = new JSONObject();
            plmnId.put("mcc", String.valueOf(100 + random.nextInt(900)));
            plmnId.put("mnc", String.valueOf(10 + random.nextInt(990)));

            tai_entry.put("plmnId", plmnId);
            tai_entry.put("tac", RandomStringUtils.randomAlphanumeric(5));

            taisArray.put(tai_entry);

            areaInfo.put("tais", taisArray);
            addtMeasInfo.put("unexpLoc", areaInfo);
        }


        else if(excepId == ExceptionId.UNEXPECTED_LONG_LIVE_FLOW || excepId == ExceptionId.UNEXPECTED_LARGE_RATE_FLOW)
        {
            JSONObject unexpFlowTep = new JSONObject();

            JSONObject ipTrafficFilter = new JSONObject();
            JSONArray flowDescriptions = new JSONArray();

            ipTrafficFilter.put("flowId", FlowDirection.values()[random.nextInt(FlowDirection.values().length)].ordinal());

            for(int i = 0; i < 1 + random.nextInt(5); i++)
            { flowDescriptions.put(flowDir[random.nextInt(flowDir.length)]); }

            ipTrafficFilter.put("flowDescriptions", flowDescriptions);

            unexpFlowTep.put("ipTrafficFilter", ipTrafficFilter);
            addtMeasInfo.put("unexpFlowTep", unexpFlowTep);
        }



        else if(excepId == ExceptionId.UNEXPECTED_WAKEUP)
        { addtMeasInfo.put("unexpWake", sim.format(new Date())); }


        else if(excepId == ExceptionId.SUSPICION_OF_DDOS_ATTACK || excepId == ExceptionId.WRONG_DESTINATION_ADDRESS)
        {
            JSONObject ipv4Addrs = new JSONObject();
            JSONArray ipv4AddrsArray = new JSONArray();

            for(int i = 0; i < 1 + random.nextInt(5); i++)
            { ipv4AddrsArray.put(random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256)); }

            ipv4Addrs.put("ipv4Addrs", ipv4AddrsArray);

            if(excepId == ExceptionId.SUSPICION_OF_DDOS_ATTACK)
            { addtMeasInfo.put("ddosAttack", ipv4Addrs); }

            else if(excepId == ExceptionId.WRONG_DESTINATION_ADDRESS)
            { addtMeasInfo.put("wrgDest", ipv4Addrs); }
        }


        else
        {
            JSONArray circumsArray = new JSONArray();
            JSONObject circum_entry = new JSONObject();

            circum_entry.put("freq", 10 * random.nextFloat());
            circum_entry.put("tm", sim.format(new Date()));


            if(excepId == ExceptionId.UNEXPECTED_RADIO_LINK_FAILURES || excepId == ExceptionId.PING_PONG_ACROSS_CELLS)
            {
                JSONObject areaInfo = new JSONObject();
                JSONArray taisArray = new JSONArray();
                JSONObject tai_entry = new JSONObject();

                JSONObject plmnId = new JSONObject();
                plmnId.put("mcc", String.valueOf(100 + random.nextInt(900)));
                plmnId.put("mnc", String.valueOf(10 + random.nextInt(990)));

                tai_entry.put("plmnId", plmnId);
                tai_entry.put("tac", RandomStringUtils.randomAlphanumeric(5));

                taisArray.put(tai_entry);

                areaInfo.put("tais", taisArray);
                circum_entry.put("locArea", areaInfo);
            }

            else if(excepId == ExceptionId.TOO_FREQUENT_SERVICE_ACCESS || excepId == ExceptionId.ABNORMAL_TRAFFIC_VOLUME)
            { circum_entry.put("vol", 100 + random.nextInt(1000)); }

            circumsArray.put(circum_entry);

            addtMeasInfo.put("circums", circumsArray);
        }


        abnorBehavrs_entry.put("supi", supiArray);
        abnorBehavrs_entry.put("exceps", exceps);
        abnorBehavrs_entry.put("addtMeasInfo", addtMeasInfo);

        abnorBehavrsArray.put(abnorBehavrs_entry);

        abnorBehavrs.put("event", NwdafEvent.ABNORMAL_BEHAVIOUR.toString());
        abnorBehavrs.put("abnorBehavrs", abnorBehavrsArray);

        eventNotifications.put(abnorBehavrs);

        json.put("subscriptionId", abnorBehavrNotifyData.getSubscriptionId());
        json.put("eventNotifications", eventNotifications);

        return json;
    }


    public static JSONObject getUeCommPayload(UeCommNotification ueCommNotification) throws JSONException {

        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();

        JSONObject ueComms = new JSONObject();
        JSONArray ueCommsArray = new JSONArray();

        JSONObject ueComms_entry = new JSONObject();
        ueComms_entry.put("commDur", ueCommNotification.getCommDur());
        ueComms_entry.put("ts", ueCommNotification.getTs());

        JSONObject trafChar = new JSONObject();

        JSONArray fDescs = new JSONArray();

        for(int i = 0; i < FlowDirection.values().length; i++)
        {
            JSONObject fDescs_entry = new JSONObject();

            JSONObject ipTrafficFilter = new JSONObject();

            JSONObject flowDescriptions = new JSONObject();
            flowDescriptions.put("direction", flowDir[i]);

            ipTrafficFilter.put("flowId", FlowDirection.values()[i].toString());
            ipTrafficFilter.put("flowDescriptions", flowDescriptions);

            fDescs_entry.put("ipTrafficFilter", ipTrafficFilter);
            fDescs.put(fDescs_entry);
        }


        trafChar.put("fDescs", fDescs);

        ueComms_entry.put("trafChar", trafChar);
        ueComms_entry.put("ulVol", ueCommNotification.getUlVol());
        ueComms_entry.put("dlVol", ueCommNotification.getDlVol());

        ueCommsArray.put(ueComms_entry);

        ueComms.put("event", NwdafEvent.UE_COMM.toString());
        ueComms.put("ueComms", ueCommsArray);

        eventNotifications.put(ueComms);

        json.put("subscriptionId", ueCommNotification.getSubscriptionId());
        json.put("eventNotifications", eventNotifications);

        return json;
    }




    public static JSONObject getNfLoadPayload(NfLoadNotification nfLoadNotification, NfThresholdType thresholdType) throws JSONException {

        JSONObject json = new JSONObject();
        JSONArray eventNotifications = new JSONArray();

        JSONObject nfLoadLevelInfo = new JSONObject();
        JSONArray nfLoadLevelInfoArray = new JSONArray();

        JSONObject nfLoadLevelInfo_entry = new JSONObject();

        nfLoadLevelInfo_entry.put("nfType", NFType.values()[nfLoadNotification.getNfType()].toString());
        nfLoadLevelInfo_entry.put("nfInstanceId", nfLoadNotification.getNfInstanceId());

        JSONObject nfStatus = new JSONObject();
        nfStatus.put("statusRegistered", random.nextInt(100));

        nfLoadLevelInfo_entry.put("nfStatus", nfStatus);


        switch(thresholdType)
        {
            case CPU_USAGE: nfLoadLevelInfo_entry.put("nfCpuUsage", nfLoadNotification.getNfCpuUsage());
                            break;

            case MEMORY_USAGE: nfLoadLevelInfo_entry.put("nfMemoryUsage", nfLoadNotification.getNfMemoryUsage());
                               break;

            case STORAGE_USAGE: nfLoadLevelInfo_entry.put("nfStorageUsage", nfLoadNotification.getNfStorageUsage());
                                break;

            case LOAD_LEVEL: nfLoadLevelInfo_entry.put("nfLoadLevel", nfLoadNotification.getNfLoadLevel());
                             break;
        }

        nfLoadLevelInfoArray.put(nfLoadLevelInfo_entry);

        nfLoadLevelInfo.put("event", NwdafEvent.NF_LOAD.toString());
        nfLoadLevelInfo.put("nfLoadLevelInfo", nfLoadLevelInfoArray);

        eventNotifications.put(nfLoadLevelInfo);

        json.put("subscriptionId", nfLoadNotification.getSubscriptionId());
        json.put("eventNotifications", eventNotifications);

        return json;
    }

}
