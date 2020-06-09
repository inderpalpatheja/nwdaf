package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilityInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;



public class QosSustainabilityInformationMapper implements RowMapper<QosSustainabilityInformation> {


    @Override
    public QosSustainabilityInformation mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String snssai = resultSet.getString("snssai");
        String tai = resultSet.getString("tai");
        Integer ranUeThrou = resultSet.getInt("ranUeThrou");
        Integer qosFlowRet = resultSet.getInt("qosFlowRet");

        return new QosSustainabilityInformation(ID, snssai, tai, ranUeThrou, qosFlowRet);
    }
}
