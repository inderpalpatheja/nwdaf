package com.nwdaf.Analytics.Model.AnalyticsInformation;

import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.Exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbnormalBehaviourInfo {

    String message;
    Boolean data_status;
    String supi;
    Exception exceps;


    public Object getInfo()
    {
        HashMap<Object, Object> abnorBehavr = new HashMap<>();

        abnorBehavr.put("message", message);
        abnorBehavr.put("data_status", data_status);

        HashMap<Object, Object> exceps_entry = new HashMap<>();
        exceps_entry.put("excepId", exceps.getExcepId().toString());
        exceps_entry.put("excepLevel", exceps.getExcepLevel());

        HashMap<Object, Object> abnorBehavr_entry = new HashMap<>();
        abnorBehavr_entry.put("supi", supi);
        abnorBehavr_entry.put("exceps", exceps_entry);

        abnorBehavr.put("abnorBehavr", abnorBehavr_entry);

        return abnorBehavr;
    }

}
