package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnectionForUEMobility;
import com.nwdaf.Analytics.Model.AMFModel.AMFModel;
import com.nwdaf.Analytics.Model.AnalyticsInformation.QosSustainabilityInfo;
import com.nwdaf.Analytics.Model.AnalyticsInformation.ServiceExperienceInfo;
import com.nwdaf.Analytics.Model.CustomData.*;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.EventSubscription;
import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.NetworkArea.NetworkAreaInfo;
import com.nwdaf.Analytics.Model.NetworkArea.PlmnId;
import com.nwdaf.Analytics.Model.NetworkArea.Tai;
import com.nwdaf.Analytics.Model.Nnrf.Nnrf_Model;
import com.nwdaf.Analytics.Model.NotificationData;
import com.nwdaf.Analytics.Model.NotificationFormat.*;
import com.nwdaf.Analytics.Model.NwdafEvent;
import com.nwdaf.Analytics.Model.QosNotificationData;
import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourInformation;
import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.AbnormalBehaviour.AbnormalBehaviourSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceInformation;
import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionData;
import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceInformation;
import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.SubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.NetworkPerformance.NetworkPerformanceSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilityInformation;
import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.ServiceExperience.ServiceExperienceSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.EventConnectionUE;
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.NnwdafEventsSubscriptionUEmobility;
import com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE.UEmobilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionModel;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UserLocation;
import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.UeComm.UeCommSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionInformation;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.UserDataCongestion.UserDataCongestionSubscriptionTable;
import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;


import static com.nwdaf.Analytics.Controller.Nnwdaf_Controller.EventCounter;
import static java.lang.System.out;
import static org.springframework.http.HttpHeaders.USER_AGENT;

public class BusinessLogic extends ResourceValues {


    @Autowired
    Nnwdaf_Repository repository;
    //checking

    private static final Logger logger = LoggerFactory.getLogger(BusinessLogic.class);

    Set<String> subID_SET = new HashSet<>();
    public int subThValue = 0;
    public int currentLoadLevel = 0;


    //UserLocation userLocation = new UserLocation();




    protected Object checkForData_LoadLevelInformation(EventSubscription eventSubscription, boolean getAnalytics) throws JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        String snssais = eventSubscription.getSnssais().get(0).toString();


        if (getAnalytics) {

            List<EventConnection> snssaisDataList = repository.checkForData(snssais, getAnalytics);

            // if data is not found -> calling collector function to collect data
            if (snssaisDataList == null || snssaisDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                Object obj = collectDataForLoadLevelInformation(eventSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // Adding snssais into database
                repository.add_data_into_load_level_table(eventSubscription.getSnssais().get(0).toString());

                EventConnection eventConnection = new EventConnection();
                eventConnection.setMessage("Data not Found for " + eventSubscription.getSnssais().get(0).toString());
                eventConnection.setCurrentLoadLevelInfo(0);
                eventConnection.setSnssai(eventSubscription.getSnssais().get(0).toString());
                eventConnection.setDataStatus(false);

                logger.debug("snssais: " + eventSubscription.getSnssais().get(0).toString() + "\n" +
                        " CurrentLoadLevelInfo: " + eventConnection.getCurrentLoadLevelInfo() + "\n" +
                        "DataStatus:  " + eventConnection.getDataStatus());

                //throw new NullPointerException("Data not found!");
                //return eventConnection;
                return new ResponseEntity<EventConnection>(eventConnection, HttpStatus.NOT_FOUND);


            } else {
                // flag to check if getAnalytics ref count will be updated or not
                //flag = 1;
                Object obj = collectDataForLoadLevelInformation(eventSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // if Data found returning JSON
                //return snssaisDataList;
                return new ResponseEntity(snssaisDataList, HttpStatus.OK);
            }

        } else {

            Object obj = collectDataForLoadLevelInformation(eventSubscription, getAnalytics);

            if (obj instanceof ResponseEntity) {
                return obj;
            }
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }



    protected Object collectDataForLoadLevelInformation(EventSubscription eventSubscription, boolean getAnalytics) throws JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        String snssais = eventSubscription.getSnssais().get(0).toString();

        if (!repository.snssaisExists_SliceLoadLevelSubscriptionTable(snssais)) {


            // Generating CorrelationID
            UUID correlationId = FrameWorkFunction.getUniqueID();
            // if Event Id is UE-Mobility nwdaf will subscribe to AMF;
         /*   if (nnwdafEventsSubscription.getEventID().toString() == "UE_MOBILITY") {
                subscribeAMFfromNWDAF(nnwdafEventsSubscription, correlationID, getAnalytics);
            } */


            Nnrf_Model nnrfModel = new Nnrf_Model();

            // Adding correlationID to object and send to SIMULATOR
            nnrfModel.setCorrelationId(String.valueOf(correlationId));
            // Updated URL For NWDAF to Subscribe
            // updated_POST_NRF_URL = POST_NRF_URL + "/" + correlationID;
            // POST_NRF_URL = NRF URL ------> [ Reading from ]application.properties
            try {

                URL obj = new URL(POST_NRF_URL);

                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");

                // Function To Send CorrelationID to SIMULATOR
                int responseCode = send_data_receieve_response(nnrfModel, con);

                if (responseCode != HttpStatus.OK.value()) {
                    throw new IOException();
                }

                responseHandler_LoadLevelInformation(eventSubscription, responseCode, correlationId, con, getAnalytics);

                if (con != null) {
                    con.disconnect();
                }

                EventCounter[eventSubscription.getEvent().ordinal()].incrementSubscriptionsSent();

            } catch (IOException ex) {

                ErrorCounters.incrementIOException();
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }



            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return correlationId;
        }

        else if(!getAnalytics)
        { repository.increaseRefCountSliceLoadLevel(eventSubscription.getSnssais().get(0).toString()); }

        return null;
    }


    /**
     * Function for UEMobility Data;
     */
    private Object subscribeAMFfromNWDAF(EventSubscription eventSubscription,
                                         UUID correlationId, boolean getAnalytics) {
        try {

            URL obj = new URL(POST_AMF_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            // Function To Send CorrelationId to SIMULATOR
            AMFModel amfModel = new AMFModel();
            int responseCode = send_data_receieve_response_AMF(amfModel, con);

            if (responseCode != HttpStatus.OK.value()) {
                throw new Exception();
            }

            responseHandler_LoadLevelInformation(eventSubscription, responseCode, correlationId, con, getAnalytics);

            if (con != null) {
                con.disconnect();
            }

        } catch (Exception ex) {
            return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
        }
        return null;
    }

    // For UE-Mobility
    private int send_data_receieve_response_AMF(AMFModel amfModel, HttpURLConnection con) throws JSONException, IOException {

        JSONObject json = new JSONObject();

        json.put("eventID", NwdafEvent.UE_MOBILITY.ordinal());
        json.put("correlationId", amfModel.getCorrelationId());
        json.put("unSubCorrelationId", amfModel.getUnSubCorrelationId());


        logger.info("correlationId:  " + amfModel.getCorrelationId() + "\n" +
                "unSubCorrelationId", amfModel.getUnSubCorrelationId());


        // notificationTargetUrl = Namf_EventExposure_Notify
        json.put("notificationTargetAddress", notificationTargetUrlForAMF);

        // For POST only - START
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END
        int responseCode = con.getResponseCode();
        String responseMessage = con.getResponseMessage();
        System.out.println("\n");

        return responseCode;
    }



    protected void responseHandler_LoadLevelInformation(EventSubscription eventSubscription,
                                                        int responseCode, UUID correlationId,
                                                        HttpURLConnection con, boolean getAnalytics) throws IOException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            try {

                SliceLoadLevelSubscriptionTable slice = new SliceLoadLevelSubscriptionTable();
                slice.setCorrelationId(String.valueOf(correlationId));
                slice.setSubscriptionId(response.toString());
                slice.setSnssai(eventSubscription.getSnssais().get(0).toString());

                if (getAnalytics) {
                    if (!repository.snssaisExists_SliceLoadLevelSubscriptionTable(slice.getSnssai())) {
                        repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(slice, true);
                    }
                } else {
                    repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(slice, false);
                }

            } catch (Exception e) {
                out.println(e.getMessage());
            }
            in.close();

        } else {

            logger.error(" POST request not worked");
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }


    /**
     * @param subscriptionID
     * @param snssais
     * @param loadLevelThreshold
     * @desc function to add values in subscription data table
     */
    protected void add_values_into_subscriptionData(String subscriptionID, String snssais, Integer loadLevelThreshold) {


        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        SliceLoadLevelSubscriptionData nwdafSliceLoadLevelSubscriptionDataModel = new
                SliceLoadLevelSubscriptionData();

        // Setting values in NwdafSliceLoadLevelSubscriptionData object
        nwdafSliceLoadLevelSubscriptionDataModel.setSubscriptionId(String.valueOf(subscriptionID));
        nwdafSliceLoadLevelSubscriptionDataModel.setSnssai(snssais);
        nwdafSliceLoadLevelSubscriptionDataModel.setLoadLevelThreshold(loadLevelThreshold);

        // Adding nwdafSliceLoadLevelSubscriptionDatamodel into NwdafSliceLoadLevelSubscriptionData Table [ Database ]
        repository.addDataIntoNwdafSliceLoadLevelSubscriptionDataTable(nwdafSliceLoadLevelSubscriptionDataModel);
        //logger.debug("enter add_values_into_subscriptionData");

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }

    /**
     * @param subscriptionId
     * @return
     * @throws URISyntaxException
     * @desc function to send response header to NF consumers
     */
    protected HttpHeaders send_response_header_to_NF(String subscriptionId) throws URISyntaxException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        URI location = new URI(URI + "subscriptions/" + subscriptionId);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);


        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return responseHeaders;
    }

    /**
     * @param nnrfModel
     * @param con
     * @return
     * @throws JSONException
     * @throws IOException
     * @desc function to receive response code
     */
    protected int send_data_receieve_response(Nnrf_Model nnrfModel, HttpURLConnection con) throws JSONException, IOException {

        JSONObject json = new JSONObject();

        json.put("correlationId", nnrfModel.getCorrelationId());
        json.put("unSubCorrelationId", nnrfModel.getUnSubCorrelationId());


        logger.info("correlationId:  " + nnrfModel.getCorrelationId() + "\n" +
                "unSubCorrelationId", nnrfModel.getUnSubCorrelationId());


        // notificationTargetUrl = NRF URL
        json.put("notificationTargetAddress", notificationTargetUrlForNRF);

        // For POST only - START
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {

            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }
        // For POST only - END
        int responseCode = con.getResponseCode();
        String responseMessage = con.getResponseMessage();
        System.out.println("\n");

        return responseCode;
    }

    /**
     * @param snssais
     * @return
     * @desc this function will fetch all subscription IDs of a particular snssais
     */
    //Fetching all subscriptionID of particular snssais
    protected List<SliceLoadLevelSubscriptionData> fetch_all_subscitpionID(String snssais) {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        List<SliceLoadLevelSubscriptionData> subscriptionIDList = repository.getAllSubIdsbysnssais(snssais);

        if (subscriptionIDList == null) {
            logger.warn("No subscriber found");
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return subscriptionIDList;
    }







    // @RequestMapping(method = RequestMethod.DELETE, value = "/Nnrf_NFManagement_NFStatusUnSubscribe")
    // @RequestMapping("/t")
    protected void unsubscribeFromNWDAF(String snssais) throws IOException, JSONException {

        // here subscriptionID = unsubCorrealtionID

        // String subscriptionID =  repository.getUnSubCorrelationID(snssais);

        URL obj = new URL(DELETE_NRF_URL);

        //  out.println(DELETE_NRF_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        String unSubCorrelationID = repository.getUnSubCorrelationID_SliceLoadLevel(snssais);
        //   String mCorrelationID = repository.getUnSubCorrelationID(snssais);

        String correlationID = repository.getCorrelationID(unSubCorrelationID, NwdafEvent.LOAD_LEVEL_INFORMATION);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("unSubCorrelationId", unSubCorrelationID);
        jsonObject.put("correlationId", correlationID);

        con.setRequestMethod("DELETE");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonObject.toString().getBytes("utf-8");

            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Read the response from input stream;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

    }


    protected boolean testConnection() {

        try {
            URL url = new URL(testConnect_URI);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            if (con.getResponseCode() != HttpStatus.ACCEPTED.value()) {

                if (con != null) {
                    con.disconnect();
                }

                return false;
            }

            if (con != null) {
                con.disconnect();
            }

        } catch (IOException ex) {

            ErrorCounters.incrementIOException();
            return false;
        }

        return true;
    }


    // Updated nwdaf_notification_manager 3rd March, 2020
    protected void sliceLoadLevelNotificationManager() throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        // Fetching all snssais list
        List<SliceLoadLevelInformation> list = repository.getALLsnssais();

        if (list == null || list.isEmpty()) {
            logger.warn("no snssais Found [ Null object ]");
            return;
        }

        LoadLevelInformationNotification ldLevelNotifyData = new LoadLevelInformationNotification();

        for (SliceLoadLevelInformation slice : list) {
            List<NotificationData> dataSet = repository.getAllNotificationData(slice.getSnssai(), slice.getCurrentLoadLevel());

            if (dataSet != null && !dataSet.isEmpty()) {
                for (NotificationData notifyData : dataSet) {
                  /*  if (!subID_SET.contains(notifyData.getSubscriptionID())) {
                        subID_SET.add(notifyData.getSubscriptionID());

                        //SubscriptionTable subscriptionData = repository.findById_subscriptionID(notifyData.getSubscriptionID());
                        send_notificaiton_to_NF(repository.getNotificationURI(notifyData.getSubscriptionID()), EventID.LOAD_LEVEL_INFORMATION, slice.getSnssais(), slice.getCurrentLoadLevel(), notifyData.getSubscriptionID());
                    } */

                    //send_notificaiton_to_NF(repository.getNotificationURI(notifyData.getSubscriptionID()), EventID.LOAD_LEVEL_INFORMATION, slice.getSnssais(), slice.getCurrentLoadLevel(), notifyData.getSubscriptionID());
                    initiate_SliceLoadLevelNotifyData(ldLevelNotifyData, notifyData.getSubscriptionId(), repository.getNotificationURI(notifyData.getSubscriptionId()), slice.getSnssai(), slice.getCurrentLoadLevel());
                }
            }
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }


    protected void qosNotificationManager(QosType qosType)  {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        try
        {
            List<QosSustainabilityInformation> list = repository.getAll_Tai_Snssai_QosSustainabilityInformation(qosType);

            if (list == null || list.isEmpty()) {
                logger.warn("no snssais Found [ Null object ]");
                return;
            }

            QosSustainabilityNotification qosNotifyData = new QosSustainabilityNotification();

            for (QosSustainabilityInformation qos : list) {
                List<QosNotificationData> dataSet;

                if (qosType == QosType.RAN_UE_THROUGHPUT) {
                    dataSet = repository.getAllQosNotificationData(qos.getTai(), qos.getSnssai(), qos.getRanUeThrou(), qosType);
                } else {
                    dataSet = repository.getAllQosNotificationData(qos.getTai(), qos.getSnssai(), qos.getQosFlowRet(), qosType);
                }


                if (dataSet != null && !dataSet.isEmpty()) {
                    if (qosType == QosType.RAN_UE_THROUGHPUT) {
                        for (QosNotificationData notifyData : dataSet) {
                            initiate_QosSustainabilityNotifyData(qosNotifyData, notifyData.getSubscriptionId() ,repository.getNotificationURI(notifyData.getSubscriptionId()), qos.getTai() ,qos.getSnssai(), qos.getRanUeThrou(), QosType.RAN_UE_THROUGHPUT);
                        }
                    } else {
                        for (QosNotificationData notifyData : dataSet) {
                            initiate_QosSustainabilityNotifyData(qosNotifyData, notifyData.getSubscriptionId() ,repository.getNotificationURI(notifyData.getSubscriptionId()), qos.getTai() ,qos.getSnssai(), qos.getQosFlowRet(), QosType.QOS_FLOW_RETAIN);
                        }
                    }
                }
            }

            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        }

        catch(JSONException e)
        {
            ErrorCounters.incrementJsonException();
            e.printStackTrace();
        }

        catch(IOException e)
        {
            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }

    }



    public void initiate_QosSustainabilityNotifyData(QosSustainabilityNotification qosNotifyData, String subscriptionId, String notificationURI, String tai, String snssai, Integer threshold, QosType thresholdType) throws IOException, JSONException {

        qosNotifyData.setSubscriptionId(subscriptionId);
        qosNotifyData.setNotificationURI(notificationURI);
        qosNotifyData.setTai(tai);
        qosNotifyData.setSnssai(snssai);
        qosNotifyData.setThreshold(threshold);
        qosNotifyData.setThresholdType(thresholdType);

        sendQosSustainabilityNotification(qosNotifyData);
    }


    public void initiate_SliceLoadLevelNotifyData(LoadLevelInformationNotification ldLevelNotifyData, String subscriptionId, String notificationURI, String snssai, Integer currentLoadLevel) throws IOException, JSONException {

        ldLevelNotifyData.setSubscriptionId(subscriptionId);
        ldLevelNotifyData.setNotificationURI(notificationURI);
        ldLevelNotifyData.setSnssai(snssai);
        ldLevelNotifyData.setCurrentLoadLevel(currentLoadLevel);

        sendLoadLevelInformationNotification(ldLevelNotifyData);
    }





    /**************************UE_MOBILITY code*********************************************************************/


    protected Object check_For_data_for_UE_Mobility(EventSubscription eventSubscription, boolean getAnalytics) throws IOException, JSONException {


        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (getAnalytics) {

            List<EventConnectionForUEMobility> supiDataList = repository.checkForUEMobilityData(eventSubscription.getTgtUe().getSupi());

            // if data is not found -> calling collector function to collect data
            if (supiDataList == null || supiDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                // Object obj = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics);

                //  if (obj instanceof ResponseEntity) {
                //      return obj;
                //  }

                // Calling collector function
                Object obj = nwdaf_data_collector_For_UE_Mobility(eventSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }


                // Adding snssais into database
                //  repository.add_data_into_load_level_table(nnwdafEventsSubscription.getSnssais());
                UEMobilitySubscriptionModel ueMobilitySubscriptionModel = new UEMobilitySubscriptionModel();
                ueMobilitySubscriptionModel.setSupi(eventSubscription.getTgtUe().getSupi());

                repository.add_data_into_nwdafUEmobility(ueMobilitySubscriptionModel);

                EventConnectionForUEMobility eventConnection = new EventConnectionForUEMobility();
                eventConnection.setSupi(eventSubscription.getTgtUe().getSupi());
                eventConnection.setMessage("Data not Found for " + eventSubscription.getTgtUe().getSupi());

                eventConnection.setDataStatus(false);


                out.println("supi------->" + ueMobilitySubscriptionModel.getSupi());


                return eventConnection;

            } else {
                // flag to check if getAnalytics ref count will be updated or not
                //flag = 1;
                Object obj = nwdaf_data_collector_For_UE_Mobility(eventSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // if Data found returning JSON
                return supiDataList;
            }

        } else {

            // subscription Data Collector
            Object obj = nwdaf_data_collector_For_UE_Mobility(eventSubscription, getAnalytics);

            if (obj instanceof ResponseEntity) {
                return obj;
            }

        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }


    public void add_value_to_userLocationTable(String taiValue, String cellID, Integer timeDuration) {

        repository.addValuesToUserLocationTable(taiValue, cellID, timeDuration);
    }



    /*
    public void updateUEMobilityTable(String correlationID, List<String> taiList) {

        //Fetch all IDs from UserLcoation table
        List<Integer> IDs = repository.getAllIDFromUserLocationTable(taiList);

        // finding the supi value by correlationID
        String supi = repository.getSupiValueByCorrelationID(correlationID);

        //Updating the location of supi;
        repository.updateLocatoinValueToUEMobilityTable(IDs, supi);
    }*/


    public void updateLocationIdTable(String supi, List<String> tai_cellIDs) {

        // Extracting locationID from nwdafUserLocation table based on Tai & cellID values
        // Inserting locationID into nwdafLocationID with the corresponding supi

        repository.deleteOldEntriesInLocationID(supi);

        for(String tai_cellID: tai_cellIDs)
        {
            String areaInfo[] = tai_cellID.split(" ");
            repository.updateLocationIdTable(supi, areaInfo);
        }
    }


    protected void nwdaf_notification_manager_ForUEMobility(String supi) throws IOException, JSONException {

        List<Integer> supiLocationIDs = repository.getLocationIDs(supi);

        if (supiLocationIDs == null || supiLocationIDs.isEmpty()) {
            logger.warn("supi value: " + supi + " locations not found");
            return;
        }

        List<UserLocation> userLocations = new ArrayList<UserLocation>();

        for(Integer locationID: supiLocationIDs)
        { userLocations.add(repository.getUserLocationFromID(locationID)); }

        out.println("supi value = " + supi);

        String subscriptionID = repository.getSubscriptionIdBySupi(supi);

        if (subscriptionID == null) {
            out.println("No subscriptionID found for supi: " + supi);
            return;
        }

        String notificationURI = repository.getNotificationURI(subscriptionID);

        send_notificaiton_to_NF_UEeMobility(notificationURI, userLocations, supi, subscriptionID);
    }





    protected Object check_For_dataUEmobility(NnwdafEventsSubscriptionUEmobility nnwdafEventsSubscriptionUE, boolean getAnalytics) throws IOException, JSONException {

        out.println("incheck-for-data-UE");
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (getAnalytics) {

            List<EventConnectionForUEMobility> supiDataList = getUElocation(nnwdafEventsSubscriptionUE.getSupi());
            //List<UserLocation> supiDataList = repository.getAllNotificationDataForUEMobility(nnwdafEventsSubscriptionUE.getSupi());

            // if data is not found -> calling collector function to collect data
            if (supiDataList == null || supiDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                Object obj = nwdaf_data_collectorUEmobility(nnwdafEventsSubscriptionUE, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                UEMobilitySubscriptionModel ueMobilitySubscriptionModel = new UEMobilitySubscriptionModel();
                ueMobilitySubscriptionModel.setSupi(nnwdafEventsSubscriptionUE.getSupi());
                // Adding supi into database
                repository.add_data_into_nwdafUEmobility(ueMobilitySubscriptionModel);

                EventConnectionUE eventConnectionUE = new EventConnectionUE();
               // eventConnectionUE.setMessage("Data not Found for " + nnwdafEventsSubscriptionUE.getSupi());
                eventConnectionUE.setMessage("204 No Content");
                eventConnectionUE.setSupi(nnwdafEventsSubscriptionUE.getSupi());
                eventConnectionUE.setDataStatus(false);

                logger.debug("supi: " + nnwdafEventsSubscriptionUE.getSupi() + "\n" +
                        "DataStatus:  " + eventConnectionUE.getDataStatus());

                //throw new NullPointerException("Data not found!");
                return new ResponseEntity<EventConnectionUE>(eventConnectionUE, HttpStatus.NOT_FOUND);

            } else {
                // flag to check if getAnalytics ref count will be updated or not
                //flag = 1;
                Object obj = nwdaf_data_collectorUEmobility(nnwdafEventsSubscriptionUE, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // if Data found returning JSON
                return new ResponseEntity(supiDataList, HttpStatus.OK);
            }

        } else {

            Object obj = nwdaf_data_collectorUEmobility(nnwdafEventsSubscriptionUE, getAnalytics);

            if (obj instanceof ResponseEntity) {
                return obj;
            }
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;

    }


    private Object nwdaf_data_collector_For_UE_Mobility(EventSubscription eventSubscription, boolean getAnalytics) {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        Object snssais_object = repository.findby_supi(eventSubscription.getTgtUe().getSupi());

        if (snssais_object == null) {

            // Generating CorrelationID
            String correlationId = FrameWorkFunction.getUniqueID().toString();

            // if Event Id is UE-Mobility nwdaf will subscribe to AMF;

          /* if (nnwdafEventsSubscription.getEventID().toString() == "UE_MOBILITY") {
                subscribeAMFfromNWDAF(nnwdafEventsSubscription, correlationID, getAnalytics);
            }*/

            AMFModel amfModel = new AMFModel();

            // Adding correlationID to object and send to SIMULATOR
            amfModel.setCorrelationId(String.valueOf(correlationId));
            // Updated URL For NWDAF to Subscribe
            // updated_POST_NRF_URL = POST_NRF_URL + "/" + correlationID;
            // POST_NRF_URL = NRF URL ------> [ Reading from ]application.properties
            try {

                URL obj = new URL(POST_AMF_URL);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");

                // Function To Send CorrelationID to SIMULATOR
                int responseCode = send_data_receieve_response_For_UEMobility(amfModel, con);
                if (responseCode != HttpStatus.OK.value()) {
                    throw new Exception();
                }

                response_handler_for_UEMobility(eventSubscription, responseCode, correlationId, con, getAnalytics);

                if (con != null) {
                    con.disconnect();
                }

            } catch (Exception ex) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }

            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return correlationId;
        } else {
            repository.increment_ref_count_ofSupi(eventSubscription.getTgtUe().getSupi());
        }
        return null;

    }


    /**
     * @param notificationURI
     * @throws IOException
     * @desc this function will send notification to network function
     */
    //For UE_MOBILITY only
    protected void send_notificaiton_to_NF_UEeMobility(String notificationURI,
                                           List<UserLocation> userLocations,
                                           String supi,
                                           String subscriptionID) throws IOException, JSONException {

//        JSONObject jsonForUEMobility = new JSONObject();
//        JSONObject jsonForSliceLoadLevel = new JSONObject();
//        JSONArray jsonArrayFinalNotification = new JSONArray();
//        JSONObject jsonObjectFinalNotification = new JSONObject();

        String tacValue;

        /*JSON NOTIFICATION ACCORDING 3GPP START*/

        JSONArray eventNotificationArray = new JSONArray();
        JSONObject eventNotificationFinalObject = new JSONObject();
        JSONObject eventNotificationObject = new JSONObject();
        JSONArray uEMobilityArray = new JSONArray();
       // JSONObject sliceLoadLevelInfo = new JSONObject();
       // JSONArray snssaisArray = new JSONArray();
        JSONObject ueMobilityObject = new JSONObject();
        JSONArray locationInfoArray = new JSONArray();

        JSONArray ueTrajectoryArray = new JSONArray();
        JSONObject ueTrajectoryObject = new JSONObject();


        for (int i = 0; i < userLocations.size(); i++) {

            JSONObject locationInfoObject = new JSONObject();

            String key = "userLocation-" + i;
            String taiValue = userLocations.get(i).getTai();
            String cellIDValue = userLocations.get(i).getCellID();
            Integer timeDuration = userLocations.get(i).getTimeDuration();

            // String finalLocationValue = taiValue + "||" + cellIDValue;
            out.println("\n\n\ntaiValue" + taiValue + "\n\n\n");
            //String[] splitedString = splitLocationString.split(",");
            String[] taiSplitValue = taiValue.split(":");
            String plmnValues = taiSplitValue[0];
            tacValue = taiSplitValue[1];

            out.println("\n\nplmnValues" + plmnValues);
            out.println("\n\nTacValue" + tacValue);

            String[] plmnSplittedValues = plmnValues.split(",");
            String MCC = plmnSplittedValues[0];
            String MNC = plmnSplittedValues[1];
            //  String finalLocationValue = " MCC - " + MCC + " MNC - " + MNC + " Tac Value - " + tacValue + " Cell-ID - " + cellIDValue;

            JSONObject plmnObject = new JSONObject();
            plmnObject.put("MNC", MNC);
            plmnObject.put("MCC", MCC);

            JSONObject EcgiObject = new JSONObject();
            EcgiObject.put("plmnId", plmnObject);
            EcgiObject.put("EutraCellId", cellIDValue);


            JSONObject taiObject = new JSONObject();
            taiObject.put("plmnId", plmnObject);
            taiObject.put("Tac", tacValue);


            JSONObject userLocation = new JSONObject();
            userLocation.put("timeDuration", timeDuration);
            userLocation.put("Tai", taiObject);
            userLocation.put("Ecgi", EcgiObject);


            locationInfoObject.put(key, userLocation);
            locationInfoArray.put(locationInfoObject);

        }


        ueTrajectoryObject.put("ts", "Date-time-value");
        ueTrajectoryObject.put("recurringTime", "recurringTime-value");
        //ueTrajectoryObject.put("duration", 30);
        ueMobilityObject.put("supi", supi);
        ueTrajectoryObject.put("locInfo", locationInfoArray);

        ueTrajectoryArray.put(ueTrajectoryObject);

        ueMobilityObject.put("ueTraj", ueTrajectoryArray);


        uEMobilityArray.put(ueMobilityObject);

      /*  snssaisArray.put(snssais);
        sliceLoadLevelInfo.put("loadLevelInformation", currentLoadLevel);
        sliceLoadLevelInfo.put("snssais", snssaisArray); */

        /*FIXED JSON PAYLOAD
         * It will only send data for particular event-ID*/

      /*  if (eventID == "LOAD_LEVEL_INFORMATION") {
            eventNotificationObject.put("NwdafEvent", eventID);
            eventNotificationObject.put("SliceLoadLevelInformation", sliceLoadLevelInfo);
            eventNotificationArray.put(eventNotificationObject);
        }
        if (eventID == "UE_MOBILITY") {
            eventNotificationObject.put("NwdafEvent", eventID);
            eventNotificationObject.put("ueMobs", uEMobilityArray);
            eventNotificationArray.put(eventNotificationObject);
        }

        if (eventID == "QOS_SUSTAINABILITY") {

        } */

        eventNotificationObject.put("NwdafEvent", NwdafEvent.UE_MOBILITY.toString());
        eventNotificationObject.put("ueMobs", uEMobilityArray);
        eventNotificationArray.put(eventNotificationObject);


        eventNotificationFinalObject.put("eventNotifications", eventNotificationArray);
        eventNotificationFinalObject.put("subscriptionId", subscriptionID);

        /*JSON NOTIFICATION ACCORDING 3GPP ENDS*/


        // jsonObjectFinalNotification.put("subscriptionID", subscriptionID);

     /*   final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);*/




        // out.println("eventID - " + eventID);


        // String subscriptionURI = "http://localhost:8081/nnwdaf-eventssubscription/v1/subscriptions";

        // URL url = null;
        //try {
        URL url = new URL(notificationURI);
        //} catch (MalformedURLException e) {
        // logger.warn("http connect exception found");
        //e.printStackTrace();
        //}

        // Opening connection;
        // HttpURLConnection con = null;
        ///try {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //} catch (IOException e) {
        //e.printStackTrace();

        //}

        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);


        /*
       Todo: Here We have to send the JSON ARRAY of events
        but it's giving 500 internal server error for that*/


       /* if (eventID.equals("UE_MOBILITY")) {

            // out.println("sheetal_UE");
            // out.println(eventID);

            jsonForUEMobility.put("eventID", eventID);
            jsonForUEMobility.put("supi", supi);


            for (int i = 0; i < userLocations.size(); i++) {


                String key = "userLocation-" + i;
                String taiValue = userLocations.get(i).getTai();
                String cellIDValue = userLocations.get(i).getCellID();
                Integer timeDuration = userLocations.get(i).getTimeDuration();

                // String finalLocationValue = taiValue + "||" + cellIDValue;
                out.println("\n\n\ntaiValue" + taiValue + "\n\n\n");
                //String[] splitedString = splitLocationString.split(",");
                String[] taiSplitValue = taiValue.split(":");
                String plmnValues = taiSplitValue[0];
                tacValue = taiSplitValue[1];

                out.println("\n\nplmnValues" + plmnValues);
                out.println("\n\nTacValue" + tacValue);

                String[] plmnSplittedValues = plmnValues.split(",");
                String MCC = plmnSplittedValues[0];
                String MNC = plmnSplittedValues[1];
                //  String finalLocationValue = " MCC - " + MCC + " MNC - " + MNC + " Tac Value - " + tacValue + " Cell-ID - " + cellIDValue;

                JSONObject plmnObject = new JSONObject();
                plmnObject.put("MNC", MNC);
                plmnObject.put("MCC", MCC);

                JSONObject EcqiObject = new JSONObject();
                EcqiObject.put("plmn", plmnObject);
                EcqiObject.put("cellID", cellIDValue);


                JSONObject taiObject = new JSONObject();
                taiObject.put("plmn", plmnObject);
                taiObject.put("Tac", tacValue);


                JSONObject userLocation = new JSONObject();
                // userLocation.put("correlationID", correlationID);
                userLocation.put("timeDuration", timeDuration);
                userLocation.put("Tai", taiObject);
                userLocation.put("Ecqi", EcqiObject);


                jsonForUEMobility.put(key, userLocation);
            }
            jsonForUEMobility.put("notificationURI", subscriptionURI.trim());
            jsonForUEMobility.put("subscriptionID", subscriptionID);

            jsonObjectFinalNotification.put(eventID, jsonForUEMobility);
        }
        if (eventID.equals("LOAD_LEVEL_INFORMATION")) {

            // out.println("sheetal_load");
            //out.println(eventID);


            jsonForSliceLoadLevel.put("eventID", eventID);
            jsonForSliceLoadLevel.put("snssais", snssais);


            // json.put("supi", supi);


            // json.put("userLocation-1", userLocations.get(0).getTai());
            // json.put("userLocation-2", userLocations.get(1).getTai());

            // This value will be fetched from nwdafSliceLoadLevelSubscriptionData send by NF
            jsonForSliceLoadLevel.put("notificationURI", subscriptionURI.trim());
            jsonForSliceLoadLevel.put("subscriptionID", subscriptionID);
            jsonForSliceLoadLevel.put("currentLoadLevel", currentLoadLevel);


            jsonObjectFinalNotification.put(eventID, jsonForSliceLoadLevel);


        }

        // jsonArrayFinalNotification.pi
        jsonArrayFinalNotification.put(jsonObjectFinalNotification);


        // jsonArrayFinalNotification.put(jsonForSliceLoadLevel);
        //jsonArrayFinalNotification.put(jsonForUEMobility);*/


        // For POST only - START
        // con.setDoOutput(true);
        //  out.println("final-notification-test-1" + jsonArrayFinalNotification.toString());
        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {

            out.println("final-notification-test-2" + eventNotificationFinalObject.toString());
            byte[] input = eventNotificationFinalObject.toString().getBytes("utf-8");


            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { //success

            // after sending the notification making json array null;
            // jsonArrayFinalNotification = null;

            //  out.println("\n\n\njsonarray0Index - " + jsonArrayFinalNotification.get(0).toString());
            //   out.println("jsonarraylength" + jsonArrayFinalNotification.length());

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }


        if (con != null) {
            con.disconnect();
        }
        // For load-level-checking
        //userLocations.clear();
        // jsonArrayFinalNotification.remove(0);

        // logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);

        EventCounter[NwdafEvent.UE_MOBILITY.ordinal()].incrementSubscriptionNotificationsSent();
    }


    //Need to make changes here also
    public List<EventConnectionForUEMobility> getUElocation(String supi) {


        List<EventConnectionForUEMobility> eventConnectionForUEMobilityArrayList = new ArrayList<>();
        List<UserLocation> userLocationsForAnalytics = new ArrayList<>();

        out.println("in getUELocation");
        EventConnectionForUEMobility eventConnectionForUEMobility = new EventConnectionForUEMobility();


        try {

            List<Integer> supilocationIDs = repository.getLocationIDs(supi);

            // iterating through split string and finding User Location from particular ID
            for(Integer locationID: supilocationIDs)
            {
                /* now it's time to find the UserLocation by particular ID */
                UserLocation userLocation = repository.getUserLocationFromID(locationID);


                out.println("userlocation - " + userLocation.toString());

                userLocationsForAnalytics.add(userLocation);
                out.println("user-location : Tai-value " + userLocation.getTai());
            }

            out.println("userlocations - > " + userLocationsForAnalytics.toString());
            eventConnectionForUEMobility.setSupi(supi);
            eventConnectionForUEMobility.setMessage("Data Found for supi " + supi);
            eventConnectionForUEMobility.setDataStatus(true);
            eventConnectionForUEMobility.setLocationInfo(userLocationsForAnalytics);

            eventConnectionForUEMobilityArrayList.add(eventConnectionForUEMobility);

            out.println("userlocations" + userLocationsForAnalytics.get(0).toString());
            return eventConnectionForUEMobilityArrayList;


        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

    }


    protected Object nwdaf_data_collectorUEmobility(NnwdafEventsSubscriptionUEmobility nnwdafEventsSubscriptionUE, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if (!repository.supiExists(nnwdafEventsSubscriptionUE.getSupi())) {


            // Generating CorrelationID
            String correlationId = FrameWorkFunction.getUniqueID().toString();


            //  Nnrf_Model nnrfModel = new Nnrf_Model();

            AMFModel amfModel = new AMFModel();
            // Adding correlationID to object and send to SIMULATOR
            amfModel.setCorrelationId(String.valueOf(correlationId));
            // Updated URL For NWDAF to Subscribe
            // updated_POST_NRF_URL = POST_NRF_URL + "/" + correlationID;
            // POST_NRF_URL = NRF URL ------> [ Reading from ]application.properties
            try {

                //URL obj = new URL(POST_NRF_URL);
                URL obj = new URL(POST_AMF_URL);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");

                // Function To Send CorrelationID to SIMULATOR
                //  int responseCode = send_data_receieve_response(nnrfModel, con);

                int responseCode = send_data_receieve_response_AMF(amfModel, con);

                if (responseCode != HttpStatus.OK.value()) {
                    throw new Exception();
                }

                response_handlerUEmobility(nnwdafEventsSubscriptionUE, responseCode, correlationId, con, getAnalytics);

                if (con != null) {
                    con.disconnect();
                }

            } catch (Exception ex) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }

            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return correlationId;
        } else {

            if (!getAnalytics) { //repository.increment_ref_count(nnwdafEventsSubscriptionUE.getSupi());

            }
        }
        return null;
    }


    private int send_data_receieve_response_For_UEMobility(AMFModel amfModel, HttpURLConnection con) throws JSONException, IOException {

        JSONObject json = new JSONObject();

        json.put("correlationID", amfModel.getCorrelationId());
        json.put("unSubCorrelationId", amfModel.getUnSubCorrelationId());
        json.put("eventID", NwdafEvent.UE_MOBILITY.ordinal());


        // notificationTargetUrl = Namf_EventExposure_Notify
        json.put("notificationTargetAddress", notificationTargetUrlForAMF);

        // For POST only - START
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For POST only - END
        int responseCode = con.getResponseCode();
        String responseMessage = con.getResponseMessage();
        System.out.println("\n");

        return responseCode;
    }


    private void response_handler_for_UEMobility(EventSubscription eventSubscription, int responseCode, String correlationId, HttpURLConnection con, boolean getAnalytics) throws IOException {


        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success



            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            try {

                UEMobilitySubscriptionTable uEmobilitySubscriptionTable = new UEMobilitySubscriptionTable();

                uEmobilitySubscriptionTable.setCorrelationId(correlationId);
                uEmobilitySubscriptionTable.setSubscriptionId(response.toString());
                uEmobilitySubscriptionTable.setSupi(eventSubscription.getTgtUe().getSupi());

                if (getAnalytics) {
                    // if (!repository.supiExists(uEmobilitySubscriptionTable.getSupi())) {
                    repository.addCorrealationIDAndUnSubCorrelationIDIntonwdafUEmobilitySubscriptionTable(uEmobilitySubscriptionTable, true);
                    // }
                } else {
                    repository.addCorrealationIDAndUnSubCorrelationIDIntonwdafUEmobilitySubscriptionTable(uEmobilitySubscriptionTable, false);
                }


            } catch (Exception e) {
                out.println(e.getMessage());
            }
            in.close();
        } else {

            logger.error(" POST request not worked");
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);

    }


    protected void response_handlerUEmobility(NnwdafEventsSubscriptionUEmobility nnwdafEventsSubscriptionUE,
                                              int responseCode, String correlationId,
                                              HttpURLConnection con, boolean getAnalytics) throws IOException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            // incrementing Counter
            //Counters.incrementCollectorSubscriptions();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            try {

                UEmobilitySubscriptionTable UEobj = new UEmobilitySubscriptionTable();
                UEobj.setCorrelationId(String.valueOf(correlationId));
                UEobj.setSubscriptionId(response.toString());
                UEobj.setSupi(nnwdafEventsSubscriptionUE.getSupi());

                if (getAnalytics) {
                    if (!repository.supiExists(UEobj.getSupi())) {
                        repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable_UE(UEobj, true);
                    }
                } else {
                    repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable_UE(UEobj, false);
                }


            } catch (Exception e) {
                out.println(e.getMessage());
            }
            in.close();
        } else {

            logger.error(" POST request not worked");
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }


    /************************************************************************************************************************/


    /*

    public void updateForSliceLoadLevelInformation(NnwdafEventsSubscription subscription)
    {
        boolean hasNotifMethod = subscription.getNotifMethod() != null;
        boolean hasRepetitionPeriod = subscription.getRepetitionPeriod() != null;
        boolean hasLoadLevelThreshold = subscription.getLoadLevelThreshold() != null;

        if(hasLoadLevelThreshold)
        { repository.updateSliceLoadLevelEntry(subscription, 1); }

        if(hasNotifMethod && hasRepetitionPeriod)
        { repository.updateSliceLoadLevelEntry(subscription, 2); }

        else if(hasNotifMethod)
        { repository.updateSliceLoadLevelEntry(subscription, 3); }

        else if(hasRepetitionPeriod)
        { repository.updateSliceLoadLevelEntry(subscription, 4); }

    }


    public void updateForQosSustainability(NnwdafEventsSubscription subscription)
    {
        if(subscription.getRanUeThroughputThreshold() != null)
        { repository.updateQosSustainabilityEntry(subscription, QosType.RAN_UE_THROUGHPUT); }

        else if(subscription.getQosFlowRetainThreshold() != null)
        { repository.updateQosSustainabilityEntry(subscription, QosType.QOS_FLOW_RETAIN); }
    }
*/

    /***************************************QOS_SUSTAINABILITY************************************************************************/



    public Object checkForData_QosSustainability(String snssai, String tai, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if(getAnalytics)
        {
            List<QosSustainabilityInfo> qosInfo = repository.getQosSustainabilityInfo(tai, snssai);
            collectDataForQosSustainability(snssai, tai, true);

            repository.setNwdafQosSustainabilityInformation(snssai, tai);

            if(qosInfo == null || qosInfo.isEmpty())
            { return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND); }

            else
            { return new ResponseEntity(qosInfo, HttpStatus.OK); }
        }

        else
        { collectDataForQosSustainability(snssai, tai, false); }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }



    public void collectDataForQosSustainability(String snssai, String tai, boolean getAnalytics) throws IOException, JSONException {


        if(repository.plmnIdSnssaisExists_QosSustainability(tai, snssai, TableType.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.increaseRefCountQosSustainability(tai, snssai); }
        }

        else
        {
            String correlationId = FrameWorkFunction.getUniqueID().toString();
            String unSubCorrelationId = subscribeRightSide(POST_OAM_URL, correlationId, NwdafEvent.QOS_SUSTAINABILITY);

            responseHandlerQosSustainability(tai, snssai, correlationId, unSubCorrelationId, getAnalytics);
        }
    }




    public void responseHandlerQosSustainability(String tai, String snssai, String correlationId, String unSubCorrelationId, boolean getAnalytics)
    {
        QosSustainabilitySubscriptionTable qos = new QosSustainabilitySubscriptionTable();

        qos.setTai(tai);
        qos.setSnssai(snssai);
        qos.setSubscriptionId(unSubCorrelationId);
        qos.setCorrelationId(correlationId);

        repository.addDataQosSustainabilitySubscriptionTable(qos, getAnalytics);
    }









    /***************************************SERVICE_EXPERIENCE************************************************************************/






    public Object checkForData_ServiceExperience(String supi, String snssai, Boolean anyUe, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if(getAnalytics)
        {
            List<ServiceExperienceInfo> svcExpInfo = repository.getServiceExperienceInfo(supi, snssai, anyUe);
            collectDataForServiceExperience(supi, snssai, true);

            repository.addServiceExperienceInformation(supi, snssai);

            if(svcExpInfo == null || svcExpInfo.isEmpty())
            { return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND); }

            else
            { return new ResponseEntity(svcExpInfo, HttpStatus.OK); }

        }

        else
        { collectDataForServiceExperience(supi, snssai, false); }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }



    public void collectDataForServiceExperience(String supi, String snssai, boolean getAnalytics) throws IOException, JSONException {

        if(repository.supiSnssaiExist_ServiceExperience(supi, snssai, TableType.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.updateRefCount_ServiceExperience(supi, snssai); }
        }

        else
        {
            String correlationId = FrameWorkFunction.getUniqueID().toString();
            String unSubCorrelationId = subscribeRightSide(POST_NF_URL, correlationId, NwdafEvent.SERVICE_EXPERIENCE);

            responseHandlerServiceExperience(supi, snssai, correlationId, unSubCorrelationId, getAnalytics);
        }

    }



    public void responseHandlerServiceExperience(String supi, String snssai, String correlationId, String unSubCorrelationId, boolean getAnalytics)
    {

        ServiceExperienceSubscriptionTable svcExp = new ServiceExperienceSubscriptionTable();

        svcExp.setSupi(supi);
        svcExp.setSnssai(snssai);
        svcExp.setCorrelationId(correlationId);
        svcExp.setSubscriptionId(unSubCorrelationId);

        repository.addServiceExperienceSubscriptionTable(svcExp, getAnalytics);

    }





    /***************************************NETWORK_PERFORMANCE************************************************************************/


    public Object checkForData_NetworkPerformance(String supi, Integer nwPerfType, Boolean anyUe, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if(getAnalytics)
        {
            List<Object> nwPerfInfo = repository.getNetworkPerformanceInfo(supi, nwPerfType, anyUe);
            collectDataForNetworkPerformance(supi, nwPerfType, true);

            repository.addNetworkPerformanceInformation(supi, nwPerfType);

            if(nwPerfInfo == null || nwPerfInfo.isEmpty())
            { return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND); }

            else
            { return new ResponseEntity(nwPerfInfo, HttpStatus.OK); }

        }

        else
        { collectDataForNetworkPerformance(supi, nwPerfType, false); }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }



    public void collectDataForNetworkPerformance(String supi, Integer nwPerfType, boolean getAnalytics) throws IOException, JSONException {


        if(repository.supi_nwPerfTypeExists(supi, nwPerfType, TableType.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.updateRefCount_NetworkPerformance(supi, nwPerfType); }
        }

        else
        {
            String correlationId = FrameWorkFunction.getUniqueID().toString();
            String unSubcorrelationId = subscribeRightSide(POST_OAM_URL, correlationId, NwdafEvent.NETWORK_PERFORMANCE);

            responseHandlerNetworkPerformance(supi, nwPerfType, unSubcorrelationId, correlationId, getAnalytics);
        }
    }



    public void responseHandlerNetworkPerformance(String supi, Integer nwPerfType, String subscriptionId, String correlationId, boolean getAnalytics)
    {
        NetworkPerformanceSubscriptionTable nwPerfSubTable = new NetworkPerformanceSubscriptionTable();

        nwPerfSubTable.setSupi(supi);
        nwPerfSubTable.setNwPerfType(nwPerfType);
        nwPerfSubTable.setSubscriptionId(subscriptionId);
        nwPerfSubTable.setCorrelationId(correlationId);

        repository.addNetworkPerformanceSubscriptionTable(nwPerfSubTable, getAnalytics);
    }



    /***************************************USER_DATA_CONGESTION************************************************************************/


    public Object checkForData_UserDataCongestion(String supi, Integer congType, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if(getAnalytics)
        {
            String tai = repository.getNetworkInfo_UserDataCongestion(supi, congType);

            if(tai == null)
            { Tai ueLocation = fetchSupiLocationFromAMF(supi, NwdafEvent.USER_DATA_CONGESTION, AmfEventType.LOCATION_REPORT);
              tai = ueLocation.toString(); }

            List<Object> userDataCongInfo = repository.getUserDataCongestionInfo(supi, tai, congType);
            collectDataForUserDataCongestion(supi, congType, tai, true);

            repository.addUserDataCongestionInformation(supi, congType, tai);

            if(userDataCongInfo == null || userDataCongInfo.isEmpty())
            { return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND); }

            else
            { return new ResponseEntity(userDataCongInfo, HttpStatus.OK); }
        }

        else
        {
            String tai = repository.getNetworkInfo_UserDataCongestion(supi, congType);
            collectDataForUserDataCongestion(supi, congType, tai, false);
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }






    public void collectDataForUserDataCongestion(String supi, Integer congType, String tai, boolean getAnalytics) throws IOException, JSONException {

        if(repository.userDataCongDetailsExist(supi, congType, tai, TableType.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.updateRefCount_UserDataCongestion(supi, congType, tai); }
        }

        else
        {
            String correlationId = FrameWorkFunction.getUniqueID().toString();
            String unSubcorrelationId = subscribeRightSide(POST_OAM_URL, correlationId, NwdafEvent.USER_DATA_CONGESTION);

            responseHandlerUserDataCongestion(supi, congType, tai, correlationId, unSubcorrelationId, getAnalytics);
        }
    }



    public void responseHandlerUserDataCongestion(String supi, Integer congType, String tai, String correlationId, String subscriptionId, boolean getAnalytics)
    {
        UserDataCongestionSubscriptionTable usrDataCongSubTable = new UserDataCongestionSubscriptionTable();

        usrDataCongSubTable.setSupi(supi);
        usrDataCongSubTable.setCongType(congType);
        usrDataCongSubTable.setTai(tai);
        usrDataCongSubTable.setCorrelationId(correlationId);
        usrDataCongSubTable.setSubscriptionId(subscriptionId);

        repository.addUserDataCongestionSubscriptionTable(usrDataCongSubTable, getAnalytics);
    }



    public Tai fetchSupiLocationFromAMF(String supi, NwdafEvent event, AmfEventType amfEventType) throws JSONException, IOException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        URL obj = new URL(POST_AMF_URL);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);


        JSONObject json = new JSONObject();

        json.put("amfEventType", amfEventType.ordinal());
        json.put("supi", supi);
        json.put("event", event.ordinal());

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            //  out.println("Status: " + con.getResponseCode());
            //  out.println("Message: " + response.toString());
        }


        Tai tai = new Tai(new PlmnId(con.getHeaderField("mcc"), con.getHeaderField("mnc")), con.getHeaderField("tac"));
        //NetworkAreaInfo networkAreaInfo = new NetworkAreaInfo();
        //networkAreaInfo.getTais().add(tai);

        //eventSubscription.setNetworkArea(networkAreaInfo);

        /*
        subscription.getTai().getPlmnId().setMcc(con.getHeaderField("mcc"));
        subscription.getTai().getPlmnId().setMnc(con.getHeaderField("mnc"));
        subscription.getTai().setTac(con.getHeaderField("tac")); */

        return tai;
    }




    /***************************************ABNORMAL_BEHAVIOUR************************************************************************/




    public Object checkForData_AbnormalBehaviour(String supi, Integer excepId, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if(getAnalytics)
        {
            List<Object> abnorBehavrInfo = repository.getAbnormalBehaviourInfo(supi, excepId);
            collectDataForAbnormalBehaviour(supi, excepId, true);

            repository.addAbnormalBehaviourInformation(supi, excepId);

            if(abnorBehavrInfo == null || abnorBehavrInfo.isEmpty())
            { return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND); }

            else
            { return new ResponseEntity(abnorBehavrInfo, HttpStatus.OK); }
        }

        else
        { collectDataForAbnormalBehaviour(supi, excepId, false); }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }


    public void collectDataForAbnormalBehaviour(String supi, Integer excepId, boolean getAnalytics) throws IOException, JSONException {


        if(repository.abnorBehavrDetailsExist(supi, excepId, TableType.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.updateRefCount_AbnormalBehaviour(supi, excepId); }
        }

        else
        {
            String correlationId = FrameWorkFunction.getUniqueID().toString();
            String unSubcorrelationId = subscribeRightSide(POST_SMF_URL, correlationId, NwdafEvent.ABNORMAL_BEHAVIOUR);

            responseHandlerAbnormalBehaviour(supi, excepId, correlationId, unSubcorrelationId, getAnalytics);
        }
    }



    public void responseHandlerAbnormalBehaviour(String supi, Integer excepId, String correlationId, String subscriptionId, boolean getAnalytics)
    {
        AbnormalBehaviourSubscriptionTable abnormalBehaviourSubscriptionTable = new AbnormalBehaviourSubscriptionTable();

        abnormalBehaviourSubscriptionTable.setSupi(supi);
        abnormalBehaviourSubscriptionTable.setExcepId(excepId);
        abnormalBehaviourSubscriptionTable.setCorrelationId(correlationId);
        abnormalBehaviourSubscriptionTable.setSubscriptionId(subscriptionId);

        repository.addAbnormalBehaviourSubscriptionTable(abnormalBehaviourSubscriptionTable, getAnalytics);
    }




    /***************************************UE_COMM************************************************************************/



    public Object checkForData_UeComm(String supi, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if(getAnalytics)
        { }

        else
        { collectDataForUeComm(supi, false); }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }


    public void collectDataForUeComm(String supi, boolean getAnalytics) throws IOException, JSONException {

        if(repository.ueComm_SupiExists(supi, TableType.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.updateRefCount_UeComm(supi); }
        }

        else
        {
            String correlationId = FrameWorkFunction.getUniqueID().toString();
            String unSubcorrelationId = subscribeRightSide(POST_SMF_URL, correlationId, NwdafEvent.UE_COMM);

            responseHandlerUeComm(supi, correlationId, unSubcorrelationId, getAnalytics);
        }

    }


    public void responseHandlerUeComm(String supi, String correlationId, String unSubcorrelationId, boolean getAnalytics)
    {
        UeCommSubscriptionTable ueCommSubscriptionTable = new UeCommSubscriptionTable();

        ueCommSubscriptionTable.setSupi(supi);
        ueCommSubscriptionTable.setSubscriptionId(unSubcorrelationId);
        ueCommSubscriptionTable.setCorrelationId(correlationId);

        repository.addUeCommSubscriptionTable(ueCommSubscriptionTable, getAnalytics);
    }



    public Object analyticsRequest_UeComm(String supi, Integer maxAnaEntry) throws IOException, JSONException {

        List<Object> ueAnalyticsSet = repository.getUeCommAnalytics(supi, maxAnaEntry);

        if(ueAnalyticsSet == null || ueAnalyticsSet.isEmpty())
        {
            collectDataForUeComm(supi, true);
            return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND);
        }

        else
        {
            HashMap<Object, Object> analytics = new HashMap<>();
            analytics.put("ueComms", ueAnalyticsSet);

            return analytics;
        }
    }



    /**********************************************************************************************************************************/



    public void sendLoadLevelInformationNotification(LoadLevelInformationNotification ldLevelNotifyData) throws JSONException, IOException
    { sendNotificationToNF(ldLevelNotifyData.getNotificationURI(), NotificationPayload.getLoadLevelInformationPayload(ldLevelNotifyData), NwdafEvent.LOAD_LEVEL_INFORMATION); }


    public void sendQosSustainabilityNotification(QosSustainabilityNotification qosNotifyData) throws JSONException, IOException
    { sendNotificationToNF(qosNotifyData.getNotificationURI(), NotificationPayload.getQosSustainabilityPayload(qosNotifyData), NwdafEvent.QOS_SUSTAINABILITY); }


    public void sendServiceExperienceNotification(ServiceExperienceNotification svcExpNotifyData) throws JSONException, IOException
    { sendNotificationToNF(svcExpNotifyData.getNotificationURI(), NotificationPayload.getServiceExperiencePayload(svcExpNotifyData), NwdafEvent.SERVICE_EXPERIENCE); }


    public void sendNetworkExperienceNotification(NetworkPerformanceNotification nwPerfNotifyData, NetworkPerfThreshold threshold) throws JSONException, IOException
    { sendNotificationToNF(nwPerfNotifyData.getNotificationURI(), NotificationPayload.getNetworkPerformancePayload(nwPerfNotifyData, threshold), NwdafEvent.NETWORK_PERFORMANCE); }


    public void sendUserDataCongestionNotification(UserDataCongestionNotification usrDataCongNotifyData) throws JSONException, IOException
    { sendNotificationToNF(usrDataCongNotifyData.getNotificationURI(), NotificationPayload.getUserDataCongestionPayload(usrDataCongNotifyData), NwdafEvent.USER_DATA_CONGESTION); }


    public void sendAbnormalBehaviourNotification(AbnormalBehaviourNotification abnorBehavrNotifyData) throws JSONException, IOException
    { sendNotificationToNF(abnorBehavrNotifyData.getNotificationURI(), NotificationPayload.getAbnormalBehaviourPayload(abnorBehavrNotifyData), NwdafEvent.ABNORMAL_BEHAVIOUR); }


    public void sendUeCommNotification(UeCommNotification ueCommNotification) throws JSONException, IOException
    { sendNotificationToNF(ueCommNotification.getNotificationURI(), NotificationPayload.getUeCommPayload(ueCommNotification), NwdafEvent.UE_COMM); }



    // Sending Notification Data to the Left side
    public void sendNotificationToNF(String notificationURI, JSONObject notificationPayload, NwdafEvent event) throws JSONException, IOException
    {
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        URL url = new URL(notificationURI);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();


        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);


        try (OutputStream os = con.getOutputStream()) {
            byte[] input = notificationPayload.toString().getBytes("utf-8");

            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read the response from input stream;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }


        EventCounter[event.ordinal()].incrementSubscriptionNotificationsSent();
        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }






    public String subscribeRightSide(String subscribeURI, String correlationID, NwdafEvent event) throws JSONException, IOException
    {
        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        URL obj = new URL(subscribeURI);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");


        JSONObject json = new JSONObject();

        json.put("correlationId", correlationID);
        json.put("notificationTargetAddress", subscribeURI);
        json.put("event", event.ordinal());

        // For POST only - START
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        } catch (IOException e) {

            ErrorCounters.incrementIOException();
            e.printStackTrace();
        }
        // For POST only - END
        int responseCode = con.getResponseCode();

        if (responseCode != HttpStatus.OK.value()) {
            throw new IOException();
        }


        if (responseCode == HttpURLConnection.HTTP_OK) { //success

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            EventCounter[event.ordinal()].incrementSubscriptionsSent();
            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);

            // UnSubCorrelationID received from the Right-Side
            return response.toString();
        }

        return null;
    }






    public void unSubscribeRightSide(String unSubscribeURI, String correlationId, String unSubCorrelationId, NwdafEvent event) throws JSONException, IOException
    {

        URL obj = new URL(unSubscribeURI);


        HttpURLConnection con = (HttpURLConnection) obj.openConnection();


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("unSubCorrelationId", unSubCorrelationId);
        jsonObject.put("correlationId", correlationId);
        jsonObject.put("event", event.ordinal());

        con.setRequestMethod("DELETE");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonObject.toString().getBytes("utf-8");

            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Read the response from input stream;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        EventCounter[event.ordinal()].incrementUnSubscriptionsSent();
    }





    public Object subscribeForEvent(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws JSONException, IOException {

        NwdafEvent event = eventSubscription.getEvent();

        switch(event)
        {
            case LOAD_LEVEL_INFORMATION: return subscribeLoadLevelInformation(eventSubscription, subscriptionId, notificationURI);

            case QOS_SUSTAINABILITY: return subscribeQosSustainability(eventSubscription, subscriptionId, notificationURI);

            case SERVICE_EXPERIENCE: return subscribeServiceExperience(eventSubscription, subscriptionId, notificationURI);

            case NETWORK_PERFORMANCE: return subscribeNetworkPerformance(eventSubscription, subscriptionId, notificationURI);

            case USER_DATA_CONGESTION: return subscribeUserDataCongestion(eventSubscription, subscriptionId, notificationURI);

            case ABNORMAL_BEHAVIOUR: return subscribeAbnormalBehaviour(eventSubscription, subscriptionId, notificationURI);

            case UE_COMM: return subscribeUeComm(eventSubscription, subscriptionId, notificationURI);

        }

        return null;
    }



    // Subscribe for event: LOAD_LEVEL_INFORMATION
    public Object subscribeLoadLevelInformation(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws JSONException {

        // Adding data into NwdafSubscriptionTable   [ Database ]

        SubscriptionTable ldLevelSubTable = new SubscriptionTable(eventSubscription, subscriptionId, notificationURI);
        repository.subscribeNF(ldLevelSubTable);

        // adding values into subscriptionData
        add_values_into_subscriptionData(subscriptionId, eventSubscription.getSnssais().get(0).toString(),
                eventSubscription.getLoadLevelThreshold());

        // Storing data into loadlevelInformation Table
        repository.add_data_into_load_level_table(eventSubscription.getSnssais().get(0).toString());

        Object obj = checkForData_LoadLevelInformation(eventSubscription, false);

        if (obj instanceof ResponseEntity) {
            return obj;
        }

        return null;
    }



    // Subscribe for event: QOS_SUSTAINABILITY
    public Object subscribeQosSustainability(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws IOException, JSONException {


        SubscriptionTable subscriptionTable = new SubscriptionTable(eventSubscription, subscriptionId, notificationURI);
        repository.subscribeNF(subscriptionTable);

        QosSustainabilitySubscriptionData qosSustainabilitySubscriptionData = new QosSustainabilitySubscriptionData(eventSubscription, subscriptionId);
        repository.addDataQosSustainabilitySubscriptionData(qosSustainabilitySubscriptionData);

        QosSustainabilityInformation qosSustainabilityInformation = new QosSustainabilityInformation(eventSubscription);
        repository.setNwdafQosSustainabilityInformation(qosSustainabilityInformation.getSnssai(), qosSustainabilityInformation.getTai());

        String snssai = eventSubscription.getSnssais().get(0).toString();
        String tai = eventSubscription.getNetworkArea().getTais().get(0).toString();

        Object obj = checkForData_QosSustainability(snssai, tai, false);

        if (obj instanceof ResponseEntity) {
            return obj;
        }

        return null;
    }



    // Subscribe for event: SERVICE_EXPERIENCE
    public Object subscribeServiceExperience(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws IOException, JSONException {

        SubscriptionTable subscriptionTable = new SubscriptionTable(eventSubscription, subscriptionId, notificationURI);
        repository.subscribeNF(subscriptionTable);

        ServiceExperienceSubscriptionData serviceExperienceSubscriptionData = new ServiceExperienceSubscriptionData(eventSubscription, subscriptionId);
        repository.addServiceExperienceSubscriptionData(serviceExperienceSubscriptionData);

        ServiceExperienceInformation serviceExperienceInformation = new ServiceExperienceInformation(eventSubscription);
        repository.addServiceExperienceInformation(serviceExperienceInformation.getSupi(), serviceExperienceInformation.getSnssai());

        String supi = eventSubscription.getTgtUe().getSupi();
        String snssai = eventSubscription.getSnssais().get(0).toString();
        Boolean anyUe = (eventSubscription.getTgtUe().getAnyUe() != null) ? eventSubscription.getTgtUe().getAnyUe() : Boolean.FALSE;

        Object obj = checkForData_ServiceExperience(supi, snssai, anyUe, false);

        if (obj instanceof ResponseEntity) {
            return obj;
        }

        return null;
    }



    // Subscribe for event: NETWORK_PERFORMANCE
    public Object subscribeNetworkPerformance(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws IOException, JSONException {

        SubscriptionTable subscriptionTable = new SubscriptionTable(eventSubscription, subscriptionId, notificationURI);
        repository.subscribeNF(subscriptionTable);

        NetworkPerformanceSubscriptionData networkPerformanceSubscriptionData = new NetworkPerformanceSubscriptionData(eventSubscription, subscriptionId);

        if(networkPerformanceSubscriptionData.getAbsoluteNumThrd() != null)
        { repository.addNetworkPerformanceSubscriptionData(networkPerformanceSubscriptionData, NetworkPerfThreshold.ABSOLUTE_NUM); }

        else
        { repository.addNetworkPerformanceSubscriptionData(networkPerformanceSubscriptionData, NetworkPerfThreshold.RELATIVE_RATIO); }

        NetworkPerformanceInformation networkPerformanceInformation = new NetworkPerformanceInformation(eventSubscription);
        repository.addNetworkPerformanceInformation(networkPerformanceInformation.getSupi(), networkPerformanceInformation.getNwPerfType());


        String supi = eventSubscription.getTgtUe().getSupi();
        Integer nwPerfType = eventSubscription.getNwPerfRequs().get(0).getNwPerfType().ordinal();
        Boolean anyUe = (eventSubscription.getTgtUe().getAnyUe() != null) ? eventSubscription.getTgtUe().getAnyUe() : Boolean.FALSE;


        Object obj = checkForData_NetworkPerformance(supi, nwPerfType, anyUe, false);

        if (obj instanceof ResponseEntity) {
            return obj;
        }

        return null;
    }



    // Subscribe for event: USER_DATA_CONGESTION
    public Object subscribeUserDataCongestion(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws IOException, JSONException {

        SubscriptionTable subscriptionTable = new SubscriptionTable(eventSubscription, subscriptionId, notificationURI);
        repository.subscribeNF(subscriptionTable);

        UserDataCongestionSubscriptionData userDataCongestionSubscriptionData = new UserDataCongestionSubscriptionData(eventSubscription, subscriptionId);
        repository.addUserDataCongestionSubscriptionData(userDataCongestionSubscriptionData);

        Tai tai = fetchSupiLocationFromAMF(eventSubscription.getTgtUe().getSupi(), NwdafEvent.USER_DATA_CONGESTION, AmfEventType.LOCATION_REPORT);

        UserDataCongestionInformation userDataCongestionInformation = new UserDataCongestionInformation(eventSubscription, tai.toString());
        repository.addUserDataCongestionInformation(userDataCongestionInformation.getSupi(), userDataCongestionInformation.getCongType(), userDataCongestionInformation.getTai());

        String supi = eventSubscription.getTgtUe().getSupi();
        Integer congType = eventSubscription.getCongType().ordinal();

        Object obj = checkForData_UserDataCongestion(supi, congType,false);

        if (obj instanceof ResponseEntity) {
            return obj;
        }

        return null;
    }



    // Subscribe for event: ABNORMAL_BEHAVIOUR
    public Object subscribeAbnormalBehaviour(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws IOException, JSONException {

        SubscriptionTable subscriptionTable = new SubscriptionTable(eventSubscription, subscriptionId, notificationURI);
        repository.subscribeNF(subscriptionTable);

        AbnormalBehaviourSubscriptionData abnormalBehaviourSubscriptionData = new AbnormalBehaviourSubscriptionData(eventSubscription, subscriptionId);
        repository.addAbnormalBehaviourSubscriptionData(abnormalBehaviourSubscriptionData);

        AbnormalBehaviourInformation abnormalBehaviourInformation = new AbnormalBehaviourInformation(eventSubscription);
        repository.addAbnormalBehaviourInformation(abnormalBehaviourInformation.getSupi(), abnormalBehaviourInformation.getExcepId());

        String supi = eventSubscription.getTgtUe().getSupi();
        Integer excepId = eventSubscription.getExcepRequs().get(0).getExcepId().ordinal();

        Object obj = checkForData_AbnormalBehaviour(supi, excepId, false);

        if(obj instanceof ResponseEntity)
        { return obj; }

        return null;
    }



    public Object subscribeUeComm(EventSubscription eventSubscription, String subscriptionId, String notificationURI) throws IOException, JSONException {

        SubscriptionTable subscriptionTable = new SubscriptionTable(eventSubscription, subscriptionId, notificationURI);
        repository.subscribeNF(subscriptionTable);

        UeCommSubscriptionData ueCommSubscriptionData = new UeCommSubscriptionData(eventSubscription, subscriptionId);
        repository.addUeCommSubscriptionData(ueCommSubscriptionData);

        String supi = eventSubscription.getTgtUe().getSupi();

        Object obj = checkForData_UeComm(supi, false);

        if(obj instanceof ResponseEntity)
        { return obj; }

        return null;
    }


}
