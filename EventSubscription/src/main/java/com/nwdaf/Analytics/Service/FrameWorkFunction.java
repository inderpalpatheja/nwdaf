package com.nwdaf.Analytics.Service;


import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NwdafEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import static com.nwdaf.Analytics.Controller.Nnwdaf_Controller.EventCounter;


public class FrameWorkFunction {



    public static final String ENTER = "ENTER ";
    public static final String EXIT = "EXIT ";


    public static Object getStats(Integer eventID)
    {
        if(eventID == null)
        { return getAllStats(); }

        HashMap<Object, Object> stats = new HashMap<>();
        HashMap<Object, Object> eventInfo = new HashMap<>();

        eventInfo.put("ID", eventID);
        eventInfo.put("Event", NwdafEvent.values()[eventID].toString());

        stats.put("EventInfo", eventInfo);
        stats.put("Counters", EventCounter[eventID].getCountersData());

        return stats;
    }



    public static Object getErrorStats()
    {
        HashMap<Object, Object> errorStats = new HashMap<>();
        errorStats.put("Error_Counters", ErrorCounters.getCountersData());

        return errorStats;
    }



    public static List<Object> getAllStats()
    {
        List<Object> list = new ArrayList<>();

        HashMap<Object, Object> cumulativeData = new HashMap<>();

        cumulativeData.put("CumulativeStats", getCumulativeData());
        list.add(cumulativeData);

        for(int eventID = 0; eventID < NwdafEvent.values().length; eventID++)
        {
            HashMap<Object, Object> stats = new HashMap<>();

            HashMap<Object, Object> eventInfo = new HashMap<>();

            eventInfo.put("ID", eventID);
            eventInfo.put("Event", NwdafEvent.values()[eventID].toString());

            stats.put("EventInfo", eventInfo);
            stats.put("Counters", EventCounter[eventID].getCountersData());

            list.add(stats);
        }

        return list;
    }



    public static HashMap<Object, Object> getCumulativeData()
    {
        Counters cumulative = new Counters();

        for(Counters eventCounter: EventCounter)
        {
            cumulative.setSubscriptions_received(cumulative.getSubscriptions_received().add(eventCounter.getSubscriptions_received()));
            cumulative.setSubscriptions_response(cumulative.getSubscriptions_response().add(eventCounter.getSubscriptions_response()));

            cumulative.setUnSubscriptions_received(cumulative.getUnSubscriptions_received().add(eventCounter.getUnSubscriptions_received()));
            cumulative.setUnSubscriptions_response(cumulative.getUnSubscriptions_response().add(eventCounter.getUnSubscriptions_response()));

            cumulative.setSubscriptionUpdates_received(cumulative.getSubscriptionUpdates_received().add(eventCounter.getSubscriptionUpdates_received()));
            cumulative.setSubscriptionUpdates_response(cumulative.getSubscriptionUpdates_response().add(eventCounter.getSubscriptionUpdates_response()));

            cumulative.setSubscriptionNotifications_sent(cumulative.getSubscriptionNotifications_sent().add(eventCounter.getSubscriptionNotifications_sent()));

            cumulative.setSubscriptions_sent(cumulative.getSubscriptions_sent().add(eventCounter.getSubscriptions_sent()));
            cumulative.setUnSubscriptions_sent(cumulative.getUnSubscriptions_sent().add(eventCounter.getUnSubscriptions_sent()));
            cumulative.setSubscriptionNotifications_received(cumulative.getSubscriptionNotifications_received().add(eventCounter.getSubscriptionNotifications_received()));

            cumulative.setAnalyticsRequest(cumulative.getAnalyticsRequest().add(eventCounter.getAnalyticsRequest()));
            cumulative.setAnalyticsResponse(cumulative.getAnalyticsResponse().add(eventCounter.getAnalyticsResponse()));
        }

        return cumulative.getCountersData();
    }






    // Reset Counters
    public static void restCounters() {

        for(Counters eventCounters: EventCounter)
        { eventCounters.reset(); }

        ErrorCounters.reset();
    }


    // Returns Unique generated ID
    public static UUID getUniqueID()
    { return UUID.randomUUID(); }



}



