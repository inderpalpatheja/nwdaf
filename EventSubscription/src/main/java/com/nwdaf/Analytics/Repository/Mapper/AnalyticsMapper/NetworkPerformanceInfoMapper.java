package com.nwdaf.Analytics.Repository.Mapper.AnalyticsMapper;

import com.nwdaf.Analytics.Model.AnalyticsInformation.NetworkPerformanceInfo;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NetworkPerformanceInfoMapper implements RowMapper {

    final String message = "DATA FOUND";
    final Boolean data_status = Boolean.TRUE;

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        NetworkPerformanceInfo nwPerfInfo = new NetworkPerformanceInfo();

        nwPerfInfo.setSupi(resultSet.getString("supi"));

        Integer nwPerfType_oridinal = resultSet.getInt("nwPerfType");

        nwPerfInfo.setNwPerfType(NetworkPerfType.values()[nwPerfType_oridinal].toString());

        if(hasRelativeRatio(nwPerfType_oridinal))
        {
            nwPerfInfo.setThreshold_type(NetworkPerfThreshold.RELATIVE_RATIO);
            nwPerfInfo.setRelativeRatio(resultSet.getInt("relativeRatio"));
        }

        else
        {
            nwPerfInfo.setThreshold_type(NetworkPerfThreshold.ABSOLUTE_NUM);
            nwPerfInfo.setAbsoluteNum(resultSet.getInt("absoluteNum"));
        }

        nwPerfInfo.setMessage(message);
        nwPerfInfo.setData_status(data_status);

        return nwPerfInfo.getInfo();
    }

    public boolean hasRelativeRatio(Integer nwPerfType_oridinal)
    { return nwPerfType_oridinal == NetworkPerfType.GNB_ACTIVE_RATIO.ordinal() || nwPerfType_oridinal == NetworkPerfType.SESS_SUCC_RATIO.ordinal() || nwPerfType_oridinal == NetworkPerfType.HO_SUCC_RATIO.ordinal(); }


}
