package com.nwdaf.Analytics.Service.Validator;

import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.RawData;

public class TypeChecker {

    public static Object checkForSubscription(RawData rawData) {

        NnwdafEventsSubscription subscription = new NnwdafEventsSubscription();
        int error_count = 0;


        if(rawData.getEventID() instanceof Integer)
        { subscription.setEventID((Integer)rawData.getEventID()); }

        else
        { rawData.setEventID("needs to be an Integer");
          error_count++; }


        if(rawData.getNotificationURI() instanceof String)
        { subscription.setNotificationURI((String)rawData.getNotificationURI()); }

        else
        { rawData.setNotificationURI("needs to be a String");
          error_count++; }



        if(rawData.getSnssais() instanceof String)
        { subscription.setSnssais((String)rawData.getSnssais()); }

        else
        { rawData.setSnssais("needs to be a String");
          error_count++; }



        if(rawData.getNotifMethod() instanceof Integer)
        { subscription.setNotifMethod((Integer)rawData.getNotifMethod()); }

        else
        {
            if(rawData.getNotifMethod() == null)
            { rawData.setNotifMethod("Default value: 1 (THRESHOLD)"); }

            else
            { rawData.setNotifMethod("needs to be an Integer");
              error_count++; }
        }



        if(rawData.getRepetitionPeriod() instanceof Integer)
        { subscription.setRepetitionPeriod((Integer)rawData.getRepetitionPeriod()); }

        else
        { rawData.setRepetitionPeriod("needs to be an Integer");
          error_count++; }


        if(rawData.getLoadLevelThreshold() instanceof Integer)
        { subscription.setLoadLevelThreshold((Integer)rawData.getLoadLevelThreshold()); }

        else
        { rawData.setLoadLevelThreshold("needs to be an Integer");
          error_count++; }



        if(rawData.getAnySlice() instanceof Boolean)
        { subscription.setAnySlice((Boolean)rawData.getAnySlice()); }

        else
        {
            if(rawData.getAnySlice() == null)
            { rawData.setAnySlice("Default value: 0 (False)"); }

            else
            { rawData.setAnySlice("needs to be Boolean");
              error_count++;
            }
        }


        return error_count == 0 ? subscription : new InvalidType(rawData, error_count);
    }

}
