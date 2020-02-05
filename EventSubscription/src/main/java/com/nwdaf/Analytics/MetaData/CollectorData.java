package com.nwdaf.Analytics.MetaData;

import java.math.BigInteger;


public class CollectorData {

    static private BigInteger subscriptions = BigInteger.ZERO;
    static private BigInteger subscriptionNotifications = BigInteger.ZERO;
    static private BigInteger analyticsSubscriptions = BigInteger.ZERO;
    static private BigInteger analyticsNotifications = BigInteger.ZERO;


    public static BigInteger getSubscriptions() {
        return subscriptions;
    }

    public static BigInteger getSubscriptionNotifications() {
        return subscriptionNotifications;
    }

    public static BigInteger getAnalyticsSubscriptions() {
        return analyticsSubscriptions;
    }

    public static BigInteger getAnalyticsNotifications() {
        return analyticsNotifications;
    }


    public static void incrementSubscriptions()
    { subscriptions = subscriptions.add(BigInteger.ONE); }

    public static void incrementSubscriptionNotifications()
    { subscriptionNotifications = subscriptionNotifications.add(BigInteger.ONE); }

    public static void incrementAnalyticsSubscriptions()
    { analyticsSubscriptions = analyticsSubscriptions.add(BigInteger.ONE); }

    public static void incrementAnalyticsNotifications()
    { analyticsNotifications = analyticsNotifications.add(BigInteger.ONE); }


    public static void reset()
    {
        subscriptions = BigInteger.ZERO;
        subscriptionNotifications = BigInteger.ZERO;
        analyticsSubscriptions = BigInteger.ZERO;
        analyticsNotifications = BigInteger.ZERO;
    }

}
