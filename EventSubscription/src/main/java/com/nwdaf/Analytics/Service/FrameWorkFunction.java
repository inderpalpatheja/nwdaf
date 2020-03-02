package com.nwdaf.Analytics.Service;


import com.nwdaf.Analytics.Model.MetaData.Counters;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.UUID;


public class FrameWorkFunction {



    private static HashMap<String, BigInteger> counterStats = new HashMap<String, BigInteger>();


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


}
