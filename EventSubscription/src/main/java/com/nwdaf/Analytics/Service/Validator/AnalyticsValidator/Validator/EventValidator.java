package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.EventError;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;

public class EventValidator {

    final static String regex = "^[0-" + (EventID.values().length - 1) + "]{1}$";


    public static Object check(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    { return checkForEventID(rawData, subscription) ? subscription : new EventError(rawData.getEventID());  }



    public static boolean checkForEventID(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getEventID() == null || rawData.getEventID().isEmpty())
        { rawData.setEventID(ErrorMessage.IS_NULL); }

        else
        {
            if(rawData.getEventID().matches(regex))
            { subscription.setEventID(Integer.valueOf(rawData.getEventID())); }

            else
            { rawData.setEventID(ErrorMessage.INVALID_EVENT_ID); }
        }

        return subscription.getEventID() != null;
    }

}
