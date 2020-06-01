package com.nwdaf.Analytics.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class Snssai {

    Integer sst;
    String sd;

    public Snssai(String snssai)
    {
        String snssai_part[] = snssai.split("-");

        this.sst = Integer.valueOf(snssai_part[0]);
        this.sd = snssai_part[1];
    }

    @Override
    public String toString() {
        return sst + "-" + sd;
    }
}
