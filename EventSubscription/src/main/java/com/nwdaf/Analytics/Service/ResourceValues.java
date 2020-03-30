package com.nwdaf.Analytics.Service;

import org.springframework.beans.factory.annotation.Value;

public class ResourceValues {

    @Value("${spring.MAIN.url}")
    String URI;

    @Value("${spring.NRF_NotificationTarget.Url}")
    String notificationTargetUrlForNRF;

    @Value("${spring.AMF_NotificationTarget.Url}")
    String notificationTargetUrlForAMF;

    @Value("${spring.Simulator.testConnection}")
    String testConnect_URI;

    @Value("${spring.NRF.Subscribe.url}")
    String POST_NRF_URL;

    @Value("${spring.AMF.Subscribe.url}")
    String POST_AMF_URL;

    @Value("${spring.NRF.UnSubscribe.url}")
    String DELETE_NRF_URL;

    @Value("${spring.AMF.UnSubscribe.url}")
    String DELETE_AMF_URL;

}
