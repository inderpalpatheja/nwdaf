package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator;

import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.UeMobilityAnalyticsError;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;

public class UeMobilityAnalyticsValidator {


    public static Object check(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    { return checkForSupi(rawData, subscription) ? subscription : new UeMobilityAnalyticsError(rawData.getSupi()); }


    public static boolean checkForSupi(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getSupi() == null || rawData.getSupi().isEmpty())
        {
            rawData.setSupi(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullSupi();
        }

        else
        { subscription.setSupi(rawData.getSupi()); }

        return subscription.getSupi() != null;
    }

}
