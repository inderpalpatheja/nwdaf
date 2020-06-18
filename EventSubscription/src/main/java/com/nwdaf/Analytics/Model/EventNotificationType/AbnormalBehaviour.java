package com.nwdaf.Analytics.Model.EventNotificationType;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.AdditionalData.AdditionalMeasurement;
import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.Exception;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class AbnormalBehaviour {

    List<String> supi;
    Exception exceps;
    AdditionalMeasurement addtMeasInfo;
}
