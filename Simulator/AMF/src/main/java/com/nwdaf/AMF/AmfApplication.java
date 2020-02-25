package com.nwdaf.AMF;
//import ch.qos.logback.classic.BasicConfigurator;

import ch.qos.logback.classic.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class AmfApplication extends Functionality {

    private static final Logger logger = LoggerFactory.getLogger(AmfApplication.class);
    public static  List<String> subIDList = new ArrayList<>();

    Random rand = new Random();


    public void test() throws Exception {

        for (int i = 0; i < 10000; i++) {
         String subId =    subscribe(0,
                    "http://localhost:8082/notify",
                    "AMF",
                    1,
                    0,
                    rand.nextInt(90) + 10); /// rand.nextInt( high - low ) + low

            subIDList.add(subId);
        }
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(AmfApplication.class, args);
        logger.debug("In AmfApplication class");
        AmfApplication amfApplication = new AmfApplication();
        amfApplication.test();
        AMFController amfController = new AMFController();

        while (true) {
            Thread.sleep(5000);
            System.out.println("Main correaltionList - " + amfController.getCorrelationIDList().size());

         for(int i = 0; i<amfController.getCorrelationIDList().size(); i++){
             amfController.sendData("http://localhost:8081/Nnrf_NFManagement_NFStatusNotify",
                     amfController.getCorrelationIDList().get(i));
            // amfApplication.unsubscribe(subIDList.get(i));
         }

        }

    }
}
