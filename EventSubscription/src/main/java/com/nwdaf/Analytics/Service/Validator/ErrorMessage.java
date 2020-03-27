package com.nwdaf.Analytics.Service.Validator;

import com.nwdaf.Analytics.Model.CustomData.EventID;

public class ErrorMessage {

    public static final String IS_NULL = "cannot be null";
    public static final String NOT_INTEGER = "needs to be an Integer";
    public static final String NOT_STRING = "needs to be a String";
    public static final String INVALID_EVENT_ID = "needs to be between 0 ≤ eventID ≤ " + (EventID.values().length - 1);
    public static final String INVALID_NOTIFICATION_METHOD = "needs to be either 0(PERIODIC) or 1(THRESHOLD)";
    public static final String LESS_THAN_ZERO = "cannot be less than 0";
    public static final String NOT_BOOLEAN = "needs to be a Boolean value (0 or 1)";
    public static final String INVALID_ANY_SLICE = "needs to be either 1(True) or 0(False)";


    public static final String REPETITION_PERIOID_REQUIRED = " | repetitionPeriod is required when notifMethod = 0(PERIODIC)";
    public static final String LOADLEVEL_THRESHOLD_REQUIRED = " | loadLevelThreshold is required when notifMethod = 1(THRESHOLD)";


    public static final String INVALID_5QI = "needs to be between 0 ≤ 5Qi ≤ 255";
    public static final String INVALID_MCC = "needs to be a 3 digit numeric string";
    public static final String INVALID_MNC = "needs to be either a 2 or 3 digit numeric string";

    public static final String BOTH_QOS_THRESHOLD = " | needs to be either one of thresholds -- ranUeThroughput or qosFlowRetain";
}
