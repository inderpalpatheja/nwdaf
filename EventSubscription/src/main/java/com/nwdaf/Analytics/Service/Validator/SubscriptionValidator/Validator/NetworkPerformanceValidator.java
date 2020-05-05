package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.NetworkPerformanceError;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.GenericSubscriptionValidator;

public class NetworkPerformanceValidator extends GenericSubscriptionValidator {

    public static Object check(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasSupi = checkForSupi(rawData, subscription);
        boolean hasNotifMethod = checkForNotifMethod(rawData, subscription);
        boolean hasNwPerfType = checkForNwPerfType(rawData, subscription);
        boolean hasRelativeRatioThreshold = checkForRelativeRatioThreshold(rawData, subscription);
        boolean hasAbsoluteNumThreshold = checkForAbsoluteNumThreshold(rawData, subscription);

        if(hasSupi && hasNwPerfType && hasNotifMethod)
        {
            if(hasRelativeRatioThreshold || hasAbsoluteNumThreshold)
            { return subscription; }
        }


        return new NetworkPerformanceError(rawData);
    }



    public static boolean checkForNwPerfType(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getNwPerfType() == null)
        { rawData.setNwPerfType(ErrorMessage.IS_NULL); }

        else if(!(rawData.getNwPerfType() instanceof Integer))
        { rawData.setNwPerfType(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer nwPerfType = (Integer)rawData.getNwPerfType();

            if(nwPerfType < 0 || nwPerfType >= NetworkPerfType.values().length)
            { rawData.setNwPerfType(ErrorMessage.INVALID_NETWORK_PERF_TYPE); }

            else
            { subscription.setNwPerfType(nwPerfType); }
        }

        return subscription.getNwPerfType() != null;
    }


    public static boolean checkForRelativeRatioThreshold(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getRelativeRatioThreshold() == null)
        { subscription.setRelativeRatioThreshold(null); }

        else if(!(rawData.getRelativeRatioThreshold() instanceof Integer))
        { rawData.setRelativeRatioThreshold(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer relativeRatioThreshold = (Integer)rawData.getRelativeRatioThreshold();

            if(relativeRatioThreshold < 0 || relativeRatioThreshold > 100)
            { rawData.setRelativeRatioThreshold(ErrorMessage.INVALID_RELATIVE_RATIO); }

            else
            { subscription.setRelativeRatioThreshold(relativeRatioThreshold); }
        }

        return subscription.getRelativeRatioThreshold() != null;
    }


    public static boolean checkForAbsoluteNumThreshold(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getAbsoluteNumThreshold() == null)
        { subscription.setAbsoluteNumThreshold(null); }

        else if(!(rawData.getAbsoluteNumThreshold() instanceof Integer))
        { rawData.setAbsoluteNumThreshold(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer absoluteNumThreshold = (Integer)rawData.getAbsoluteNumThreshold();

            if(absoluteNumThreshold < 0)
            { rawData.setAbsoluteNumThreshold(ErrorMessage.LESS_THAN_ZERO); }

            else
            { subscription.setAbsoluteNumThreshold(absoluteNumThreshold); }
        }

        return subscription.getAbsoluteNumThreshold() != null;
    }


}
