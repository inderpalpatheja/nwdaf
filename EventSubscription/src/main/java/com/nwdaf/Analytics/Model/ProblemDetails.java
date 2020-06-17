package com.nwdaf.Analytics.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDetails {

    String title;
    Integer status;
    String cause;
}
