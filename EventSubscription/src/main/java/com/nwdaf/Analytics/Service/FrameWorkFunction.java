package com.nwdaf.Analytics.Service;


import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.nwdaf.Analytics.Controller.Nnwdaf_Controller.EventCounters;


public class FrameWorkFunction {



    public static final String ENTER = "ENTER ";
    public static final String EXIT = "EXIT ";


    public static Object getStats(Integer eventID)
    {
        if(eventID == null)
        { return getAllStats(); }

        HashMap<Object, Object> stats = new HashMap<>();

        HashMap<Object, Object> eventInfo = new HashMap<>();
        HashMap<Object, Object> counterStats = new HashMap<>();

        eventInfo.put("ID", eventID);
        eventInfo.put("Event", EventID.values()[eventID].toString());

        counterStats.put("Subscriptions_Received", EventCounters[eventID].getSubscriptions_received());
        counterStats.put("Subscriptions_Response", EventCounters[eventID].getSubscriptions_response());

        counterStats.put("Un-Subscriptions_Received", EventCounters[eventID].getUnSubscriptions_received());
        counterStats.put("Un-Subscriptions_Response", EventCounters[eventID].getUnSubscriptions_response());

        counterStats.put("SubscriptionUpdates_Received", EventCounters[eventID].getSubscriptionUpdates_received());
        counterStats.put("SubscriptionUpdates_Response", EventCounters[eventID].getSubscriptionUpdates_response());

        counterStats.put("SubscriptionNotifications_Sent", EventCounters[eventID].getSubscriptionNotifications_sent());

        counterStats.put("Subscriptions_Sent", EventCounters[eventID].getSubscriptions_sent());
        counterStats.put("Un-Subscriptions_Sent", EventCounters[eventID].getUnSubscriptions_sent());
        counterStats.put("SubscriptionNotifications_Received", EventCounters[eventID].getSubscriptionNotifications_received());

        counterStats.put("Analytics_Request", EventCounters[eventID].getAnalyticsRequest());
        counterStats.put("Analytics_Response", EventCounters[eventID].getAnalyticsResponse());

        stats.put("EventInfo", eventInfo);
        stats.put("Counters", counterStats);

        return stats;
    }




    public static List<Object> getAllStats()
    {
        List<Object> list = new ArrayList<Object>();

        HashMap<Object, Object> cumulativeData = new HashMap<>();

        cumulativeData.put("CumulativeStats", getCumulativeData());
        list.add(cumulativeData);

        for(int eventID = 0; eventID < EventID.values().length; eventID++)
        {
            HashMap<Object, Object> stats = new HashMap<>();

            HashMap<Object, Object> eventInfo = new HashMap<>();
            HashMap<Object, Object> counterStats = new HashMap<>();

            eventInfo.put("ID", eventID);
            eventInfo.put("Event", EventID.values()[eventID].toString());

            counterStats.put("Subscriptions_Received", EventCounters[eventID].getSubscriptions_received());
            counterStats.put("Subscriptions_Response", EventCounters[eventID].getSubscriptions_response());

            counterStats.put("Un-Subscriptions_Received", EventCounters[eventID].getUnSubscriptions_received());
            counterStats.put("Un-Subscriptions_Response", EventCounters[eventID].getUnSubscriptions_response());

            counterStats.put("SubscriptionUpdates_Received", EventCounters[eventID].getSubscriptionUpdates_received());
            counterStats.put("SubscriptionUpdates_Response", EventCounters[eventID].getSubscriptionUpdates_response());

            counterStats.put("SubscriptionNotifications_Sent", EventCounters[eventID].getSubscriptionNotifications_sent());

            counterStats.put("Subscriptions_Sent", EventCounters[eventID].getSubscriptions_sent());
            counterStats.put("Un-Subscriptions_Sent", EventCounters[eventID].getUnSubscriptions_sent());
            counterStats.put("SubscriptionNotifications_Received", EventCounters[eventID].getSubscriptionNotifications_received());

            counterStats.put("Analytics_Request", EventCounters[eventID].getAnalyticsRequest());
            counterStats.put("Analytics_Response", EventCounters[eventID].getAnalyticsResponse());

            stats.put("EventInfo", eventInfo);
            stats.put("Counters", counterStats);

            list.add(stats);
        }

        return list;
    }



    public static HashMap<Object, Object> getCumulativeData()
    {
        Counters cumulative = new Counters();
        HashMap<Object, Object> cumulativeStats = new HashMap<>();

        for(Counters eventCounter: EventCounters)
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

        cumulativeStats.put("Subscriptions_Received", cumulative.getSubscriptions_received());
        cumulativeStats.put("Subscriptions_Response", cumulative.getSubscriptions_response());

        cumulativeStats.put("Un-Subscriptions_Received", cumulative.getUnSubscriptions_received());
        cumulativeStats.put("Un-Subscriptions_Response", cumulative.getUnSubscriptions_response());

        cumulativeStats.put("SubscriptionUpdates_Received", cumulative.getSubscriptionUpdates_received());
        cumulativeStats.put("SubscriptionUpdates_Response", cumulative.getSubscriptionUpdates_response());

        cumulativeStats.put("SubscriptionNotifications_Sent", cumulative.getSubscriptionNotifications_sent());

        cumulativeStats.put("Subscriptions_Sent", cumulative.getSubscriptions_sent());
        cumulativeStats.put("Un-Subscriptions_Sent", cumulative.getUnSubscriptions_sent());
        cumulativeStats.put("SubscriptionNotifications_Received", cumulative.getSubscriptionNotifications_received());

        cumulativeStats.put("Analytics_Request", cumulative.getAnalyticsRequest());
        cumulativeStats.put("Analytics_Response", cumulative.getAnalyticsResponse());

        return cumulativeStats;
    }




    // Reset Counters
    public static void restCounters() {

        for(Counters eventCounters: EventCounters)
        { eventCounters.reset(); }
    }


    // Returns Unique generated ID
    public static UUID getUniqueID()
    { return UUID.randomUUID(); }


    public static void showQosInfo(NnwdafEventsSubscription nnwdafEventsSubscription)
    {
        System.out.println("5Qi: " + nnwdafEventsSubscription.get_5Qi());
        System.out.println("plmnID: " + nnwdafEventsSubscription.getPlmnID());
        System.out.println("tac: " + nnwdafEventsSubscription.getTac());
        System.out.println("ranUeThroughtThreshold: " + nnwdafEventsSubscription.getRanUeThroughputThreshold());
        System.out.println("qosFlowRetainThreshold: " + nnwdafEventsSubscription.getQosFlowRetainThreshold());
    }

}



/*

    //Returns Counter Statistics
    public static HashMap<String, Object> getStats(Integer eventID)
    {
        if(eventID == null)
        { return cumulativeStats(); }


        counterStats.put("Event_ID", EventID.values()[eventID].toString());

        counterStats.put("Event_Subscriptions", EventCounters[eventID].getSubscriptions());
        counterStats.put("Event_UnSubscriptions", EventCounters[eventID].getUnSubscriptions());
        counterStats.put("Event_SubscriptionUpdates", EventCounters[eventID].getSubscriptionUpdates());
        counterStats.put("Event_SubscriptionNotifications", EventCounters[eventID].getSubscriptionNotifications());

        counterStats.put("Collector_Subscriptions", EventCounters[eventID].getCollectorSubscriptions());
        counterStats.put("Collector_UnSubscriptions", EventCounters[eventID].getCollectorUnSubscriptions());
        counterStats.put("Collector_SubscriptionNotifications", EventCounters[eventID].getCollectorSubscriptionNotifications());

        counterStats.put("Analytics_Subscriptions", EventCounters[eventID].getAnalyticsSubscriptions());
        counterStats.put("Analytics_Notifications", EventCounters[eventID].getAnalyticsNotifications());

        return counterStats;
    }


    public static HashMap<String, Object> cumulativeStats()
    {
        Counters cumulativeCounters = new Counters();

        for(Counters eventCounters: EventCounters)
        {
            cumulativeCounters.setSubscriptions(cumulativeCounters.getSubscriptions().add(eventCounters.getSubscriptions()));
            cumulativeCounters.setUnSubscriptions(cumulativeCounters.getUnSubscriptions().add(eventCounters.getUnSubscriptions()));
            cumulativeCounters.setSubscriptionUpdates(cumulativeCounters.getSubscriptionUpdates().add(eventCounters.getSubscriptionUpdates()));
            cumulativeCounters.setSubscriptionNotifications(cumulativeCounters.getSubscriptionNotifications().add(eventCounters.getSubscriptionNotifications()));

            cumulativeCounters.setCollectorSubscriptions(cumulativeCounters.getCollectorSubscriptions().add(eventCounters.getCollectorSubscriptions()));
            cumulativeCounters.setCollectorUnSubscriptions(cumulativeCounters.getCollectorUnSubscriptions().add(eventCounters.getCollectorUnSubscriptions()));
            cumulativeCounters.setCollectorSubscriptionNotifications(cumulativeCounters.getCollectorSubscriptionNotifications().add(eventCounters.getCollectorSubscriptionNotifications()));

            cumulativeCounters.setAnalyticsSubscriptions(cumulativeCounters.getAnalyticsSubscriptions().add(eventCounters.getAnalyticsSubscriptions()));
            cumulativeCounters.setAnalyticsNotifications(cumulativeCounters.getAnalyticsNotifications().add(eventCounters.getAnalyticsNotifications()));
        }


        counterStats.put("Event_Subscriptions", cumulativeCounters.getSubscriptions());
        counterStats.put("Event_UnSubscriptions", cumulativeCounters.getUnSubscriptions());
        counterStats.put("Event_SubscriptionUpdates", cumulativeCounters.getSubscriptionUpdates());
        counterStats.put("Event_SubscriptionNotifications", cumulativeCounters.getSubscriptionNotifications());

        counterStats.put("Collector_Subscriptions", cumulativeCounters.getCollectorSubscriptions());
        counterStats.put("Collector_UnSubscriptions", cumulativeCounters.getCollectorUnSubscriptions());
        counterStats.put("Collector_SubscriptionNotifications", cumulativeCounters.getCollectorSubscriptionNotifications());

        counterStats.put("Analytics_Subscriptions", cumulativeCounters.getAnalyticsSubscriptions());
        counterStats.put("Analytics_Notifications", cumulativeCounters.getAnalyticsNotifications());

        return counterStats;
    }
 */