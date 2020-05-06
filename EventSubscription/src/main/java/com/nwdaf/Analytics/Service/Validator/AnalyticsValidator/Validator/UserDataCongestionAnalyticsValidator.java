package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.UserDataCongestion.CongestionType;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.UserDataCongestionAnalyticsError;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.GenericAnalyticsValidator;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;

public class UserDataCongestionAnalyticsValidator extends GenericAnalyticsValidator {

    final static String regex = "^[0-" + (CongestionType.values().length - 1) + "]{1}$";


    public static Object check(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasSupi = checkForSupi(rawData, subscription);
        boolean hasCongType = checkForCongType(rawData, subscription);

        return (hasSupi && hasCongType) ? subscription : new UserDataCongestionAnalyticsError(rawData);
    }


    public static boolean checkForCongType(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getCongType() == null || rawData.getCongType().isEmpty())
        { rawData.setCongType(ErrorMessage.IS_NULL); }

        else
        {
            if(rawData.getCongType().matches(regex))
            { subscription.setCongType(Integer.valueOf(rawData.getCongType())); }

            else
            { rawData.setCongType(ErrorMessage.INVALID_CONGESTION_TYPE); }
        }

        return subscription.getCongType() != null;
    }

}
