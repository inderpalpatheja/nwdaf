package com.nwdaf.Analytics;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EvenSubscriptionApplication {

    public static final Logger logger = Logger.getLogger(EvenSubscriptionApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EvenSubscriptionApplication.class, args);

        BasicConfigurator.configure();
        logger.debug("In EventSubscriptionApplication Class");

    }


}
