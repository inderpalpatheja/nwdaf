package com.nwdaf.Analytics.Model.TableType.ServiceExperience;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceExperienceInformation {

    Integer ID;
    String supi;
    String snssais;
    Float mos;
    Float upperRange;
    Float lowerRange;
}
