package com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper;

import com.nwdaf.Analytics.Model.AnalyticsInformation.AbnormalBehaviourInfo;
import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.Exception;
import com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour.ExceptionId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AbnormalBehaviourInfoMapper implements RowMapper {

    final String message = "DATA FOUND";
    final Boolean data_status = Boolean.TRUE;


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        AbnormalBehaviourInfo abnorBehavrInfo = new AbnormalBehaviourInfo();

        abnorBehavrInfo.setMessage(message);
        abnorBehavrInfo.setData_status(data_status);
        abnorBehavrInfo.setSupi(resultSet.getString("supi"));

        Exception exceps = new Exception();
        exceps.setExcepId(ExceptionId.values()[resultSet.getInt("excepId")]);
        exceps.setExcepLevel(resultSet.getInt("excepLevel"));

        abnorBehavrInfo.setExceps(exceps);

        return abnorBehavrInfo.getInfo();
    }
}
