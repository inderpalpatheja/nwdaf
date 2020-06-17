package com.nwdaf.Analytics.Model.EventNotificationType;

import com.nwdaf.Analytics.Model.Snssai;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class SliceLoadLevelInformation {

    Integer loadLevelInformation;
    List<Snssai> snssais;
}
