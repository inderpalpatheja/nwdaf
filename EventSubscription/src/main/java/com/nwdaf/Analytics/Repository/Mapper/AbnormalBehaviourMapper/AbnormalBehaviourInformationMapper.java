package com.nwdaf.Analytics.Repository.Mapper.AbnormalBehaviourMapper;

import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AbnormalBehaviourInformationMapper implements RowMapper<AbnormalBehaviourInformation> {

    @Override
    public AbnormalBehaviourInformation mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ID = resultSet.getInt("ID");
        String supi = resultSet.getString("supi");
        Integer excepId = resultSet.getInt("excepId");
        Integer excepLevel = resultSet.getInt("excepLevel");

        return new AbnormalBehaviourInformation(ID, supi, excepId, excepLevel);
    }
}
