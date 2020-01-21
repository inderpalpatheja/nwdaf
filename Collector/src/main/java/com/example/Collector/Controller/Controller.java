package com.example.Collector.Controller;


import com.example.Collector.Repository.CollectorRepository;
import com.example.Collector.model.CollectorDataModel;
import com.example.Collector.model.Namf_EventExposure.Namf_EventExposure_Notify;
import com.example.Collector.model.Namf_EventExposure.Namf_EventExposure_Subscribe;
import com.example.Collector.model.Namf_EventExposure.Namf_EventExposure_UnSubscribe;
import com.example.Collector.model.Nsmf_EventExposure.Nsmf_EventExposure_Notify;
import com.example.Collector.model.Nsmf_EventExposure.Nsmf_EventExposure_Subscribe;
import com.example.Collector.model.Nsmf_EventExposure.Nsmf_EventExposure_UnSubscribe;
import com.example.Collector.model.nestedModel.EventReportingInformationInformation;
import com.example.Collector.model.nestedModel.NotificationEventIdModel;
import com.example.Collector.model.nestedModel.TargetEventReporting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class Controller {


    JdbcTemplate jdbcTemplate;

    private int[] supi = {10, 20, 30, 40};
    public ArrayList<Integer> groupUEData = new ArrayList<Integer>();

    // Custom List for Sub [AMF]
    ArrayList<Namf_EventExposure_Subscribe> namf_eventExposure_subscribeArrayList = new ArrayList(
            Arrays.asList((new Namf_EventExposure_Subscribe(1, new TargetEventReporting(false),
                            "http://localhost:8080/getCollectorDetails")),
                    new Namf_EventExposure_Subscribe(2, new TargetEventReporting(false),
                            "http://localhost:8080/getCollectorDetails")));

    // Custom list for unsub [AMF]
    ArrayList<Namf_EventExposure_UnSubscribe> namf_eventExposure_unSubscribeArrayList = new ArrayList(
            Arrays.asList((new Namf_EventExposure_UnSubscribe(1))));


    // Custom List for SMF Sub
    ArrayList<Nsmf_EventExposure_Subscribe> nsmf_eventExposure_subscribeArrayList = new ArrayList(
            Arrays.asList(new Nsmf_EventExposure_Subscribe(1,
                    new TargetEventReporting(true),
                    "SMF_notificationTargetAddress + Notification Correlation ID",
                    new EventReportingInformationInformation(0,
                            1,
                            2,
                            3,
                            4,
                            5)),
                    new Nsmf_EventExposure_Subscribe(2,
                            new TargetEventReporting(false),
                            "SMF_notificationTargetAddress + Notification Correlation ID2",
                            new EventReportingInformationInformation(0,
                                    11,
                                    12,
                                    13,
                                    14,
                                    15)),
                    new Nsmf_EventExposure_Subscribe(3,
                            new TargetEventReporting(true),
                            "SMF_notificationTargetAddress + Notification Correlation ID3",
                            new EventReportingInformationInformation(00,
                                    21,
                                    22,
                                    23,
                                    24,
                                    25))

                    )
    );


    //Custom List for SMF UnSub
    ArrayList<Nsmf_EventExposure_UnSubscribe> nsmf_eventExposure_unSubscribeslist = new ArrayList(Arrays.asList(

            new Nsmf_EventExposure_UnSubscribe("SMF-001"),
            new Nsmf_EventExposure_UnSubscribe("SMF-002"),
            new Nsmf_EventExposure_UnSubscribe("SMF-003")
            ));

    // Custom List for SMF notify;
    ArrayList<Nsmf_EventExposure_Notify> nsmf_eventExposure_notifiesList = new ArrayList(
            Arrays.asList(
                    new Nsmf_EventExposure_Notify(1,
                            "xyz"
                            , "UE1",
                            "A1",
                            "2012")));

    // Custom list for [AMF] notify -> just to check the format of JSON;
    ArrayList<Namf_EventExposure_Notify> namf_eventExposure_notifiesList = new ArrayList(Arrays.asList(
            new Namf_EventExposure_Notify(1, "ABC",
                    new NotificationEventIdModel(1, "ABC"), "2123421")
    ));


    @Autowired
    CollectorRepository CollectorRepository;


    // get method to get AMF sub
    @RequestMapping("/Namf_EventExposure_Subscribe")
    public List<Namf_EventExposure_Subscribe> getNamf_EventExposure_Subscribe() {

        return namf_eventExposure_subscribeArrayList;
    }

    // get method of get AMF UnSub
    @RequestMapping("/Namf_EventExposure_UnSubscribe")
    public List<Namf_EventExposure_UnSubscribe> getNamf_EventExposure_UnSubscribe() {
        groupUEData.add(1);
        groupUEData.add(2);
        groupUEData.add(3);
        return namf_eventExposure_unSubscribeArrayList;
    }

    // Checking format of AMF notificaion JSON
    @RequestMapping("/Namf_EventExposure_Notify")
    public List<Namf_EventExposure_Notify> getNamf_EventExposure_Unsubscribe() {
        return namf_eventExposure_notifiesList;
    }


    // Post method for [AMF] Notification
    @RequestMapping(method = RequestMethod.POST, value = "/Namf_EventExposure_Notify")
    public String amfNotifcaion(@RequestBody Namf_EventExposure_Notify namf_eventExposure_notify) {
        if (namf_eventExposure_notify.getAMF_ID() != 0
                && namf_eventExposure_notify.getTimeStamp() != null
                && namf_eventExposure_notify.getNotificaionCorrelationInformation() != null
                && namf_eventExposure_notify.getEventId() != null) {
            System.out.println("NOTIFICATION SEND ! [AMF]");
            return " Notification Send ! [AMF]";
        } else {
            System.out.println("BAD PARAMETER ! [AMF]");
            return " Bad Parameter! [AMF]";
        }

    }

    // get method for SMF sub
    @RequestMapping("Nsmf_EventExposure_Subscribe")
    public List<Nsmf_EventExposure_Subscribe> getNsmf_EventExposure_Subscribe() {
        return nsmf_eventExposure_subscribeArrayList;
    }

    // get method for SMF unSub
    @RequestMapping("/Nsmf_EventExposure_UnSubscribe")
    public List<Nsmf_EventExposure_UnSubscribe> getNsmf_EventExposure_UnSubscribe() {
        return nsmf_eventExposure_unSubscribeslist;
    }
    // Checking format of SMF notificaion JSON
    @RequestMapping("/Nsmf_EventExposure_Notify")
    public List<Nsmf_EventExposure_Notify> getNsmf_EventExposure_Unsubscribe() {
        return nsmf_eventExposure_notifiesList;
    }


    // Post method of [SMF] Notification
    @RequestMapping(method = RequestMethod.POST, value = "/Nsmf_EventExposure_Notify")
    public String smfNotification(@RequestBody Nsmf_EventExposure_Notify nsmf_eventExposure_notify) {
        if (nsmf_eventExposure_notify.getEventId() != 0 &&
                nsmf_eventExposure_notify.getNotificaitonCorrelationInformation() != null &&
                nsmf_eventExposure_notify.getPDPSessionId() != null &&
                nsmf_eventExposure_notify.getPDPSessionId() != null &&
                nsmf_eventExposure_notify.getTimeStamp() != null &&
                nsmf_eventExposure_notify.getUE_ID() != null) {
            System.out.println("Notification Send [SMF]!");
            return "Notification Send ! [SMF]";
        } else {
            System.out.println("Bad Parameter! [SMf]");
            return "Bad Parameter ! [SMF]";
        }
    }

    @RequestMapping("/getCollectorDetails")
    public List<CollectorDataModel> getAllAnalyticsInfoData() {
        return CollectorRepository.collectorDetails();
    }


    // POST method
    @RequestMapping(method = RequestMethod.POST, value = "/postCollectorDetails")
    public ResponseEntity<String> createUser(@RequestBody CollectorDataModel collectorDataModel) throws SQLIntegrityConstraintViolationException {

        if (CollectorRepository.findByEventId(collectorDataModel.getEventId()) != null) {
            return new ResponseEntity<String>("Duplicate Entry ID." + collectorDataModel.getEventId(), HttpStatus.IM_USED);
        }

        CollectorRepository.saveInfo(collectorDataModel);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // PUT method
    @RequestMapping(method = RequestMethod.PUT, value = "/putCollectorDetails")
    public ResponseEntity<?> updateCollector(@RequestBody CollectorDataModel collectorDataModel) {
        if (CollectorRepository.findByEventId(collectorDataModel.getEventId()) == null) {
            return new ResponseEntity<String>("Event ID." + collectorDataModel.getEventId() + " Not Found", HttpStatus.NOT_FOUND);
        }

        CollectorRepository.updateCollectorDetails(collectorDataModel);

        return new ResponseEntity<CollectorDataModel>(collectorDataModel, HttpStatus.OK);
    }

    // DELETE METHOD
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteCollectorDetails/{id}")
    public ResponseEntity<?> deleteCollector(@PathVariable("id") Integer event_id) {
        CollectorDataModel collectorDataModel = CollectorRepository.findByEventId(event_id);

        if (collectorDataModel == null) {
            return new ResponseEntity<String>("Event ID." + event_id + " Not found", HttpStatus.NOT_FOUND);
        }

        CollectorRepository.deleteCollectorByEventId(event_id);

        return new ResponseEntity<CollectorDataModel>(HttpStatus.NO_CONTENT);
    }

}
