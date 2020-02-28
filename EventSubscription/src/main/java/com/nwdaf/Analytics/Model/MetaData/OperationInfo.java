package com.nwdaf.Analytics.Model.MetaData;

public class OperationInfo {

    public static final String SUBSCRIBE_INFO = "Subscribe to NWDAF for Event Notification(s)";
    public static final String SUBSCRIBE_NOTES = "NF service consumer subscribes to NWDAF for event notification(s) with the following parameters in the RequestBody:-\n1) eventID (Integer)\n2) notificationURI (String)\n3) snssais (String)\n4) notifMethod (Integer)\n5) repetitionPeriod (Integer)\n6) loadLevelThreshold (Integer)";


    public static final String UNSUBSCRICE_INFO = "Unsubscribe from NWDAF for Event Notification(s)";
    public static final String UNSUBSCRIBE_NOTES = "The NF service consumer shall send an HTTP DELETE request where \"{subscriptionId}\" is the event subscriptionID of the existing subscription that is to be deleted";


    public static final String UPDATE_SUBSCRIPTION_INFO = "Update Subscription for Event Notification(s)";
    public static final String UPDATE_SUBSCRIPTION_NOTES = "The NF service consumer shall send an HTTP PUT request where \"{subscriptionId}\" is the event subscriptionID of the existing subscription that is to be updated, with the following parameters:-\n1) eventID (Integer)\n2) notifMethod (Integer)\n3) repetitionPeriod (Integer)\n4) loadLevelThreshold (Integer)";


    public static final String COUNTERS_INFO = "Shows Counters";
    public static final String COUNTERS_NOTES = "Shows the count of API operations which have occurred so far like Subscriptions, Unsubscriptions, Updates, getAnalytics";


    public static final String COUNTERS_ZERO_INFO = "Sets Counters to Zero (0)";
    public static final String COUNTERS_ZERO_NOTES = "Sets all counters pertaining to Subscription/Unsubscription, Updates, Analytics to Zero (0)";
}



