package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilityInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilityInformationMapper implements RowMapper {

    int loadVal;

    public QosSustainabilityInformationMapper()
    { loadVal = 0; }

    public QosSustainabilityInformationMapper(QosType qosType)
    { loadVal = (qosType == QosType.RAN_UE_THROUGHPUT) ? 1 : 2; }



    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        QosSustainabilityInformation qosSustainabilityInformation = new QosSustainabilityInformation();

        qosSustainabilityInformation.setPlmnID(resultSet.getString("plmnID"));
        qosSustainabilityInformation.setSnssais(resultSet.getString("snssais"));

        if(loadVal == 1 || loadVal == 0)
        { qosSustainabilityInformation.setRanUeThroughput(resultSet.getInt("ranUeThroughput")); }

        if(loadVal == 2 || loadVal == 0)
        { qosSustainabilityInformation.setQosFlowRetain(resultSet.getInt("qosFlowRetain")); }

        return qosSustainabilityInformation;
    }
}
