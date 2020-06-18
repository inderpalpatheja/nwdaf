package com.nwdaf.Analytics.Configuration;

import com.nwdaf.Analytics.Model.Snssai;
import org.springframework.core.convert.converter.Converter;

public class StringToSnssai implements Converter<String, Snssai> {

    @Override
    public Snssai convert(String s) {

        String str[] = s.split("-");

        Integer sst = Integer.valueOf(str[0]);
        String sd = str[1];

        return new Snssai(sst, sd);
    }
}
