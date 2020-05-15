package com.nwdaf.Analytics.Model.NetworkArea;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class NetworkAreaInfo {

    List<Tai> tais;

    public NetworkAreaInfo()
    { tais = new ArrayList<>(); }

}
