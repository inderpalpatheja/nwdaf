package com.nwdaf.AMF;
//import ch.qos.logback.classic.BasicConfigurator;

import ch.qos.logback.classic.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ImageBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


@SpringBootApplication
public class AmfApplication extends Functionality {

    private static final Logger logger = LoggerFactory.getLogger(AmfApplication.class);
    //  public static List<String> subIDList = new ArrayList<>();

    public static HashMap<String, String> unSubURIMap = new HashMap<>();

    /* public static List<String> getSubIDList() {
           return subIDList;
       } */


    public static HashMap<String, String> getUnSubURIMap() {
        return unSubURIMap;
    }

    Random rand = new Random();

    String[] snssaisArray = {"AMF", "SMF", "PCF"};
    Integer[] eventIDArray = {0, 5};

    Random random = new Random();


    public void test(int subList, int eventID) throws Exception {

        //  System.out.println("Subscribers Count - " + subList);
        int MCC = random.nextInt(900) + 100;
        // System.out.println("MCC - " + MCC);
        int MNC = random.nextInt(900) + 100;
        // System.out.println("MNC - " + MNC);
        int IMSI = random.nextInt(900000000) + 100000000;
        // System.out.println("IMSI" + IMSI);

        String supi = String.valueOf(MCC) + String.valueOf(MNC) + String.valueOf(IMSI);

        //  System.out.println("SUPI ---- > " + supi);


        if (0 == eventID) {
            for (int i = 0; i < subList; i++) {

                // int eventIDvalue = eventIDArray[rand.nextInt(eventIDArray.length)];
                System.out.println("even-ID-value = " + eventID);

                String subId = subscribe(0,
                        "http://localhost:8082/notify",
                        snssaisArray[rand.nextInt(snssaisArray.length)],
                        1,
                        0,
                        supi,
                        rand.nextInt(30) + 40); /// rand.nextInt( high - low ) + low

                //  System.out.println("Un-sub-URI : " + subId);
                System.out.println("Length - " + subId.length());
                String ID = subId.substring(subId.length() - 36, subId.length());
                System.out.println("ID - " + ID);
                String URI = subId.substring(0, subId.length() - 37);
                System.out.println("URI - " + URI);

                //unSubURIMap.put(,ID);

                unSubURIMap.put(ID, URI);

                // subIDList.add(subId);
            }
        }
        if (5 == eventID) {
            for (int i = 0; i < subList; i++) {

                // int eventIDvalue = eventIDArray[rand.nextInt(eventIDArray.length)];
                System.out.println("even-ID-value = " + eventID);

                String subId = subscribe(5,
                        "http://localhost:8082/notify",
                        snssaisArray[rand.nextInt(snssaisArray.length)],
                        1,
                        0,
                        supi,
                        rand.nextInt(30) + 40); /// rand.nextInt( high - low ) + low

                //  System.out.println("Un-sub-URI : " + subId);
                System.out.println("Length - " + subId.length());
                String ID = subId.substring(subId.length() - 36, subId.length());
                System.out.println("ID - " + ID);
                String URI = subId.substring(0, subId.length() - 37);
                System.out.println("URI - " + URI);

                //unSubURIMap.put(,ID);

                unSubURIMap.put(ID, URI);

                // subIDList.add(subId);
            }
        }
        if (3 == eventID) {
            System.out.println("even-ID-value = " + eventID);
            System.out.println("Qos_changes");
        }

    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(AmfApplication.class, args);
        logger.debug("In AmfApplication class");

        AMFController amfController = new AMFController();

        // Reading subscriber list From file
        ApplicationPropertiesValue properties = new ApplicationPropertiesValue();
        List<String> propValuesList = properties.getPropValues();

        int subListInt = Integer.parseInt(propValuesList.get(0));
        int eventIDInt = Integer.parseInt(propValuesList.get(1));

        AmfApplication amfApplication = new AmfApplication();

        // amfController.testJSONData();

        amfApplication.test(subListInt, eventIDInt);

       /* for (int i = 0; i < amfController.getCorrelationIDList().size(); i++) {
            amfController.sendDataForUEMobility("http://localhost:8081/Namf_EventExposure_Notify",
                    amfController.getCorrelationIDList().get(i));


            if(amfController.getCorrelationIDList().size() !=0){
                amfController.sendData("http://localhost:8081/Nnrf_NFManagement_NFStatusNotify",
                        amfController.getCorrelationIDList().get(i));
            }

        }*/


     /*   while (true) {
            //Thread.sleep(5000);

            amfApplication.test(subList);
            //  System.out.println("Main correaltionList - " + amfController.getCorrelationIDList().size());

         /*   for (int i = 0; i < amfController.getCorrelationIDList().size(); i++) {
                amfController.sendData("http://localhost:8081/Nnrf_NFManagement_NFStatusNotify",
                        amfController.getCorrelationIDList().get(i));
                // amfApplication.unsubscribe(subIDList.get(i));
            }

            for (int i = 0; i < amfController.getCorrelationIDList().size(); i++) {
                amfController.sendDataForUEMobility("http://localhost:8081/Namf_EventExposure_Notify",
                        amfController.getCorrelationIDList().get(i));


                if(amfController.getCorrelationIDList().size() !=0){
                    amfController.sendData("http://localhost:8081/Nnrf_NFManagement_NFStatusNotify",
                            amfController.getCorrelationIDList().get(i));
                }

            }

            Thread.sleep(20 * 1000);
        } */


        // AMFController amfController = new AMFController();

        // TestHarshit testHarshit = new TestHarshit();
        // testHarshit.fetchData();


    }
}
