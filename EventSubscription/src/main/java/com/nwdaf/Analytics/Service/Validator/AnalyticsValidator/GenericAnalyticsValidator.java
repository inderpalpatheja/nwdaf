package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator;

import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;

public abstract class GenericAnalyticsValidator {

    final static String anySliceRegex = "^[0-1]{1}$";


    public static boolean checkForSnssais(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getSnssais() == null || rawData.getSnssais().isEmpty())
        { rawData.setSnssais(ErrorMessage.IS_NULL); }

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
            if(rawData.getAnySlice().matches(anySliceRegex))
            { subscription.setAnySlice(rawData.getAnySlice().equals("1") ? Boolean.TRUE : Boolean.FALSE); }

            else
            { rawData.setAnySlice(ErrorMessage.INVALID_ANY_SLICE); }
        }

        return subscription.getAnySlice() != null;
    }

}
