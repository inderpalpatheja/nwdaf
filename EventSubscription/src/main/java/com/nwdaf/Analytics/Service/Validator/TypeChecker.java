package com.nwdaf.Analytics.Service.Validator;

import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData.SubUpdateRawData;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;

public class TypeChecker {


    // Check Datatypes for Subscription process
    public static Object checkForSubscription(SubscriptionRawData subscriptionRawData) {

        NnwdafEventsSubscription subscription = new NnwdafEventsSubscription();
        int error_count = 0;


        if(subscriptionRawData.getEventID() instanceof Integer)
        { subscription.setEventID((Integer) subscriptionRawData.getEventID()); }

        else
        { subscriptionRawData.setEventID("needs to be an Integer");
          error_count++; }


        if(subscriptionRawData.getNotificationURI() instanceof String)
        { subscription.setNotificationURI((String) subscriptionRawData.getNotificationURI()); }

        else
        { subscriptionRawData.setNotificationURI("needs to be a String");
          error_count++; }



        if(subscriptionRawData.getSnssais() instanceof String)
        { subscription.setSnssais((String) subscriptionRawData.getSnssais()); }

        else
        { subscriptionRawData.setSnssais("needs to be a String");
          error_count++; }



        if(subscriptionRawData.getNotifMethod() instanceof Integer)
        { subscription.setNotifMethod((Integer) subscriptionRawData.getNotifMethod()); }

        else
        {
            if(subscriptionRawData.getNotifMethod() == null)
            { subscriptionRawData.setNotifMethod("Default value: 1 (THRESHOLD)"); }

            else
            { subscriptionRawData.setNotifMethod("needs to be an Integer");
              error_count++; }
        }



        if(subscriptionRawData.getRepetitionPeriod() instanceof Integer)
        { subscription.setRepetitionPeriod((Integer) subscriptionRawData.getRepetitionPeriod()); }

        else
        { subscriptionRawData.setRepetitionPeriod("needs to be an Integer");
          error_count++; }


        if(subscriptionRawData.getLoadLevelThreshold() instanceof Integer)
        { subscription.setLoadLevelThreshold((Integer) subscriptionRawData.getLoadLevelThreshold()); }

        else
        { subscriptionRawData.setLoadLevelThreshold("needs to be an Integer");
          error_count++; }



        if(subscriptionRawData.getAnySlice() instanceof Boolean)
        { subscription.setAnySlice((Boolean) subscriptionRawData.getAnySlice()); }

        else
        {
            if(subscriptionRawData.getAnySlice() == null)
            { subscriptionRawData.setAnySlice("Default value: 0 (False)"); }

            else
            { subscriptionRawData.setAnySlice("needs to be Boolean");
              error_count++;
            }
        }


        return error_count == 0 ? subscription : new InvalidType(subscriptionRawData, error_count);
    }




    // Check Datatypes for Update Subscription process
    public static Object checkForUpdateSub(SubUpdateRawData updateData)
    {

        NnwdafEventsSubscription subscription = new NnwdafEventsSubscription();
        int error_count = 0;


        if(updateData.getEventID() != null)
        {
            if(updateData.getEventID() instanceof Integer)
            { subscription.setEventID((Integer)updateData.getEventID()); }

            else
            {
                updateData.setEventID("needs to be an Integer");
                error_count++;
            }
        }


        if(updateData.getNotifMethod() != null)
        {
            if(updateData.getNotifMethod() instanceof Integer)
            { subscription.setNotifMethod((Integer)updateData.getNotifMethod()); }

            else
            {
                updateData.setNotifMethod("needs to be an Integer");
                error_count++;
            }
        }


        if(updateData.getRepetitionPeriod() != null)
        {
            if(updateData.getRepetitionPeriod() instanceof Integer)
            { subscription.setRepetitionPeriod((Integer)updateData.getRepetitionPeriod()); }

            else
            {
                updateData.setRepetitionPeriod("needs to be an Integer");
                error_count++;
            }
        }


        if(updateData.getLoadLevelThreshold() != null)
        {
            if(updateData.getLoadLevelThreshold() instanceof Integer)
            { subscription.setLoadLevelThreshold((Integer)updateData.getLoadLevelThreshold()); }

            else
            {
                updateData.setLoadLevelThreshold("needs to be an Integer");
                error_count++;
            }
        }


        return error_count == 0 ? subscription : new InvalidType(updateData, error_count);
    }




}
