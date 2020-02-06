package com.nwdaf.Analytics;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication
public class EvenSubscriptionApplication {
    static Logger log = Logger.getLogger(EvenSubscriptionApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(EvenSubscriptionApplication.class, args);


       // Nnwdaf_controller.TestThread testThread = new Nnwdaf_controller.TestThread();
        //testThread.start();



//        Logger logger = LogManager.getLogger("CONSOLE_JSON_APPENDER");
  //      logger.debug("Debug message");

    }



}
