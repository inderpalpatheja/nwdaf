package com.nwdaf.Analytics.Service.Validator;

import com.nwdaf.Analytics.Controller.ConnectionCheck.MissingData;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;


public class SubscriptionValidator {



    public static Object check(NnwdafEventsSubscription subscription, SubscriptionRawData subscriptionRawData)
    {

        int error_count = 0;

        if(subscription.getEventID() != null)
        {
            if(subscription.getEventID() < 0 || subscription.getEventID() >= EventID.values().length)
            { subscriptionRawData.setEventID("should be between 0 ≤ eventID ≤ " + (EventID.values().length - 1));
              error_count++; }
        }

        else
        { subscriptionRawData.setEventID("cannot be missing or empty");
          error_count++; }


        if(subscription.getNotificationURI() == null || subscription.getNotificationURI().trim().isEmpty())
        {
            subscriptionRawData.setNotificationURI("cannot be missing or empty");
            error_count++;
        }


        if(subscription.getSnssais() == null || subscription.getSnssais().trim().isEmpty())
        {
            subscriptionRawData.setSnssais("cannot be missing or empty");
            error_count++;
        }



        if(subscription.getNotifMethod() == null)
        { subscription.setNotifMethod(NotificationMethod.THRESHOLD.ordinal()); }

        else
        {
            if(subscription.getNotifMethod() < 0 || subscription.getNotifMethod() > 1)
            { subscriptionRawData.setNotifMethod("needs to be either 0 (PERIOD) or 1 (THRESHOLD)");
              error_count++; }

        }

        if(subscription.getRepetitionPeriod() < 0)
        {
            subscriptionRawData.setRepetitionPeriod("needs to be greater than 0");
            error_count++;
        }


        if(subscription.getLoadLevelThreshold() < 0)
        {
            subscriptionRawData.setLoadLevelThreshold("needs to be greater than 0");
            error_count++;
        }


        subscription.setAnySlice(hasAnySlice(subscription.getAnySlice()));


        if(subscription.getNotifMethod().equals(NotificationMethod.PERIODIC.ordinal()) && subscription.getRepetitionPeriod() == null)
        {  subscriptionRawData.setRepetitionPeriod("cannot be missing or empty when NotifMethod = 0 (PERIODIC)");
           error_count++; }

        if(subscription.getNotifMethod().equals(NotificationMethod.THRESHOLD.ordinal()) && subscription.getLoadLevelThreshold() == null)
        { subscriptionRawData.setLoadLevelThreshold("cannot be missing or empty when NotifMethod = 1 (THRESHOLD)");
          error_count++; }


        return error_count == 0 ? subscription : new MissingData(subscriptionRawData, error_count);
    }



    public static Boolean hasAnySlice(Boolean anySlice)
    { return (anySlice == null) ? Boolean.FALSE : anySlice; }



/*
    public static NnwdafEventsSubscription check(NnwdafEventsSubscription subscription)
    {
        subscription.setNotifMethod(hasNotifMethod(subscription.getNotifMethod()));
        subscription.setAnySlice(hasAnySlice(subscription.isAnySlice()));

        if(!hasEssentials(subscription) || !hasNotifParameters(subscription))
        { return null; }

        return subscription;
    }



    public static boolean hasEssentials(NnwdafEventsSubscription subscription)
    { return subscription.getEventID() != null && subscription.getNotificationURI() != null && subscription.getSnssais() != null; }


    public static Integer hasNotifMethod(Integer notifMethod)
    { return (notifMethod == null) ? NotificationMethod.THRESHOLD.ordinal() : notifMethod; }


    public static Boolean hasAnySlice(Boolean anySlice)
    { return (anySlice == null) ? Boolean.FALSE : anySlice; }


    public static boolean hasNotifParameters(NnwdafEventsSubscription subscription)
    {
        if(subscription.getNotifMethod() == NotificationMethod.PERIODIC.ordinal() && subscription.getRepetitionPeriod() == null)
        {  return false; }

        if(subscription.getNotifMethod() == NotificationMethod.THRESHOLD.ordinal() && subscription.getLoadLevelThreshold() == null)
        { return false; }

        return true;
    }
 */
}
