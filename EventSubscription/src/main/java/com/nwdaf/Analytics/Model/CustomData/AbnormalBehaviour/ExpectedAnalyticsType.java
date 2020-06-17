package com.nwdaf.Analytics.Model.CustomData.AbnormalBehaviour;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;


public enum ExpectedAnalyticsType {

    MOBILITY,
    COMMUN,
    MOBILITY_AND_COMMUN;

    private static final Map<ExpectedAnalyticsType, List<ExceptionId>> excepIds;

    static
    {
        Map<ExpectedAnalyticsType, List<ExceptionId>> map = new HashMap<>();

        List<ExceptionId> mobilityList = new ArrayList<>();
        mobilityList.add(ExceptionId.UNEXPECTED_UE_LOCATION);
        mobilityList.add(ExceptionId.PING_PONG_ACROSS_CELLS);


        List<ExceptionId> communList = new ArrayList<>();
        communList.add(ExceptionId.UNEXPECTED_LONG_LIVE_FLOW);
        communList.add(ExceptionId.UNEXPECTED_LARGE_RATE_FLOW);
        communList.add(ExceptionId.UNEXPECTED_WAKEUP);
        communList.add(ExceptionId.SUSPICION_OF_DDOS_ATTACK);
        communList.add(ExceptionId.WRONG_DESTINATION_ADDRESS);
        communList.add(ExceptionId.TOO_FREQUENT_SERVICE_ACCESS);
        communList.add(ExceptionId.ABNORMAL_TRAFFIC_VOLUME);
        communList.add(ExceptionId.UNEXPECTED_RADIO_LINK_FAILURES);


        List<ExceptionId> mobility_communList = new ArrayList<>();
        mobility_communList.addAll(mobilityList);
        mobility_communList.addAll(communList);

        map.put(MOBILITY, mobilityList);
        map.put(COMMUN, communList);
        map.put(MOBILITY_AND_COMMUN, mobility_communList);

        excepIds = Collections.unmodifiableMap(map);
    }

    public List<ExceptionId> getExceptionIds()
    { return excepIds.get(this); }

}
