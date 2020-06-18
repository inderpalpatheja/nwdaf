package com.nwdaf.Analytics.Configuration;

import com.nwdaf.Analytics.Model.CustomData.NfLoad.NFType;
import org.springframework.core.convert.converter.Converter;

public class StringToNFType implements Converter<String, NFType> {

    String regex = "^[0-9]{1,2}$";

    @Override
    public NFType convert(String s) {

        try
        {
            if(s.matches(regex))
            {
                Integer ordinal = Integer.valueOf(s);

                if(ordinal >= 0 && ordinal < NFType.values().length)
                { return NFType.values()[ordinal]; }

                else
                { throw new IllegalArgumentException(); }
            }

            return NFType.valueOf(s.toUpperCase());
        }

        catch(IllegalArgumentException ex)
        { return null; }
    }
}
