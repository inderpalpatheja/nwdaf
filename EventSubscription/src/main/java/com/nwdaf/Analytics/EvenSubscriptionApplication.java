package com.nwdaf.Analytics;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;


@SpringBootApplication
@EnableSwagger2
public class EvenSubscriptionApplication {

    private static final Logger logger = LoggerFactory.getLogger(EvenSubscriptionApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EvenSubscriptionApplication.class, args);

        BasicConfigurator.configure();
        logger.debug("In EventSubscriptionApplication Class");





    }
}
