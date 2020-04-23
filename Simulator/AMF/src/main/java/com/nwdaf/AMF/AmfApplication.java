package com.nwdaf.AMF;
//import ch.qos.logback.classic.BasicConfigurator;

import com.nwdaf.AMF.model.EventID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;


@SpringBootApplication
public class AmfApplication extends Functionality {

    private static final Logger logger = LoggerFactory.getLogger(AmfApplication.class);




    String[] snssaisArray = {"AMF", "SMF", "PCF"};
    String qosLoadType[] = { "ranUeThroughput", "qosFlowRetain" };
    int[] eventIDArray = {EventID.LOAD_LEVEL_INFORMATION.ordinal(), EventID.QOS_SUSTAINABILITY.ordinal(), EventID.UE_MOBILITY.ordinal()};

    Random random = new Random();


    public void test(int subList, int eventID, AMFController amfController) throws Exception {



        do
        {
            if (eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal() || eventID == 10) {

                while(true)
                {
                    for (int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + EventID.LOAD_LEVEL_INFORMATION.toString());
                        String subscriptionID = subscribe(EventID.LOAD_LEVEL_INFORMATION);
                    }


                    for (int j = 0; j < amfController.getCorrelationIDList_LOAD_LEVEL_INFORMATION().size(); j++) {

                        amfController.sendData("http://localhost:8081/Nnrf_NFManagement_NFStatusNotify",
                                amfController.getCorrelationIDList_LOAD_LEVEL_INFORMATION().get(j));
                        // amfApplication.unsubscribe(subIDList.get(i));
                    }


                    //Thread.sleep(3 * 1000);
                    //Thread.sleep(20 * 1000);

                    if(eventID == 10)
                    { break; }
                }
            }


            if (eventID == EventID.UE_MOBILITY.ordinal() || eventID == 10) {


                while(true)
                {

                    for (int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + EventID.UE_MOBILITY.toString());
                        String subscriptionID = subscribe(EventID.UE_MOBILITY);
                    }



                       for (int j = 0; j < amfController.getCorrelationIDList_UE_MOBILITY().size(); j++) {

                            System.out.println("Sending data for UE :: correlationListSIze " + amfController.getCorrelationIDList_UE_MOBILITY().size());
                            System.out.println("correlationID  - " + amfController.getCorrelationIDList_UE_MOBILITY().get(j));
                            amfController.sendDataForUEMobility("http://localhost:8081/Namf_EventExposure_Notify",
                                    amfController.getCorrelationIDList_UE_MOBILITY().get(j));

                        }



                    //Thread.sleep(3 * 1000);

                    if(eventID == 10)
                    { break; }
                }

            }

            if (eventID == EventID.QOS_SUSTAINABILITY.ordinal() || eventID == 10) {

                while(true)
                {
                    for (int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + EventID.QOS_SUSTAINABILITY.toString());
                        String subscriptionID = subscribe(EventID.QOS_SUSTAINABILITY);
                    }


                    for (int j = 0; j < amfController.getCorrelationIDList_QOS_SUSTAINABILITY().size(); j++) {


                        amfController.sendData("http://localhost:8081/Noam_NFManagement_NFStatusNotify", qosLoadType[random.nextInt(qosLoadType.length)],
                                amfController.getCorrelationIDList_QOS_SUSTAINABILITY().get(j));

                    }


                    //Thread.sleep(3 * 1000);

                    if(eventID == 10)
                    { break; }
                }
            }



            if (eventID == EventID.SERVICE_EXPERIENCE.ordinal() || eventID == 10) {

                while(true)
                {
                    for (int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + EventID.SERVICE_EXPERIENCE.toString());
                        String subscriptionID = subscribe(EventID.SERVICE_EXPERIENCE);
                    }


                    for (int j = 0; j < amfController.getCorrelationIDList_SERVICE_EXPERIENCE().size(); j++) {
                        amfController.sendServiceExperienceData("http://localhost:8081/Nnf_EventExposure_Notify", amfController.getCorrelationIDList_SERVICE_EXPERIENCE().get(j));
                    }


                    //Thread.sleep(3 * 1000);

                    if(eventID == 10)
                    { break; }
                }
            }


            Thread.sleep(3 * 1000);

        } while(eventID == 10);




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


        amfApplication.test(subListInt, eventIDInt, amfController);

    }
}
