package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.Validator;

import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport.ServiceExperienceAnalyticsError;
import com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.GenericAnalyticsValidator;

public class ServiceExperienceAnalyticsValidator extends GenericAnalyticsValidator {

    public static Object check(AnalyticsRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasSupi = checkForSupi(rawData, subscription);
        boolean hasSnssais = checkForSnssais(rawData, subscription);
        boolean hasAnyUE = checkForAnyUE(rawData, subscription);

        return (hasSupi && hasSnssais && hasAnyUE) ? subscription : new ServiceExperienceAnalyticsError(rawData);
    }

}
