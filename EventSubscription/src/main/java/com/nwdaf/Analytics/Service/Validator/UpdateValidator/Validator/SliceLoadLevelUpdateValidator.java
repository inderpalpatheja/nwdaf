package com.nwdaf.Analytics.Service.Validator.UpdateValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;
import com.nwdaf.Analytics.Service.Validator.UpdateValidator.ErrorReport.NullValuesUpdateError;
import com.nwdaf.Analytics.Service.Validator.UpdateValidator.ErrorReport.SliceLoadLevelUpdateError;

public class SliceLoadLevelUpdateValidator {


    public static Object check(SubUpdateRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(nullCheck(rawData))
        { return new NullValuesUpdateError(EventID.LOAD_LEVEL_INFORMATION.toString()); }

        boolean notifMethod = true;
        boolean loadLevelThreshold = true;
        boolean repetitionPeriod = true;


        if(rawData.getNotifMethod() != null)
        { notifMethod = checkForNotifMethod(rawData, subscription); }

        if(rawData.getRepetitionPeriod() != null)
        { repetitionPeriod = checkForRepetitionPeriod(rawData, subscription); }

        if(rawData.getLoadLevelThreshold() != null)
        { loadLevelThreshold = checkForLoadLevelThreshold(rawData, subscription); }

        return (notifMethod && loadLevelThreshold && repetitionPeriod) ? subscription : sendErrorReport(rawData) ;
    }



    public static boolean checkForNotifMethod(SubUpdateRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(!(rawData.getNotifMethod() instanceof Integer))
        { rawData.setNotifMethod(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer notifMethod = (Integer)rawData.getNotifMethod();

            if(notifMethod == 0 || notifMethod == 1)
            { subscription.setNotifMethod(notifMethod); }

            else
            {
                rawData.setNotifMethod(ErrorMessage.INVALID_NOTIFICATION_METHOD);

                // Increment Counter
                ErrorCounters.incrementInvalidNotifMethod();
            }
        }

        return subscription.getNotifMethod() != null;
    }



    public static boolean checkForRepetitionPeriod(SubUpdateRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(!(rawData.getRepetitionPeriod() instanceof Integer))
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



    public static boolean checkForLoadLevelThreshold(SubUpdateRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(!(rawData.getLoadLevelThreshold() instanceof Integer))
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


    public static boolean nullCheck(SubUpdateRawData rawData)
    { return rawData.getNotifMethod() == null && rawData.getLoadLevelThreshold() == null && rawData.getRepetitionPeriod() == null; }


    public static SliceLoadLevelUpdateError sendErrorReport(SubUpdateRawData rawData)
    { return new SliceLoadLevelUpdateError(rawData.getNotifMethod(), rawData.getRepetitionPeriod(), rawData.getLoadLevelThreshold()); }


}
