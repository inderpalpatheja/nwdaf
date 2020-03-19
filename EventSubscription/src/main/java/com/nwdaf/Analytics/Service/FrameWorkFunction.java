package com.nwdaf.Analytics.Service;


import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.UUID;


public class FrameWorkFunction {



    private static HashMap<String, BigInteger> counterStats = new HashMap<String, BigInteger>();
    public static final String ENTER = "ENTER ";
    public static final String EXIT = "EXIT ";


    //Returns Counter Statistics
    public static HashMap<String, BigInteger> getStats()
    {
        counterStats.put("Event_Subscriptions", Counters.getSubscriptions());
        counterStats.put("Event_UnSubscriptions", Counters.getUnSubscriptions());
        counterStats.put("Event_SubscriptionUpdates", Counters.getSubscriptionUpdates());
        counterStats.put("Event_SubscriptionNotifications", Counters.getSubscriptionNotifications());

        counterStats.put("Collector_Subscriptions", Counters.getCollectorSubscriptions());
        counterStats.put("Collector_SubscriptionNotifications", Counters.getCollectorSubscriptionNotifications());
        counterStats.put("Collector_AnalyticsSubscriptions", Counters.getAnalyticsSubscriptions());
        counterStats.put("Collector_AnalyticsNotifications", Counters.getAnalyticsNotifications());

        return counterStats;
    }



    // Reset Counters
    public static void restCounters() {
        Counters.reset();
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
