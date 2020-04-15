package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.Validator;

import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;
import com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport.QosSustainabilityError;

public class QosSustainabilityValidator {

    public static Object check(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {

        boolean hasSnssais = checkForSnssais(rawData, subscription);
        boolean has5Qi = checkFor5Qi(rawData, subscription);
        boolean hasMcc = checkForMcc(rawData, subscription);
        boolean hasMnc = checkForMnc(rawData, subscription);
        boolean hasTac = checkForTac(rawData, subscription);
        boolean hasRanUeThroughputThreshold = checkForRanUeThroughputThreshold(rawData, subscription);
        boolean hasQosFlowRetainThreshold = checkForQosFlowRetainThreshold(rawData, subscription);

        if(hasMcc && hasMnc)
        { subscription.setPlmnID(subscription.getMcc(), subscription.getMnc()); }

        if(hasSnssais && has5Qi && hasMcc && hasMnc && hasTac)
        {
            if(hasRanUeThroughputThreshold && hasQosFlowRetainThreshold)
            {
                rawData.setQosFlowRetainThreshold(rawData.getQosFlowRetainThreshold().toString().concat(ErrorMessage.BOTH_QOS_THRESHOLD));
                rawData.setRanUeThroughputThreshold(rawData.getRanUeThroughputThreshold().toString().concat(ErrorMessage.BOTH_QOS_THRESHOLD));

                return new QosSustainabilityError(rawData);
            }

            else if(hasRanUeThroughputThreshold || hasQosFlowRetainThreshold)
            { return subscription; }
        }

        return new QosSustainabilityError(rawData);
    }



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



    public static boolean checkFor5Qi(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.get_5Qi() == null)
        {
            rawData.set_5Qi(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNull_5Qi();
        }

        else if(!(rawData.get_5Qi() instanceof Integer))
        { rawData.set_5Qi(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer _5Qi = (Integer)rawData.get_5Qi();

            if(_5Qi < 0 || _5Qi > 255)
            {
                rawData.set_5Qi(ErrorMessage.INVALID_5QI);

                // Increment Counter
                ErrorCounters.incrementInvalid_5Qi();
            }

            else
            { subscription.set_5Qi(_5Qi); }
        }

        return subscription.get_5Qi() != null;
    }



    public static boolean checkForMcc(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getMcc() == null)
        {
            rawData.setMcc(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullMcc();
        }

        else if(!(rawData.getMcc() instanceof String))
        { rawData.setMcc(ErrorMessage.NOT_STRING); }

        else
        {
            String mcc = rawData.getMcc().toString();

            if(mcc.matches("^[0-9]{3}$"))
            { subscription.setMcc(mcc); }

            else
            {
                rawData.setMcc(ErrorMessage.INVALID_MCC);

                // Increment Counter
                ErrorCounters.incrementInvalidMcc();
            }
        }

        return subscription.getMcc() != null;
    }


    public static boolean checkForMnc(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getMnc() == null)
        {
            rawData.setMnc(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullMnc();
        }

        else if(!(rawData.getMnc() instanceof String))
        { rawData.setMnc(ErrorMessage.NOT_STRING); }

        else
        {
            String mnc = rawData.getMnc().toString();

            if(mnc.matches("^[0-9]{2,3}$"))
            { subscription.setMnc(mnc); }

            else
            {
                rawData.setMnc(ErrorMessage.INVALID_MNC);

                // Increment Counter
                ErrorCounters.incrementInvalidMnc();
            }
        }

        return subscription.getMnc() != null;
    }


    public static boolean checkForTac(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getTac() == null)
        {
            rawData.setTac(ErrorMessage.IS_NULL);

            // Increment Counter
            ErrorCounters.incrementNullTac();
        }

        else if(!(rawData.getTac() instanceof String))
        { rawData.setTac(ErrorMessage.NOT_STRING); }

        else
        { subscription.setTac(rawData.getTac().toString()); }

        return subscription.getTac() != null;
    }



    public static boolean checkForRanUeThroughputThreshold(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getRanUeThroughputThreshold() == null)
        { subscription.setRanUeThroughputThreshold(null); }

        else if(!(rawData.getRanUeThroughputThreshold() instanceof Integer))
        { rawData.setRanUeThroughputThreshold(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer ranUeThroughputThreshold = (Integer)rawData.getRanUeThroughputThreshold();

            if(ranUeThroughputThreshold < 0)
            {
                rawData.setRanUeThroughputThreshold(ErrorMessage.LESS_THAN_ZERO);

                // Increment Counter
                ErrorCounters.incrementInvalidRanUeThroughputThreshold();
            }

            else
            { subscription.setRanUeThroughputThreshold(ranUeThroughputThreshold); }
        }

        return subscription.getRanUeThroughputThreshold() != null;
    }


    public static boolean checkForQosFlowRetainThreshold(SubscriptionRawData rawData, NnwdafEventsSubscription subscription)
    {
        if(rawData.getQosFlowRetainThreshold() == null)
        { subscription.setQosFlowRetainThreshold(null); }

        else if(!(rawData.getQosFlowRetainThreshold() instanceof Integer))
        { rawData.setQosFlowRetainThreshold(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer qosFlowRetainThreshold = (Integer)rawData.getQosFlowRetainThreshold();

            if(qosFlowRetainThreshold < 0)
            {
                rawData.setQosFlowRetainThreshold(ErrorMessage.LESS_THAN_ZERO);

                // Increment Counter
                ErrorCounters.incrementInvalidQosFlowRetainThreshold();
            }

            else
            { subscription.setQosFlowRetainThreshold(qosFlowRetainThreshold); }
        }

        return subscription.getQosFlowRetainThreshold() != null;
    }

}
