package com.nwdaf.Analytics.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class Snssai {

    Integer sst;
    String sd;

    @Override
    public String toString() {
        return sst + "-" + sd;
    }
}
