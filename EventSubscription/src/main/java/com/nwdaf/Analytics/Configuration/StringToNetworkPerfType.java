package com.nwdaf.Analytics.Configuration;

import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import org.springframework.core.convert.converter.Converter;

public class StringToNetworkPerfType implements Converter<String, NetworkPerfType> {

    String regex = "^[0-" + (NetworkPerfType.values().length - 1) + "]{1}$";

    @Override
    public NetworkPerfType convert(String s) {

        try
        {
            if(s.matches(regex))
            { return NetworkPerfType.values()[Integer.valueOf(s)]; }

            return NetworkPerfType.valueOf(s.toUpperCase());
        }

        catch(IllegalArgumentException ex)
        { return null; }
    }
}
