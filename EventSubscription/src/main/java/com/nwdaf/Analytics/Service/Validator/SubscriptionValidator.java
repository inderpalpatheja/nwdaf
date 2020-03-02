package com.nwdaf.Analytics.Service.Validator;

import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;


public class SubscriptionValidator {


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

}
