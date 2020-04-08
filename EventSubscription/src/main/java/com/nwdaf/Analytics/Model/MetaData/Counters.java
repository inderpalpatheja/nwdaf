package com.nwdaf.Analytics.Model.MetaData;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class Counters {


    private BigInteger subscriptions_received;
    private BigInteger subscriptions_response;

    private BigInteger unSubscriptions_received;
    private BigInteger unSubscriptions_response;

    private BigInteger subscriptionUpdates_received;
    private BigInteger subscriptionUpdates_response;

    private BigInteger subscriptionNotifications_sent;

    private BigInteger subscriptions_sent;
    private BigInteger unSubscriptions_sent;
    private BigInteger subscriptionNotifications_received;

    private BigInteger analyticsRequest;
    private BigInteger analyticsResponse;



    public Counters()
    {
        subscriptions_received = BigInteger.ZERO;
        subscriptions_response = BigInteger.ZERO;

        unSubscriptions_received = BigInteger.ZERO;
        unSubscriptions_response = BigInteger.ZERO;

        subscriptionUpdates_received = BigInteger.ZERO;
        subscriptionUpdates_response = BigInteger.ZERO;

        subscriptionNotifications_sent = BigInteger.ZERO;

        subscriptions_sent = BigInteger.ZERO;
        unSubscriptions_sent = BigInteger.ZERO;
        subscriptionNotifications_received = BigInteger.ZERO;

        analyticsRequest = BigInteger.ZERO;
        analyticsResponse = BigInteger.ZERO;
    }




    /****************************************************************************************************************/



    public void incrementSubscriptionsReceived()
    { subscriptions_received = subscriptions_received.add(BigInteger.ONE); }

    public void incrementSubscriptionsResponse()
    { subscriptions_response = subscriptions_response.add(BigInteger.ONE); }


    public void incrementUnSubscriptionsReceived()
    { unSubscriptions_received = unSubscriptions_received.add(BigInteger.ONE); }

    public void incrementUnSubscriptionsResponse()
    { unSubscriptions_response = unSubscriptions_response.add(BigInteger.ONE); }


    public void incrementSubscriptionUpdatesReceived()
    { subscriptionUpdates_received = subscriptionUpdates_received.add(BigInteger.ONE); }

    public void incrementSubscriptionUpdatesResponse()
    { subscriptionUpdates_response = subscriptionUpdates_response.add(BigInteger.ONE); }


    public void incrementSubscriptionNotificationsSent()
    { subscriptionNotifications_sent = subscriptionNotifications_sent.add(BigInteger.ONE); }

    public void incrementSubscriptionsSent()
    { subscriptions_sent = subscriptions_sent.add(BigInteger.ONE); }

    public void incrementUnSubscriptionsSent()
    { unSubscriptions_sent = unSubscriptions_sent.add(BigInteger.ONE); }

    public void incrementSubscriptionNotificationsReceived()
    { subscriptionNotifications_received = subscriptionNotifications_received.add(BigInteger.ONE); }

    public void incrementAnalyticsRequest()
    { analyticsRequest = analyticsRequest.add(BigInteger.ONE); }

    public void incrementAnalyticsResponse()
    { analyticsResponse = analyticsResponse.add(BigInteger.ONE); }




    /****************************************************************************************************************/


    public void reset()
    {
        subscriptions_received = BigInteger.ZERO;
        subscriptions_response = BigInteger.ZERO;

        unSubscriptions_received = BigInteger.ZERO;
        unSubscriptions_response = BigInteger.ZERO;

        subscriptionUpdates_received = BigInteger.ZERO;
        subscriptionUpdates_response = BigInteger.ZERO;

        subscriptionNotifications_sent = BigInteger.ZERO;

        subscriptions_sent = BigInteger.ZERO;
        unSubscriptions_sent = BigInteger.ZERO;
        subscriptionNotifications_received = BigInteger.ZERO;

        analyticsRequest = BigInteger.ZERO;
        analyticsResponse = BigInteger.ZERO;
    }

}
