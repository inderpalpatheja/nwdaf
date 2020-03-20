package com.nwdaf.Analytics.Repository.Mapper;

import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEmobility.userLocationTable;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userLocationTableMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {


        userLocationTable locObj = new userLocationTable();

        locObj.setID(rs.getInt("ID"));
        locObj.setTai(rs.getString("Tai"));
        locObj.setCellID(rs.getString("cellID"));


        return locObj;

    }
}
