package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilityInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilityInformationMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        QosSustainabilityInformation qosSustainabilityInformation = new QosSustainabilityInformation();

        qosSustainabilityInformation.setSnssais(resultSet.getString("snssais"));
        qosSustainabilityInformation.setRanUeThroughput(resultSet.getInt("ranUeThroughput"));
        qosSustainabilityInformation.setQosFlowRetain(resultSet.getInt("qosFlowRetain"));

        return qosSustainabilityInformation;
    }
}
