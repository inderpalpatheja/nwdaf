package com.nwdaf.Analytics;

import java.math.BigInteger;

public class EventSubData {


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


    public static void reset()
    {
        subscriptions = BigInteger.ZERO;
        unSubscriptions = BigInteger.ZERO;
        subscriptionUpdates = BigInteger.ZERO;
        subscriptionNotifications = BigInteger.ZERO;
    }
}
