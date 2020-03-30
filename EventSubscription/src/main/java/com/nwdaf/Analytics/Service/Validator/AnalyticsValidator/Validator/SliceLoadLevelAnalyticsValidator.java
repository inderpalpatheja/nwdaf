package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.AnalyticsError;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.GenericAnalyticsValidator;

public class SliceLoadLevelAnalyticsValidator extends GenericAnalyticsValidator {

    public static Object check(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasAnySlice = checkForAnySlice(rawData, subscription);
        boolean hasSnssais = checkForSnssais(rawData, subscription);

        return (hasAnySlice && hasSnssais) ? subscription : new AnalyticsError(rawData, EventID.LOAD_LEVEL_INFORMATION);
    }

}
