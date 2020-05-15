package com.nwdaf.Analytics.Model.TableType.UEMobility;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLocation {


    private Integer ID;
    private String tai;
    private String cellID;
    private Integer timeDuration;

/*
    @Override
    public String toString() {
        return "UserLocation{" +
                "ID=" + ID +
                ", tai='" + tai + '\'' +
                ", cellID='" + cellID + '\'' +
                ", timeDuration=" + timeDuration +
                '}';
    }
*/

}
