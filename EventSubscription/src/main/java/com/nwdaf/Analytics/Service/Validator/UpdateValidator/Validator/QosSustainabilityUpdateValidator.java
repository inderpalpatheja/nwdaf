package com.nwdaf.Analytics.Service.Validator.UpdateValidator.Validator;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Service.Validator.ErrorMessage;
import com.nwdaf.Analytics.Service.Validator.UpdateValidator.ErrorReport.NullValuesUpdateError;
import com.nwdaf.Analytics.Service.Validator.UpdateValidator.ErrorReport.QosSustainabilityUpdateError;

public class QosSustainabilityUpdateValidator {


    public static Object check(SubUpdateRawData rawData, NnwdafEventsSubscription subscription)
    {

        if(nullCheck(rawData))
        { return new NullValuesUpdateError(EventID.QOS_SUSTAINABILITY.toString()); }

        boolean hasRanUeThroughputThreshold = true;
        boolean hasQosFlowRetainThreshold = true;


        if(rawData.getRanUeThroughputThreshold() != null)
        { hasRanUeThroughputThreshold = checkForRanUeThroughputThreshold(rawData, subscription); }

        if(rawData.getQosFlowRetainThreshold() != null)
        { hasQosFlowRetainThreshold = checkForQosFlowRetainThreshold(rawData, subscription); }


        if(rawData.getRanUeThroughputThreshold() != null && rawData.getQosFlowRetainThreshold() != null && hasRanUeThroughputThreshold && hasQosFlowRetainThreshold)
        {
            rawData.setQosFlowRetainThreshold(rawData.getQosFlowRetainThreshold().toString().concat(ErrorMessage.BOTH_QOS_THRESHOLD));
            rawData.setRanUeThroughputThreshold(rawData.getRanUeThroughputThreshold().toString().concat(ErrorMessage.BOTH_QOS_THRESHOLD));
        }

        else if(hasRanUeThroughputThreshold || hasQosFlowRetainThreshold)
        {
            if((rawData.getRanUeThroughputThreshold() == null && hasQosFlowRetainThreshold) || (hasRanUeThroughputThreshold && rawData.getQosFlowRetainThreshold() == null))
            { return subscription; }
        }

        return sendErrorReport(rawData);
    }



    public static boolean checkForRanUeThroughputThreshold(SubUpdateRawData rawData, NnwdafEventsSubscription subscription)
    {

        if(!(rawData.getRanUeThroughputThreshold() instanceof Integer))
        { rawData.setRanUeThroughputThreshold(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer ranUeThroughputThreshold = (Integer)rawData.getRanUeThroughputThreshold();

            if(ranUeThroughputThreshold < 0)
            { rawData.setRanUeThroughputThreshold(ErrorMessage.LESS_THAN_ZERO); }

            else
            { subscription.setRanUeThroughputThreshold(ranUeThroughputThreshold); }
        }

        return subscription.getRanUeThroughputThreshold() != null;

    }


    public static boolean checkForQosFlowRetainThreshold(SubUpdateRawData rawData, NnwdafEventsSubscription subscription)
    {

        if(!(rawData.getQosFlowRetainThreshold() instanceof Integer))
        { rawData.setQosFlowRetainThreshold(ErrorMessage.NOT_INTEGER); }

        else
        {
            Integer qosFlowRetainThreshold = (Integer)rawData.getQosFlowRetainThreshold();

            if(qosFlowRetainThreshold < 0)
            { rawData.setQosFlowRetainThreshold(ErrorMessage.LESS_THAN_ZERO); }

            else
            { subscription.setQosFlowRetainThreshold(qosFlowRetainThreshold); }
        }

        return subscription.getQosFlowRetainThreshold() != null;
    }


    public static boolean nullCheck(SubUpdateRawData rawData)
    { return rawData.getRanUeThroughputThreshold() == null && rawData.getQosFlowRetainThreshold() == null; }


    public static QosSustainabilityUpdateError sendErrorReport(SubUpdateRawData rawData)
    { return new QosSustainabilityUpdateError(rawData.getRanUeThroughputThreshold(), rawData.getQosFlowRetainThreshold()); }
}
