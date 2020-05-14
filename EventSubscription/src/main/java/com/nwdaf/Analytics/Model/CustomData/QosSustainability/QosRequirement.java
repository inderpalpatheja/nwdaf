package com.nwdaf.Analytics.Model.CustomData.QosSustainability;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QosRequirement {

    @JsonProperty("5Qi")
    Integer _5Qi;

}
