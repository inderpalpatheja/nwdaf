package com.nwdaf.Analytics.Configuration;

import com.nwdaf.Analytics.Model.NetworkArea.PlmnId;
import com.nwdaf.Analytics.Model.NetworkArea.Tai;
import org.springframework.core.convert.converter.Converter;

public class StringToTai implements Converter<String, Tai> {

    @Override
    public Tai convert(String s) {

        String str[] = s.split("-");

        String mcc = str[0];
        String mnc = str[1];
        String tac = str[2];

        return new Tai(new PlmnId(mcc, mnc), tac);
    }
}
