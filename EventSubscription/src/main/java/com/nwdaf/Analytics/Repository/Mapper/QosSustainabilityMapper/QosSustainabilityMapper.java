package com.nwdaf.Analytics.Repository.Mapper.QosSustainabilityMapper;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainability;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QosSustainabilityMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        QosSustainability qosSustainability = new QosSustainability();

        qosSustainability.setID(resultSet.getInt("ID"));
        qosSustainability.setSubscriptionID(resultSet.getString("subscriptionID"));
        qosSustainability.set_5Qi(resultSet.getInt("5Qi"));
        qosSustainability.setPlmnID(resultSet.getString("plmnID"));
        qosSustainability.setTac(resultSet.getString("tac"));

        return qosSustainability;
    }
}
