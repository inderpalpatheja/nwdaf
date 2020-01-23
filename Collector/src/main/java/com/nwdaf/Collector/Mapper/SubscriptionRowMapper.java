package com.nwdaf.Collector.Mapper;


import com.nwdaf.Collector.model.CollectorDataModel;
import com.nwdaf.Collector.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        Namf_EventExposure_Subscribe namf_eventExposure_subscribe = new Namf_EventExposure_Subscribe();

       // collectorDataModel.setSnSaai(resultSet.getString("snSaai"));
       // collectorDataModel.setLOAD_LEVEL_INFORMATION(resultSet.getInt("load_level_info"));
        //collectorDataModel.setEventId(resultSet.getInt("event_id"));

        namf_eventExposure_subscribe.setNfId(resultSet.getInt("nfId"));
        namf_eventExposure_subscribe.setEventId(resultSet.getInt("eventId"));
        namf_eventExposure_subscribe.setNotificationTargetAddress(resultSet.getString("notificationTargetAddress"));
        namf_eventExposure_subscribe.setCorrelationId(resultSet.getInt("correlationId"));
        namf_eventExposure_subscribe.setCorrelationId(resultSet.getInt("correlationId"));
        namf_eventExposure_subscribe.setUnSubCorrelationId(resultSet.getInt("unSubCorrelationId"));

        return namf_eventExposure_subscribe;
    }
}
