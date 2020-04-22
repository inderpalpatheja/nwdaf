package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator;

import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;

public abstract class GenericAnalyticsValidator {

    final static String trueFalseRegex = "^[0-1]{1}$";


    public static boolean checkForSnssais(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getSnssais() == null || rawData.getSnssais().isEmpty())
        {
            rawData.setSnssais(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullSnssais();
        }

        else
        { subscription.setSnssais(rawData.getSnssais()); }

        return subscription.getSnssais() != null;
    }


    public static boolean checkForAnySlice(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getAnySlice() == null || rawData.getAnySlice().isEmpty())
        { rawData.setAnySlice(ErrorMessage.IS_NULL); }

        else
        {
            if(rawData.getAnySlice().matches(trueFalseRegex))
            { subscription.setAnySlice(rawData.getAnySlice().equals("1") ? Boolean.TRUE : Boolean.FALSE); }

            else
            { rawData.setAnySlice(ErrorMessage.INVALID_BOOLEAN); }
        }

        return subscription.getAnySlice() != null;
    }


    public static boolean checkForAnyUE(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getAnyUE() == null || rawData.getAnyUE().isEmpty())
        { rawData.setAnyUE(ErrorMessage.IS_NULL); }

        else
        {
            if(rawData.getAnyUE().matches(trueFalseRegex))
            { subscription.setAnyUE(rawData.getAnyUE().equals("1") ? Boolean.TRUE : Boolean.FALSE); }

            else
            { rawData.setAnyUE(ErrorMessage.INVALID_BOOLEAN); }
        }

        return subscription.getAnyUE() != null;
    }


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
