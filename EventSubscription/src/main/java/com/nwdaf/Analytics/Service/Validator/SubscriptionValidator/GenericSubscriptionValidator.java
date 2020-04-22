package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator;

import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;

public class GenericSubscriptionValidator {


    // Check for SUPI
    public static boolean checkForSupi(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getSupi() == null)
        {
            rawData.setSupi(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullSupi();
        }

        else if(!(rawData.getSupi() instanceof String))
        { rawData.setSupi(ErrorMessage.NOT_STRING); }

        else
        { subscription.setSupi(rawData.getSupi().toString()); }

        return subscription.getSupi() != null;
    }


    // Check for SNSSAIS
    public static boolean checkForSnssais(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getSnssais() == null)
        {
            rawData.setSnssais(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullSnssais();
        }

        else if(!(rawData.getSnssais() instanceof String))
        { rawData.setSnssais(ErrorMessage.NOT_STRING); }

        else
        { subscription.setSnssais(rawData.getSnssais().toString()); }

        return subscription.getSnssais() != null;
    }



    // Check for anySlice
    public static boolean checkForAnySlice(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getAnySlice() == null)
        { subscription.setAnySlice(Boolean.FALSE); }

        else if(!(rawData.getAnySlice() instanceof Integer))
        { rawData.setAnySlice(ErrorMessage.NOT_BOOLEAN); }

        else
        {
            Integer anySlice = (Integer)rawData.getAnySlice();

            if(anySlice == 0)
            { subscription.setAnySlice(Boolean.FALSE); }

            else if(anySlice == 1)
            { subscription.setAnySlice(Boolean.TRUE); }

            else
            { rawData.setAnySlice(ErrorMessage.INVALID_BOOLEAN); }
        }

        return subscription.getAnySlice() != null;
    }

}
