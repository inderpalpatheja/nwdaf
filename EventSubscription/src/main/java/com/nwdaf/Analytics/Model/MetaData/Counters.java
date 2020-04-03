package com.nwdaf.Analytics.Model.MetaData;

import lombok.Getter;

import java.math.BigInteger;

public class Counters {

    @Getter
    static private BigInteger subscriptions = BigInteger.ZERO;

    @Getter
    static private BigInteger unSubscriptions = BigInteger.ZERO;

    @Getter
    static private BigInteger subscriptionUpdates = BigInteger.ZERO;

    @Getter
    static private BigInteger subscriptionNotifications = BigInteger.ZERO;


    public static void incrementSubscriptions()
    { subscriptions = subscriptions.add(BigInteger.ONE); }

    public static void incrementUnSubscriptions()
    { unSubscriptions = unSubscriptions.add(BigInteger.ONE); }

    public static void incrementSubscriptionUpdates()
    { subscriptionUpdates = subscriptionUpdates.add(BigInteger.ONE); }

    public static void incrementSubscriptionNotifications()
    { subscriptionNotifications = subscriptionNotifications.add(BigInteger.ONE); }




    /****************************************************************************************************************/




    @Getter
    static private BigInteger collectorSubscriptions = BigInteger.ZERO;

    @Getter
    static private BigInteger collectorUnSubscriptions = BigInteger.ZERO;

    @Getter
    static private BigInteger collectorSubscriptionNotifications = BigInteger.ZERO;

    @Getter
    static private BigInteger analyticsSubscriptions = BigInteger.ZERO;

    @Getter
    static private BigInteger analyticsNotifications = BigInteger.ZERO;




    public static void incrementCollectorSubscriptions()
    { collectorSubscriptions = collectorSubscriptions.add(BigInteger.ONE); }

    public static void incrementCollectorUnSubscriptions()
    { collectorUnSubscriptions = collectorUnSubscriptions.add(BigInteger.ONE); }

    public static void incrementCollectorSubscriptionNotifications()
    { collectorSubscriptionNotifications = collectorSubscriptionNotifications.add(BigInteger.ONE); }

    public static void incrementAnalyticsSubscriptions()
    { analyticsSubscriptions = analyticsSubscriptions.add(BigInteger.ONE); }

    public static void incrementAnalyticsNotifications()
    { analyticsNotifications = analyticsNotifications.add(BigInteger.ONE); }





    /****************************************************************************************************************/


    public static void reset()
    {
        subscriptions = BigInteger.ZERO;
        unSubscriptions = BigInteger.ZERO;
        subscriptionUpdates = BigInteger.ZERO;
        subscriptionNotifications = BigInteger.ZERO;

        collectorSubscriptions = BigInteger.ZERO;
        collectorUnSubscriptions = BigInteger.ZERO;
        collectorSubscriptionNotifications = BigInteger.ZERO;
        analyticsSubscriptions = BigInteger.ZERO;
        analyticsNotifications = BigInteger.ZERO;
    }

}
