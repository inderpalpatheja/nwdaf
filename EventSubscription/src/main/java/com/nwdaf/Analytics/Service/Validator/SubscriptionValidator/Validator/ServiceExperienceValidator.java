package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator;

import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.ServiceExperienceError;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.GenericSubscriptionValidator;

public class ServiceExperienceValidator extends GenericSubscriptionValidator {

    public static Object check(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        boolean hasSupi = checkForSupi(rawData, subscription);
        boolean hasSnssais = checkForSnssais(rawData, subscription);

        return (hasSupi && hasSnssais) ? subscription : new ServiceExperienceError(rawData);
    }

}
