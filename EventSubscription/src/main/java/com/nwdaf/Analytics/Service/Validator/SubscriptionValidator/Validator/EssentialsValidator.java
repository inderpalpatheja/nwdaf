package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.EssentialsError;

public class EssentialsValidator {


    public static Object check(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasEventID = checkForEventID(rawData, subscription);
        boolean hasNotificationURI = checkForNotificationURI(rawData, subscription);

        return (hasEventID && hasNotificationURI) ? subscription : new EssentialsError(rawData);
    }



    // Validates EventID
    public static boolean checkForEventID(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {

        if(rawData.getEventID() == null)
        {
            rawData.setEventID(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullEventID();
        }

        else if(!(rawData.getEventID() instanceof Integer))
        { rawData.setEventID(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer eventID = (Integer)rawData.getEventID();

            if(eventID < 0 || eventID >= EventID.values().length)
            {
                rawData.setEventID(ErrorMessage.INVALID_EVENT_ID);

                // Increment Counter
                ErrorCounters.incrementInvalidEventID();
            }

            else
            { subscription.setEventID(eventID); }
        }

        return subscription.getEventID() != null;
    }



    // Validates NotificationURI
    public static boolean checkForNotificationURI(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {

        if(rawData.getNotificationURI() == null)
        {
            rawData.setNotificationURI(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullNotificationURI();
        }

        else if(!(rawData.getNotificationURI() instanceof String))
        { rawData.setNotificationURI(ErrorMessage.NOT_STRING); }

        else
        { subscription.setNotificationURI(rawData.getNotificationURI().toString()); }

        return subscription.getNotificationURI() != null;
    }

}
