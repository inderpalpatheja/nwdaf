package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.UserDataCongestion.CongestionType;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.UserDataCongestionError;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.GenericSubscriptionValidator;

public class UserDataCongestionValidator extends GenericSubscriptionValidator {

    public static Object check(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasSupi = checkForSupi(rawData, subscription);
        boolean hasCongType = checkForCongType(rawData, subscription);
        boolean hasCongLevelThreshold = checkForCongLevelThreshold(rawData, subscription);

        return (hasSupi && hasCongType && hasCongLevelThreshold) ? subscription : new UserDataCongestionError(rawData);
    }


    public static boolean checkForCongType(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getCongType() == null)
        { rawData.setCongType(ErrorMessage.IS_NULL); }

        else if(!(rawData.getCongType() instanceof Integer))
        { rawData.setCongType(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer congType = (Integer)rawData.getCongType();

            if(congType < 0 || congType >= CongestionType.values().length)
            { rawData.setCongType(ErrorMessage.INVALID_CONGESTION_TYPE); }

            else
            { subscription.setCongType(congType); }
        }

        return subscription.getCongType() != null;
    }


    public static boolean checkForCongLevelThreshold(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getCongLevelThreshold() == null)
        { rawData.setCongLevelThreshold(ErrorMessage.IS_NULL); }

        else if(!(rawData.getCongLevelThreshold() instanceof Integer))
        { rawData.setCongLevelThreshold(ErrorMessage.NOT_INTEGER); }

        else
        { subscription.setCongLevelThreshold((Integer)rawData.getCongLevelThreshold()); }

        return subscription.getCongLevelThreshold() != null;
    }

}
