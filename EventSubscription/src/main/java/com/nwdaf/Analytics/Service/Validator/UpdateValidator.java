package com.nwdaf.Analytics.Service.Validator;

import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.TableType.SubscriptionTable;

public class UpdateValidator {

    public static NnwdafEventsSubscription check(NnwdafEventsSubscription subscription, SubscriptionTable subEntry, Integer loadLevelThreshold)
    {
        if(nullCheck(subscription))
        { return null; }

        subscription = checkForMissingValues(subscription, subEntry, loadLevelThreshold);

        return subscription;
    }


    public static boolean nullCheck(NnwdafEventsSubscription subscription)
    { return subscription.getEventID() == null && subscription.getNotifMethod() == null && subscription.getRepetitionPeriod() == null && subscription.getLoadLevelThreshold() == null; }


    public static NnwdafEventsSubscription checkForMissingValues(NnwdafEventsSubscription subscription, SubscriptionTable subEntry, Integer loadLevelThreshold)
    {
        if(subscription.getEventID() == null)
        { subscription.setEventID(subEntry.getEventID()); }

        if(subscription.getNotifMethod() == null)
        { subscription.setNotifMethod(subEntry.getNotifMethod()); }

        if(subscription.getRepetitionPeriod() == null)
        { subscription.setRepetitionPeriod(subEntry.getRepetitionPeriod()); }

        if(subscription.getLoadLevelThreshold() == null)
        { subscription.setLoadLevelThreshold(loadLevelThreshold); }

        return subscription;
    }

}
