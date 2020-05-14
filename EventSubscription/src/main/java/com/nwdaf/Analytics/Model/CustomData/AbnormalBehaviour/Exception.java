package com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Exception {

    ExceptionId excepId;
    Integer excepLevel;
    ExceptionTrend excepTrend;
}
