package com.nwdaf.Analytics.Model.MetaData;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.HashMap;


@Getter @Setter
public class ErrorCounters {

    private static BigInteger nullPointerException = BigInteger.ZERO;
    private static BigInteger jsonException = BigInteger.ZERO;
    private static BigInteger ioException =  BigInteger.ZERO;
    private static BigInteger uriSyntaxException = BigInteger.ZERO;

    private static BigInteger dataNotFound = BigInteger.ZERO;

    private static BigInteger invalidEventID = BigInteger.ZERO;
    private static BigInteger nullEventID = BigInteger.ZERO;
    private static BigInteger nullNotificationURI = BigInteger.ZERO;


    private static BigInteger nullSnssais = BigInteger.ZERO;

    // LOAD_LEVEL_INFORMATION
    private static BigInteger invalidNotifMethod = BigInteger.ZERO;

    private static BigInteger invalidRepetitonPeriod = BigInteger.ZERO;
    private static BigInteger invalidLoadLevelThreshold = BigInteger.ZERO;

    private static BigInteger nullRepetitionPeriod = BigInteger.ZERO;
    private static BigInteger nullLoadLevelThreshold = BigInteger.ZERO;


    // QOS_SUSTAINABILITY
    private static BigInteger invalidMcc = BigInteger.ZERO;
    private static BigInteger invalidMnc = BigInteger.ZERO;
    private static BigInteger invalid_5Qi = BigInteger.ZERO;
    private static BigInteger invalidQosFlowRetainThreshold = BigInteger.ZERO;
    private static BigInteger invalidRanUeThroughputThreshold = BigInteger.ZERO;

    private static BigInteger nullMcc = BigInteger.ZERO;
    private static BigInteger nullMnc = BigInteger.ZERO;
    private static BigInteger null_5Qi = BigInteger.ZERO;
    private static BigInteger nullTac = BigInteger.ZERO;
    private static BigInteger nullQosFlowRetainThreshold = BigInteger.ZERO;
    private static BigInteger nullRanUeThroughputThreshold = BigInteger.ZERO;


    //UE_MOBILITY
    private static BigInteger nullSupi = BigInteger.ZERO;






    public static void incrementNullPointerException()
    { nullPointerException =  nullPointerException.add(BigInteger.ONE); }

    public static void incrementJsonException()
    { jsonException = jsonException.add(BigInteger.ONE); }


    public static void incrementIOException()
    { ioException = ioException.add(BigInteger.ONE); }

    public static void incrementDataNotFound()
    { dataNotFound = dataNotFound.add(BigInteger.ONE); }

    public static void incrementUriSyntaxException()
    { uriSyntaxException = uriSyntaxException.add(BigInteger.ONE); }




    public static void incrementInvalidEventID()
    { invalidEventID = invalidEventID.add(BigInteger.ONE); }

    public static void incrementNullEventID()
    { nullEventID = nullEventID.add(BigInteger.ONE); }


    public static void incrementNullNotificationURI()
    { nullNotificationURI = nullNotificationURI.add(BigInteger.ONE); }


    public static void incrementNullSnssais()
    { nullSnssais = nullSnssais.add(BigInteger.ONE); }




    // LOAD_LEVEL_INFORMATION

    public static void incrementInvalidNotifMethod()
    { invalidNotifMethod = invalidNotifMethod.add(BigInteger.ONE); }

    public static void incrementInvalidRepetitonPeriod()
    { invalidRepetitonPeriod = invalidRepetitonPeriod.add(BigInteger.ONE); }

    public static void incrementInvalidLoadLevelThreshold()
    { invalidLoadLevelThreshold = invalidLoadLevelThreshold.add(BigInteger.ONE); }

    public static void incrementNullRepetitionPeriod()
    { nullRepetitionPeriod = nullRepetitionPeriod.add(BigInteger.ONE); }

    public static void incrementNullLoadLevelThreshold()
    { nullLoadLevelThreshold = nullLoadLevelThreshold.add(BigInteger.ONE); }



    // QOS_SUSTAINABILITY

    public static void incrementInvalidMcc()
    { invalidMcc = invalidMcc.add(BigInteger.ONE); }

    public static void incrementInvalidMnc()
    { invalidMnc = invalidMnc.add(BigInteger.ONE); }

    public static void incrementInvalid_5Qi()
    { invalid_5Qi = invalid_5Qi.add(BigInteger.ONE); }

    public static void incrementInvalidQosFlowRetainThreshold()
    { invalidQosFlowRetainThreshold = invalidQosFlowRetainThreshold.add(BigInteger.ONE); }

    public static void incrementInvalidRanUeThroughputThreshold()
    { invalidRanUeThroughputThreshold = invalidRanUeThroughputThreshold.add(BigInteger.ONE); }



    public static void incrementNullMcc()
    { nullMcc = nullMcc.add(BigInteger.ONE); }

    public static void incrementNullMnc()
    { nullMnc = nullMnc.add(BigInteger.ONE); }

    public static void incrementNull_5Qi()
    { null_5Qi = null_5Qi.add(BigInteger.ONE); }

    public static void incrementNullTac()
    { nullTac = nullTac.add(BigInteger.ONE); }

    public static void incrementNullQosFlowRetainThreshold()
    { nullQosFlowRetainThreshold = nullQosFlowRetainThreshold.add(BigInteger.ONE); }

    public static void incrementNullRanUeThroughputThreshold()
    { nullRanUeThroughputThreshold = nullRanUeThroughputThreshold.add(BigInteger.ONE); }



    // UE_MOBILITY

    public static void incrementNullSupi()
    { nullSupi = nullSupi.add(BigInteger.ONE); }





    public static void reset()
    {
        nullPointerException = BigInteger.ZERO;
        jsonException = BigInteger.ZERO;
        ioException = BigInteger.ZERO;
        uriSyntaxException = BigInteger.ZERO;

        dataNotFound = BigInteger.ZERO;
        invalidEventID = BigInteger.ZERO;
        invalidNotifMethod = BigInteger.ZERO;

        nullEventID = BigInteger.ZERO;
        nullNotificationURI = BigInteger.ZERO;

        nullSnssais = BigInteger.ZERO;


        // LOAD_LEVEL_INFORMATION
        invalidNotifMethod = BigInteger.ZERO;

        invalidRepetitonPeriod = BigInteger.ZERO;
        invalidLoadLevelThreshold = BigInteger.ZERO;

        nullRepetitionPeriod = BigInteger.ZERO;
        nullLoadLevelThreshold = BigInteger.ZERO;


        // QOS_SUSTAINABILITY
        invalidMcc = BigInteger.ZERO;
        invalidMnc = BigInteger.ZERO;
        invalid_5Qi = BigInteger.ZERO;
        invalidQosFlowRetainThreshold = BigInteger.ZERO;
        invalidRanUeThroughputThreshold = BigInteger.ZERO;

        nullMcc = BigInteger.ZERO;
        nullMnc = BigInteger.ZERO;
        null_5Qi = BigInteger.ZERO;
        nullTac = BigInteger.ZERO;
        nullQosFlowRetainThreshold = BigInteger.ZERO;
        nullRanUeThroughputThreshold = BigInteger.ZERO;


        //UE_MOBILITY
        nullSupi = BigInteger.ZERO;
    }


    public static HashMap<Object, Object> getCountersData()
    {
        HashMap<Object, Object> counterStats = new HashMap<>();

        counterStats.put("NullPointerException", nullPointerException);
        counterStats.put("JSONException", jsonException);
        counterStats.put("IOException", ioException);
        counterStats.put("URISyntaxException", uriSyntaxException);

        counterStats.put("Data_Not_Found", dataNotFound);

        counterStats.put("Null_Value_EventID", nullEventID);
        counterStats.put("Null_Value_NotificationURI", nullNotificationURI);
        counterStats.put("Null_Value_Snssais", nullSnssais);
        counterStats.put("Null_Value_RepetitionPeriod", nullRepetitionPeriod);
        counterStats.put("Null_Value_LoadLevelThreshold", nullLoadLevelThreshold);
        counterStats.put("Null_Value_5Qi", null_5Qi);
        counterStats.put("Null_Value_Tac", nullTac);
        counterStats.put("Null_Value_Mcc", nullMcc);
        counterStats.put("Null_Value_Mnc", nullMnc);
        counterStats.put("Null_Value_QosFlowRetainThreshold", nullQosFlowRetainThreshold);
        counterStats.put("Null_Value_RanUeThroughputThreshold", nullRanUeThroughputThreshold);
        counterStats.put("Null_Value_Supi", nullSupi);

        counterStats.put("Invalid_EventID", invalidEventID);
        counterStats.put("Invalid_NotificationMethod", invalidNotifMethod);
        counterStats.put("Invalid_RepetitionPeriod", invalidRepetitonPeriod);
        counterStats.put("Invalid_LoadLevelThreshold", invalidLoadLevelThreshold);
        counterStats.put("Invalid_5Qi", invalid_5Qi);
        counterStats.put("Invalid_Mcc", invalidMcc);
        counterStats.put("Invalid_Mnc", invalidMnc);
        counterStats.put("Invalid_QosFlowRetainThreshold", invalidQosFlowRetainThreshold);
        counterStats.put("Invalid_RanUeThroughputThreshold", invalidRanUeThroughputThreshold);

        return counterStats;
    }

}
