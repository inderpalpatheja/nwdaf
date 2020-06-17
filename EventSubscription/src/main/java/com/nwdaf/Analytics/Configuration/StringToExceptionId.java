package com.nwdaf.Analytics.Configuration;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.ExceptionId;
import org.springframework.core.convert.converter.Converter;

public class StringToExceptionId implements Converter<String, ExceptionId> {

    String regex = "^[0-9]{1,2}$";

    @Override
    public ExceptionId convert(String s) {

        try
        {
            if(s.matches(regex))
            {
                Integer ordinal = Integer.valueOf(s);

                if(ordinal >= 0 && ordinal < ExceptionId.values().length)
                { return ExceptionId.values()[ordinal]; }

                else
                { throw new IllegalArgumentException(); }
            }

            return ExceptionId.valueOf(s.toUpperCase());
        }

        catch(IllegalArgumentException ex)
        { return null; }
    }
}
