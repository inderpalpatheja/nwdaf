package com.nwdaf.Analytics.Model.MetaData;

import java.math.BigInteger;

public class Counters {

    static private BigInteger subscriptions = BigInteger.ZERO;
    static private BigInteger unSubscriptions = BigInteger.ZERO;
    static private BigInteger subscriptionUpdates = BigInteger.ZERO;
    static private BigInteger subscriptionNotifications = BigInteger.ZERO;

    public static BigInteger getSubscriptions() {
        return subscriptions;
    }

    public static BigInteger getUnSubscriptions() {
        return unSubscriptions;
    }

    public static BigInteger getSubscriptionUpdates() {
        return subscriptionUpdates;
    }

    public static BigInteger getSubscriptionNotifications() {
        return subscriptionNotifications;
    }

    public static void incrementSubscriptions()
    { subscriptions = subscriptions.add(BigInteger.ONE); }

    public static void incrementUnSubscriptions()
    { unSubscriptions = unSubscriptions.add(BigInteger.ONE); }

    public static void incrementSubscriptionUpdates()
    { subscriptionUpdates = subscriptionUpdates.add(BigInteger.ONE); }

    public static void incrementSubscriptionNotifications()
    { subscriptionNotifications = subscriptionNotifications.add(BigInteger.ONE); }




    /****************************************************************************************************************/






    static private BigInteger collectorSubscriptions = BigInteger.ZERO;
    static private BigInteger collectorSubscriptionNotifications = BigInteger.ZERO;
    static private BigInteger analyticsSubscriptions = BigInteger.ZERO;
    static private BigInteger analyticsNotifications = BigInteger.ZERO;


    public static BigInteger getCollectorSubscriptions() {
        return collectorSubscriptions;
    }

    public static BigInteger getCollectorSubscriptionNotifications() {
        return collectorSubscriptionNotifications;
    }

    public static BigInteger getAnalyticsSubscriptions() {
        return analyticsSubscriptions;
    }

    public static BigInteger getAnalyticsNotifications() {
        return analyticsNotifications;
    }


    public static void incrementCollectorSubscriptions()
    { collectorSubscriptions = collectorSubscriptions.add(BigInteger.ONE); }

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
        collectorSubscriptionNotifications = BigInteger.ZERO;
        analyticsSubscriptions = BigInteger.ZERO;
        analyticsNotifications = BigInteger.ZERO;
    }

}
