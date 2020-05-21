package com.nwdaf.AMF;
//import ch.qos.logback.classic.BasicConfigurator;

import com.nwdaf.AMF.model.NwdafEvent;
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
    int[] eventIDArray = { NwdafEvent.LOAD_LEVEL_INFORMATION.ordinal(), NwdafEvent.QOS_SUSTAINABILITY.ordinal(), NwdafEvent.UE_MOBILITY.ordinal()};

    Random random = new Random();


    public void test(int subList, int event, AMFController amfController) throws Exception {



        do
        {
            if (event == NwdafEvent.LOAD_LEVEL_INFORMATION.ordinal() || event == 10) {

                while(true)
                {
                    for (int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + NwdafEvent.LOAD_LEVEL_INFORMATION.toString());
                        String subscriptionID = subscribe(NwdafEvent.LOAD_LEVEL_INFORMATION);
                    }


                    for (int j = 0; j < amfController.getCorrelationIDList_LOAD_LEVEL_INFORMATION().size(); j++) {

                        amfController.sendData("https://localhost:8081/Nnrf_NFManagement_NFStatusNotify",
                                amfController.getCorrelationIDList_LOAD_LEVEL_INFORMATION().get(j));
                        // amfApplication.unsubscribe(subIDList.get(i));
                    }


                    //Thread.sleep(3 * 1000);
                    //Thread.sleep(20 * 1000);

                    if(event == 10)
                    { break; }
                }
            }


          /*  if (event == NwdafEvent.UE_MOBILITY.ordinal() || event == 10) {


                while(true)
                {

                    for (int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + NwdafEvent.UE_MOBILITY.toString());
                        String subscriptionID = subscribe(NwdafEvent.UE_MOBILITY);
                    }



                       for (int j = 0; j < amfController.getCorrelationIDList_UE_MOBILITY().size(); j++) {

                            System.out.println("Sending data for UE :: correlationListSIze " + amfController.getCorrelationIDList_UE_MOBILITY().size());
                            System.out.println("correlationID  - " + amfController.getCorrelationIDList_UE_MOBILITY().get(j));
                            amfController.sendDataForUEMobility("https://localhost:8081/Namf_EventExposure_Notify",
                                    amfController.getCorrelationIDList_UE_MOBILITY().get(j));

                        }



                    //Thread.sleep(3 * 1000);

                    if(event == 10)
                    { break; }
                }

            } */

            if (event == NwdafEvent.QOS_SUSTAINABILITY.ordinal() || event == 10) {

                while(true)
                {
                    for (int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + NwdafEvent.QOS_SUSTAINABILITY.toString());
                        String subscriptionID = subscribe(NwdafEvent.QOS_SUSTAINABILITY);
                    }


                    for (int j = 0; j < amfController.getCorrelationIDList_QOS_SUSTAINABILITY().size(); j++) {


                        amfController.sendData("https://localhost:8081/Noam_EventExposure_Notify", qosLoadType[random.nextInt(qosLoadType.length)],
                                amfController.getCorrelationIDList_QOS_SUSTAINABILITY().get(j));

                    }


                    //Thread.sleep(3 * 1000);

                    if(event == 10)
                    { break; }
                }
            }



            if (event == NwdafEvent.SERVICE_EXPERIENCE.ordinal() || event == 10) {

                while(true)
                {
                    for (int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + NwdafEvent.SERVICE_EXPERIENCE.toString());
                        String subscriptionID = subscribe(NwdafEvent.SERVICE_EXPERIENCE);
                    }


                    for (int j = 0; j < amfController.getCorrelationIDList_SERVICE_EXPERIENCE().size(); j++) {
                        amfController.sendServiceExperienceData("https://localhost:8081/Nnf_EventExposure_Notify", amfController.getCorrelationIDList_SERVICE_EXPERIENCE().get(j));
                    }


                    //Thread.sleep(3 * 1000);

                    if(event == 10)
                    { break; }
                }
            }


            if(event == NwdafEvent.NETWORK_PERFORMANCE.ordinal() || event == 10)
            {
                while(true)
                {
                    for(int i = 0;i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + NwdafEvent.NETWORK_PERFORMANCE.toString());
                        String subscriptionID = subscribe(NwdafEvent.NETWORK_PERFORMANCE);
                    }

                    for(int j = 0; j < amfController.getCorrelationIDList_NETWORK_PERFORMANCE().size(); j++)
                    { amfController.sendNetworkPerformanceData("https://localhost:8081/Noam_EventExposure_Notify", amfController.getCorrelationIDList_NETWORK_PERFORMANCE().get(j)); }


                    //Thread.sleep(3 * 1000);

                    if(event == 10)
                    { break; }
                }

            }


            if(event == NwdafEvent.USER_DATA_CONGESTION.ordinal() || event == 10)
            {
                while(true)
                {
                    for(int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + NwdafEvent.USER_DATA_CONGESTION.toString());
                        String subscriptionID = subscribe(NwdafEvent.USER_DATA_CONGESTION);
                    }

                    for(int j = 0; j < amfController.getCorrelationIDList_USER_DATA_CONGESTION().size(); j++)
                    { amfController.sendUserDataCongestionLevel("https://localhost:8081/Noam_EventExposure_Notify", amfController.getCorrelationIDList_USER_DATA_CONGESTION().get(j)); }

                    //Thread.sleep(3 * 1000);

                    if(event == 10)
                    { break; }
                }
            }


            if(event == NwdafEvent.ABNORMAL_BEHAVIOUR.ordinal() || event == 10)
            {
                while(true)
                {
                    for(int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + NwdafEvent.ABNORMAL_BEHAVIOUR.toString());
                        String subscriptionID = subscribe(NwdafEvent.ABNORMAL_BEHAVIOUR);
                    }

                    for(int j = 0; j < amfController.getCorrelationIDList_ABNORMAL_BEHAVIOUR().size(); j++)
                    { amfController.sendAbnormalBehaviourData("https://localhost:8081/Noam_EventExposure_Notify", amfController.getCorrelationIDList_ABNORMAL_BEHAVIOUR().get(j)); }

                    //Thread.sleep(3 * 1000);

                    if(event == 10)
                    { break; }
                }
            }


            if(event == NwdafEvent.UE_COMM.ordinal() || event == 10)
            {
                while(true)
                {
                    for(int i = 0; i < subList; i++)
                    {
                        System.out.println("Subscribing for EventID: " + NwdafEvent.UE_COMM.toString());
                        String subscriptionID = subscribe(NwdafEvent.UE_COMM);
                    }

                    for(int j = 0; j < amfController.getCorrelationIDList_UE_COMM().size(); j++)
                    { amfController.sendUeCommData("https://localhost:8081/Nsmf_EventExposure_Notify", amfController.getCorrelationIDList_UE_COMM().get(j)); }

                    Thread.sleep(3 * 1000);

                    if(event == 10)
                    { break; }
                }
            }


            Thread.sleep(3 * 1000);

        } while(event == 10);




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
