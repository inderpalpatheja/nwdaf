package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;
import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.SliceLoadLevelError;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.GenericSubscriptionValidator;

public class SliceLoadLevelValidator extends GenericSubscriptionValidator {


    public static Object check(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {

        boolean hasSnssais = checkForSnssais(rawData, subscription);
        boolean hasAnySlice = checkForAnySlice(rawData, subscription);

        boolean hasNotifMethod = checkForNotifMethod(rawData, subscription);

        if(hasNotifMethod)
        {
            if(subscription.getNotifMethod() == NotificationMethod.PERIODIC.ordinal())
            {
                subscription.setLoadLevelThreshold(0);

                boolean hasRepetitionPeriod = checkForRepetitionPeriod(rawData, subscription);

                if(!hasRepetitionPeriod)
                { rawData.setRepetitionPeriod(rawData.getRepetitionPeriod().toString().concat(ErrorMessage.REPETITION_PERIOID_REQUIRED)); }

                return (hasSnssais && hasAnySlice && hasNotifMethod && hasRepetitionPeriod) ? subscription : new SliceLoadLevelError(rawData);
            }

            else if(subscription.getNotifMethod() == NotificationMethod.THRESHOLD.ordinal())
            {
                subscription.setRepetitionPeriod(0);

                boolean hasLoadLevelThreshold = checkForLoadLevelThreshold(rawData, subscription);

                if(!hasLoadLevelThreshold)
                { rawData.setLoadLevelThreshold(rawData.getLoadLevelThreshold().toString().concat(ErrorMessage.LOADLEVEL_THRESHOLD_REQUIRED)); }

                return (hasSnssais && hasAnySlice && hasNotifMethod && hasLoadLevelThreshold) ? subscription : new SliceLoadLevelError(rawData);
            }
        }

        return new SliceLoadLevelError(rawData);
    }




    public static boolean checkForRepetitionPeriod(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getRepetitionPeriod() == null)
        {
            rawData.setRepetitionPeriod(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullRepetitionPeriod();
        }


        else if(!(rawData.getRepetitionPeriod() instanceof Integer))
        { rawData.setRepetitionPeriod(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer repetitionPeriod = (Integer)rawData.getRepetitionPeriod();

            if(repetitionPeriod < 0)
            {
                rawData.setRepetitionPeriod(ErrorMessage.LESS_THAN_ZERO);

                // Increment Counter
                ErrorCounters.incrementInvalidRepetitonPeriod();
            }

            else
            { subscription.setRepetitionPeriod(repetitionPeriod); }
        }

        return subscription.getRepetitionPeriod() != null;
    }



    public static boolean checkForLoadLevelThreshold(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getLoadLevelThreshold() == null)
        {
            rawData.setLoadLevelThreshold(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullLoadLevelThreshold();
        }

        else if(!(rawData.getLoadLevelThreshold() instanceof Integer))
        { rawData.setLoadLevelThreshold(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer loadLevelThreshold = (Integer)rawData.getLoadLevelThreshold();

            if(loadLevelThreshold < 0)
            {
                rawData.setLoadLevelThreshold(ErrorMessage.LESS_THAN_ZERO);

                // Increment Counter
                ErrorCounters.incrementInvalidLoadLevelThreshold();
            }

            else
            { subscription.setLoadLevelThreshold(loadLevelThreshold); }
        }

        return subscription.getLoadLevelThreshold() != null;
    }


}
