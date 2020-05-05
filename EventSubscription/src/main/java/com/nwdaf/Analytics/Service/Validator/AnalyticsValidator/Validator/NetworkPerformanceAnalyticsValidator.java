package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.NetworkPerfAnalyticsError;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.GenericAnalyticsValidator;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;

public class NetworkPerformanceAnalyticsValidator extends GenericAnalyticsValidator {

    final static String regex = "^[0-" + (NetworkPerfType.values().length - 1) + "]{1}$";


    public static Object check(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasSupi = checkForSupi(rawData, subscription);
        boolean hasNwPerfType = checkForNwPerfType(rawData, subscription);
        boolean hasAnyUE = checkForAnyUE(rawData, subscription);

        return (hasSupi && hasNwPerfType && hasAnyUE) ? subscription : new NetworkPerfAnalyticsError(rawData);
    }


    public static boolean checkForNwPerfType(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getNwPerfType() == null || rawData.getNwPerfType().isEmpty())
        { rawData.setNwPerfType(ErrorMessage.IS_NULL); }

        else
        {
            if(rawData.getNwPerfType().matches(regex))
            { subscription.setNwPerfType(Integer.valueOf(rawData.getNwPerfType())); }

            else
            { rawData.setNwPerfType(ErrorMessage.INVALID_NETWORK_PERF_TYPE); }
        }

        return subscription.getNwPerfType() != null;
    }

}
