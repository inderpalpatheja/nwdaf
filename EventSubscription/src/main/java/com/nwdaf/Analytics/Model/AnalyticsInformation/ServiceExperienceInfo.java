package com.nwdaf.Analytics.Model.AnalyticsInformation;

import com.nwdaf.Analytics.Model.CustomData.ServiceExperience.SvcExperience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceExperienceInfo {

    String message;
    Boolean data_status;
    String supi;
    String snssais;
    SvcExperience svcExp;

}
