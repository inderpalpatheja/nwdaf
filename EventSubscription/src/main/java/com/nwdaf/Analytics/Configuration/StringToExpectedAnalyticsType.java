package com.nwdaf.Analytics.Configuration;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.ExpectedAnalyticsType;
import org.springframework.core.convert.converter.Converter;

public class StringToExpectedAnalyticsType implements Converter<String, ExpectedAnalyticsType> {

    String regex = "^[0-" + (ExpectedAnalyticsType.values().length - 1) + "]{1}$";

    @Override
    public ExpectedAnalyticsType convert(String s) {

        try
        {
            if(s.matches(regex))
            { return ExpectedAnalyticsType.values()[Integer.valueOf(s)]; }

            return ExpectedAnalyticsType.valueOf(s.toUpperCase());
        }

        catch(IllegalArgumentException ex)
        { return null; }
    }
}
