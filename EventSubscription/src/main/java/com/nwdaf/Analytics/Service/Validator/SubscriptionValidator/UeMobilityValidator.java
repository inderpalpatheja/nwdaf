package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator;

import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;
import com.nwdaf.Analytics.Service.Validator.ErrorReport.UeMobilityError;

public class UeMobilityValidator {

    public static Object check(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {

        boolean hasSupi = checkForSupi(rawData, subscription);
        boolean hasNotifMethod = checkForNotifMethod(rawData, subscription);

        if(hasNotifMethod)
        {
            if(subscription.getNotifMethod() == NotificationMethod.PERIODIC.ordinal())
            {
                subscription.setLoadLevelThreshold(0);

                boolean hasRepetitionPeriod = checkForRepetitionPeriod(rawData, subscription);

                if(!hasRepetitionPeriod)
                { rawData.setRepetitionPeriod(rawData.getRepetitionPeriod().toString().concat(ErrorMessage.REPETITION_PERIOID_REQUIRED)); }

                return (hasSupi && hasNotifMethod && hasRepetitionPeriod) ? subscription : new UeMobilityError(rawData);
            }

            else if(subscription.getNotifMethod() == NotificationMethod.THRESHOLD.ordinal())
            {
                subscription.setRepetitionPeriod(0);

                boolean hasLoadLevelThreshold = checkForLoadLevelThreshold(rawData, subscription);

                if(!hasLoadLevelThreshold)
                { rawData.setLoadLevelThreshold(rawData.getLoadLevelThreshold().toString().concat(ErrorMessage.LOADLEVEL_THRESHOLD_REQUIRED)); }

                return (hasSupi && hasNotifMethod && hasLoadLevelThreshold) ? subscription : new UeMobilityError(rawData);
            }
        }

        return new UeMobilityError(rawData);
    }



    public static boolean checkForNotifMethod(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getNotifMethod() == null)
        { subscription.setNotifMethod(NotificationMethod.THRESHOLD.ordinal()); }

        else if(!(rawData.getNotifMethod() instanceof Integer))
        { rawData.setNotifMethod(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer notifMethod = (Integer)rawData.getNotifMethod();

            if(notifMethod == 0 || notifMethod == 1)
            { subscription.setNotifMethod(notifMethod); }

            else
            { rawData.setNotifMethod(ErrorMessage.INVALID_NOTIFICATION_METHOD); }
        }

        return subscription.getNotifMethod() != null;
    }



    public static boolean checkForRepetitionPeriod(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getRepetitionPeriod() == null)
        { rawData.setRepetitionPeriod(ErrorMessage.IS_NULL); }

        else if(!(rawData.getRepetitionPeriod() instanceof Integer))
        { rawData.setRepetitionPeriod(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer repetitionPeriod = (Integer)rawData.getRepetitionPeriod();

            if(repetitionPeriod < 0)
            { rawData.setRepetitionPeriod(ErrorMessage.LESS_THAN_ZERO); }

            else
            { subscription.setRepetitionPeriod(repetitionPeriod); }
        }

        return subscription.getRepetitionPeriod() != null;
    }



    public static boolean checkForLoadLevelThreshold(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getLoadLevelThreshold() == null)
        { rawData.setLoadLevelThreshold(ErrorMessage.IS_NULL); }

        else if(!(rawData.getLoadLevelThreshold() instanceof Integer))
        { rawData.setLoadLevelThreshold(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer loadLevelThreshold = (Integer)rawData.getLoadLevelThreshold();

            if(loadLevelThreshold < 0)
            { rawData.setLoadLevelThreshold(ErrorMessage.LESS_THAN_ZERO); }

            else
            { subscription.setLoadLevelThreshold(loadLevelThreshold); }
        }

        return subscription.getLoadLevelThreshold() != null;
    }


    public static boolean checkForSupi(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getSupi() == null)
        { rawData.setSupi(ErrorMessage.IS_NULL); }

        else if(!(rawData.getSupi() instanceof String))
        { rawData.setSupi(ErrorMessage.NOT_STRING); }

        else
        { subscription.setSupi(rawData.getSupi().toString()); }

        return subscription.getSupi() != null;
    }



}
