package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilityInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilityInformationMapper implements RowMapper {

    int loadVal;


    public QosSustainabilityInformationMapper(QosType qosType)
    { loadVal = (qosType == QosType.RAN_UE_THROUGHPUT) ? 1 : 2; }


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        QosSustainabilityInformation qosSustainabilityInformation = new QosSustainabilityInformation();

        qosSustainabilityInformation.setTai(resultSet.getString("tai"));
        qosSustainabilityInformation.setSnssai(resultSet.getString("snssai"));

        if(loadVal == 1 || loadVal == 0)
        { qosSustainabilityInformation.setRanUeThrou(resultSet.getInt("ranUeThrou")); }

        if(loadVal == 2 || loadVal == 0)
        { qosSustainabilityInformation.setQosFlowRet(resultSet.getInt("qosFlowRet")); }

        return qosSustainabilityInformation;
    }
}
