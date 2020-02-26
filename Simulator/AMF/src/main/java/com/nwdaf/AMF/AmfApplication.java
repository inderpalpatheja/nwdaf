package com.nwdaf.AMF;
//import ch.qos.logback.classic.BasicConfigurator;

import ch.qos.logback.classic.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@SpringBootApplication
public class AmfApplication extends Functionality {

    private static final Logger logger = LoggerFactory.getLogger(AmfApplication.class);
    public static List<String> subIDList = new ArrayList<>();


    Random rand = new Random();


    public void test(int subList) throws Exception {

        //  System.out.println("Subscribers Count - " + subList);

        for (int i = 0; i < subList; i++) {
            String subId = subscribe(0,
                    "http://localhost:8082/notify",
                    "AMF",
                    1,
                    0,
                    rand.nextInt(30) + 40); /// rand.nextInt( high - low ) + low

            subIDList.add(subId);
        }
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(AmfApplication.class, args);
        logger.debug("In AmfApplication class");

        AMFController amfController = new AMFController();


        // Reading subscriber list From file
        ApplicationPropertiesValue properties = new ApplicationPropertiesValue();
        String subcout = properties.getPropValues();

        int subList = Integer.parseInt(subcout);

        AmfApplication amfApplication = new AmfApplication();
        amfApplication.test(subList);

        while (true) {
            Thread.sleep(5000);
            //  System.out.println("Main correaltionList - " + amfController.getCorrelationIDList().size());

            for (int i = 0; i < amfController.getCorrelationIDList().size(); i++) {
                amfController.sendData("http://localhost:8081/Nnrf_NFManagement_NFStatusNotify",
                        amfController.getCorrelationIDList().get(i));
                // amfApplication.unsubscribe(subIDList.get(i));
            }

        }

    }
}
