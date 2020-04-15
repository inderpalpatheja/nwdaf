package com.nwdaf.Analytics.Repository.Mapper.UEMobilityMapper;



import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UEMobilitySubscriptionDataMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        System.out.println("in nwdafUEMobilitySubscriptionDataMapper");

        UEMobilitySubscriptionData data = new UEMobilitySubscriptionData();

        System.out.println("Hello World");

        data.setID(resultSet.getInt("ID"));
        System.out.println(data.getID());
        data.setSupi(resultSet.getString("supi"));
        System.out.println(data.getSupi());
        data.setSubscriptionID(resultSet.getString("subscriptionID"));
        System.out.println(data.getSubscriptionID());

        System.out.println("In nwdafUEMobilitySubscriptionDataMapper - "+ data.getSubscriptionID());
        return data;
    }

}

