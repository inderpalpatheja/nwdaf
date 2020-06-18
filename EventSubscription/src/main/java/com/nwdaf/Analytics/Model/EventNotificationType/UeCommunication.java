package com.nwdaf.Analytics.Model.EventNotificationType;

import com.nwdaf.Analytics.Model.CustomData.UeComm.TrafficCharacterization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UeCommunication {

    Integer commDur;
    String ts;
    TrafficCharacterization trafChar;
}
