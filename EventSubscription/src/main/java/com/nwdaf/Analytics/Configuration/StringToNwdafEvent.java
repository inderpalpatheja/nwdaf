package com.nwdaf.Analytics.Configuration;

import com.nwdaf.Analytics.Model.NwdafEvent;
import org.springframework.core.convert.converter.Converter;

public class StringToNwdafEvent implements Converter<String, NwdafEvent> {

    String regex = "^[0-" + (NwdafEvent.values().length - 1) + "]{1}$";

    @Override
    public NwdafEvent convert(String s) {

        try
        {
            if(s.matches(regex))
            { return NwdafEvent.values()[Integer.valueOf(s)]; }

            return NwdafEvent.valueOf(s.toUpperCase());
        }

        catch(IllegalArgumentException ex)
        { return null; }
    }
}
