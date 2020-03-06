package com.nwdaf.Analytics.Service.Validator;

import com.nwdaf.Analytics.Controller.ConnectionCheck.MissingData;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Model.TableType.SubscriptionTable;


public class UpdateValidator {

    public static Object check(NnwdafEventsSubscription subscription, SubUpdateRawData rawData, SubscriptionTable subEntry, Integer loadLevelThreshold)
    {
        if(nullCheck(subscription))
        { return new MissingData(rawData, 4); }

        int error_count = 0;

        subscription = checkForMissingValues(subscription, subEntry, loadLevelThreshold);

        if(subscription.getEventID() < 0 || subscription.getEventID() > EventID.values().length)
        {
            rawData.setEventID("needs to be between 0 ≤ evenID ≤ " + (EventID.values().length - 1));
            error_count++;
        }

        if(subscription.getNotifMethod() < 0 || subscription.getNotifMethod() > 1)
        {
            rawData.setNotifMethod("needs to be either 0 (PERIOD) or 1 (THRESHOLD)");
            error_count++;
        }

        if(subscription.getRepetitionPeriod() < 0)
        {
            rawData.setRepetitionPeriod("needs to be greater than 0");
            error_count++;
        }

        if(subscription.getLoadLevelThreshold() < 0)
        {
            rawData.setLoadLevelThreshold("needs to be greater than 0");
            error_count++;
        }

        return error_count == 0 ? subscription : new MissingData(rawData, error_count);
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
