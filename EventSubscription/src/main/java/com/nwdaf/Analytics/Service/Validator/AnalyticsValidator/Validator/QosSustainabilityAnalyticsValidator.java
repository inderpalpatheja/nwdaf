package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.AnalyticsError;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.GenericAnalyticsValidator;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;

public class QosSustainabilityAnalyticsValidator extends GenericAnalyticsValidator {

    public static Object check(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasAnySlice = checkForAnySlice(rawData, subscription);
        boolean hasSnssais = checkForSnssais(rawData, subscription);
        boolean hasPlmnID = checkForPlmnID(rawData, subscription);

        return (hasAnySlice && hasSnssais && hasPlmnID) ? subscription : new AnalyticsError(rawData, EventID.QOS_SUSTAINABILITY);
    }


    public static boolean checkForPlmnID(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getPlmnID() == null || rawData.getPlmnID().isEmpty())
        { rawData.setPlmnID(ErrorMessage.IS_NULL); }

        else
        {
            String plmnID[] = rawData.getPlmnID().split("-");
            subscription.setPlmnID(plmnID[0], plmnID[1]);
        }

        return subscription.getPlmnID() != null;
    }

}
