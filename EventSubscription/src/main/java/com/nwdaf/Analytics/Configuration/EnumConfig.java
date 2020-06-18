package com.nwdaf.Analytics.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EnumConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(new StringToNwdafEvent());
        registry.addConverter(new StringToNFType());
        registry.addConverter(new StringToExceptionId());
        registry.addConverter(new StringToExpectedAnalyticsType());
        registry.addConverter(new StringToNetworkPerfType());
        registry.addConverter(new StringToSnssai());
        registry.addConverter(new StringToTai());
    }
}
