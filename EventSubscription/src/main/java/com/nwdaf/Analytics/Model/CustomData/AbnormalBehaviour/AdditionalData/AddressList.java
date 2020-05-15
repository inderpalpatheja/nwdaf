package com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.AdditionalData;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AddressList {

    List<String> ipv4Addrs;
    List<String> ipv6Addrs;
}
