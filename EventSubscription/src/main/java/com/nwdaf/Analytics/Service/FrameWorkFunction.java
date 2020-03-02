package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Model.APIBuildInformation;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;


public class FrameWorkFunction {



    private HashMap<String, BigInteger> counterStats = new HashMap<String, BigInteger>();


    //Returns Counter Statistics
    public HashMap<String, BigInteger> getStats()
    {
        counterStats.put("Event_Subscriptions", Counters.getSubscriptions());
        counterStats.put("Event_UnSubscriptions", Counters.getUnSubscriptions());
        counterStats.put("Event_SubscriptionUpdates", Counters.getSubscriptionUpdates());
        counterStats.put("Event_SubscriptionNotifications", Counters.getSubscriptionNotifications());

        counterStats.put("Collector_Subscriptions", Counters.getCollectorSubscriptions());
        counterStats.put("Collector_SubscriptionNotifications", Counters.getCollectorSubscriptionNotifications());
        counterStats.put("Collector_AnalyticsSubscriptions", Counters.getAnalyticsSubscriptions());
        counterStats.put("Collector_AnalyticsNotifications", Counters.getAnalyticsNotifications());

        return counterStats;
    }



    // Reset Counters
    public void restCounters() {
        Counters.reset();
    }






}
