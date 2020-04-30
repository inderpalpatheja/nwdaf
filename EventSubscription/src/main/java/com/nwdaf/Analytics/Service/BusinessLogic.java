package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnectionForUEMobility;
import com.nwdaf.Analytics.Model.AMFModel.AMFModel;
import com.nwdaf.Analytics.Model.AnalyticsInformation.NetworkPerformanceInfo;
import com.nwdaf.Analytics.Model.AnalyticsInformation.QosSustainabilityInfo;
import com.nwdaf.Analytics.Model.AnalyticsInformation.ServiceExperienceInfo;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfTable;
import com.nwdaf.Analytics.Model.CustomData.NetworkPerformance.NetworkPerfThreshold;
import com.nwdaf.Analytics.Model.CustomData.QosSustainabilityData.QosSustainability;
import com.nwdaf.Analytics.Model.CustomData.QosType;
import com.nwdaf.Analytics.Model.CustomData.ServiceExperience.ServiceExperience;
import com.nwdaf.Analytics.Model.MetaData.ErrorCounters;
import com.nwdaf.Analytics.Model.Nnrf.Nnrf_Model;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.NotificationData;
import com.nwdaf.Analytics.Model.NotificationFormat.LoadLevelInformationNotification;
import com.nwdaf.Analytics.Model.NotificationFormat.NetworkPerformanceNotification;
import com.nwdaf.Analytics.Model.NotificationFormat.QosSustainabilityNotification;
import com.nwdaf.Analytics.Model.NotificationFormat.ServiceExperienceNotification;
import com.nwdaf.Analytics.Model.QosNotificationData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
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



    /**
     * @param nnwdafEventsSubscription
     * @throws IOException
     * @throws JSONException
     * @desc function to check snssais data
     */
    protected Object checkForData_LoadLevelInformation(NnwdafEventsSubscription nnwdafEventsSubscription, boolean getAnalytics) throws JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if (getAnalytics) {

            List<EventConnection> snssaisDataList = repository.checkForData(nnwdafEventsSubscription.getSnssais(), nnwdafEventsSubscription.getAnySlice());

            // if data is not found -> calling collector function to collect data
            if (snssaisDataList == null || snssaisDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                Object obj = collectDataForLoadLevelInformation(nnwdafEventsSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // Adding snssais into database
                repository.add_data_into_load_level_table(nnwdafEventsSubscription.getSnssais());

                EventConnection eventConnection = new EventConnection();
                eventConnection.setMessage("Data not Found for " + nnwdafEventsSubscription.getSnssais());
                eventConnection.setCurrentLoadLevelInfo(0);
                eventConnection.setSnssais(nnwdafEventsSubscription.getSnssais());
                eventConnection.setDataStatus(false);

                logger.debug("snssais: " + nnwdafEventsSubscription.getSnssais() + "\n" +
                        " CurrentLoadLevelInfo: " + eventConnection.getCurrentLoadLevelInfo() + "\n" +
                        "DataStatus:  " + eventConnection.getDataStatus());

                //throw new NullPointerException("Data not found!");
                //return eventConnection;
                return new ResponseEntity<EventConnection>(eventConnection, HttpStatus.NOT_FOUND);


            } else {
                // flag to check if getAnalytics ref count will be updated or not
                //flag = 1;
                Object obj = collectDataForLoadLevelInformation(nnwdafEventsSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // if Data found returning JSON
                //return snssaisDataList;
                return new ResponseEntity(snssaisDataList, HttpStatus.OK);
            }

        } else {

            Object obj = collectDataForLoadLevelInformation(nnwdafEventsSubscription, getAnalytics);

            if (obj instanceof ResponseEntity) {
                return obj;
            }
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }

    /**
     * @param nnwdafEventsSubscription
     * @param getAnalytics
     * @return
     * @throws IOException
     * @throws JSONException
     * @desc this will hold functions for data collection from network functions
     */
    protected Object collectDataForLoadLevelInformation(NnwdafEventsSubscription nnwdafEventsSubscription, boolean getAnalytics) throws JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        String snssais = nnwdafEventsSubscription.getSnssais();

        if (!repository.snssaisExists_SliceLoadLevelSubscriptionTable(snssais)) {


            // Generating CorrelationID
            UUID correlationID = FrameWorkFunction.getUniqueID();
            // if Event Id is UE-Mobility nwdaf will subscribe to AMF;
         /*   if (nnwdafEventsSubscription.getEventID().toString() == "UE_MOBILITY") {
                subscribeAMFfromNWDAF(nnwdafEventsSubscription, correlationID, getAnalytics);
            } */


            Nnrf_Model nnrfModel = new Nnrf_Model();

            // Adding correlationID to object and send to SIMULATOR
            nnrfModel.setCorrelationId(String.valueOf(correlationID));
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

                responseHandler_LoadLevelInformation(nnwdafEventsSubscription, responseCode, correlationID, con, getAnalytics);

                if (con != null) {
                    con.disconnect();
                }

                EventCounter[nnwdafEventsSubscription.getEventID()].incrementSubscriptionsSent();

            } catch (IOException ex) {

                ErrorCounters.incrementIOException();
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }



            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return correlationID;
        }

        else if(!getAnalytics)
        { repository.increaseRefCountSliceLoadLevel(nnwdafEventsSubscription.getSnssais()); }

        return null;
    }


    /**
     * Function for UEMobility Data;
     */
    private Object subscribeAMFfromNWDAF(NnwdafEventsSubscription nnwdafEventsSubscription,
                                         UUID correlationID, boolean getAnalytics) {
        try {

            URL obj = new URL(POST_AMF_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            // Function To Send CorrelationID to SIMULATOR
            AMFModel amfModel = new AMFModel();
            int responseCode = send_data_receieve_response_AMF(amfModel, con);

            if (responseCode != HttpStatus.OK.value()) {
                throw new Exception();
            }

            responseHandler_LoadLevelInformation(nnwdafEventsSubscription, responseCode, correlationID, con, getAnalytics);

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

        json.put("correlationID", amfModel.getCorrelationId());
        json.put("unSubCorrelationId", amfModel.getUnSubCorrelationId());


        logger.info("correlationID:  " + amfModel.getCorrelationId() + "\n" +
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

    /**
     * @param nnwdafEventsSubscription
     * @param responseCode
     * @param correlationID
     * @param con
     * @throws IOException
     * @desc function to send response header to NF consumers when they subscribe
     */
    protected void responseHandler_LoadLevelInformation(NnwdafEventsSubscription nnwdafEventsSubscription,
                                                        int responseCode, UUID correlationID,
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
                slice.setCorrelationID(String.valueOf(correlationID));
                slice.setSubscriptionID(response.toString());
                slice.setSnssais(nnwdafEventsSubscription.getSnssais());

                if (getAnalytics) {
                    if (!repository.snssaisExists_SliceLoadLevelSubscriptionTable(slice.getSnssais())) {
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
    protected void add_values_into_subscriptionData(UUID subscriptionID, String snssais, int loadLevelThreshold) {


        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        SliceLoadLevelSubscriptionData nwdafSliceLoadLevelSubscriptionDataModel = new
                SliceLoadLevelSubscriptionData();

        // Setting values in NwdafSliceLoadLevelSubscriptionData object
        nwdafSliceLoadLevelSubscriptionDataModel.setSubscriptionID(String.valueOf(subscriptionID));
        nwdafSliceLoadLevelSubscriptionDataModel.setSnssais(snssais);
        nwdafSliceLoadLevelSubscriptionDataModel.setLoadLevelThreshold(loadLevelThreshold);

        // Adding nwdafSliceLoadLevelSubscriptionDatamodel into NwdafSliceLoadLevelSubscriptionData Table [ Database ]
        repository.addDataIntoNwdafSliceLoadLevelSubscriptionDataTable(nwdafSliceLoadLevelSubscriptionDataModel);
        //logger.debug("enter add_values_into_subscriptionData");

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }

    /**
     * @param subscriptionID
     * @return
     * @throws URISyntaxException
     * @desc function to send response header to NF consumers
     */
    protected HttpHeaders send_response_header_to_NF(UUID subscriptionID) throws URISyntaxException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        URI location = new URI(URI + "subscriptions/" + String.valueOf(subscriptionID));
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

        json.put("correlationID", nnrfModel.getCorrelationId());
        json.put("unSubCorrelationId", nnrfModel.getUnSubCorrelationId());


        logger.info("correlationID:  " + nnrfModel.getCorrelationId() + "\n" +
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

        String correlationID = repository.getCorrelationID(unSubCorrelationID, EventID.LOAD_LEVEL_INFORMATION);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("unSubCorrelationID", unSubCorrelationID);
        jsonObject.put("correlationID", correlationID);

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
            List<NotificationData> dataSet = repository.getAllNotificationData(slice.getSnssais(), slice.getCurrentLoadLevel());

            if (dataSet != null && !dataSet.isEmpty()) {
                for (NotificationData notifyData : dataSet) {
                  /*  if (!subID_SET.contains(notifyData.getSubscriptionID())) {
                        subID_SET.add(notifyData.getSubscriptionID());

                        //SubscriptionTable subscriptionData = repository.findById_subscriptionID(notifyData.getSubscriptionID());
                        send_notificaiton_to_NF(repository.getNotificationURI(notifyData.getSubscriptionID()), EventID.LOAD_LEVEL_INFORMATION, slice.getSnssais(), slice.getCurrentLoadLevel(), notifyData.getSubscriptionID());
                    } */

                    //send_notificaiton_to_NF(repository.getNotificationURI(notifyData.getSubscriptionID()), EventID.LOAD_LEVEL_INFORMATION, slice.getSnssais(), slice.getCurrentLoadLevel(), notifyData.getSubscriptionID());
                    initiate_SliceLoadLevelNotifyData(ldLevelNotifyData, notifyData.getSubscriptionID(), repository.getNotificationURI(notifyData.getSubscriptionID()), slice.getSnssais(), slice.getCurrentLoadLevel());
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
            List<QosSustainabilityInformation> list = repository.getAllPlmnId_SnssaisForQos(qosType);

            if (list == null || list.isEmpty()) {
                logger.warn("no snssais Found [ Null object ]");
                return;
            }

            QosSustainabilityNotification qosNotifyData = new QosSustainabilityNotification();

            for (QosSustainabilityInformation qos : list) {
                List<QosNotificationData> dataSet;

                if (qosType == QosType.RAN_UE_THROUGHPUT) {
                    dataSet = repository.getAllQosNotificationData(qos.getPlmnID(), qos.getSnssais(), qos.getRanUeThroughput(), qosType);
                } else {
                    dataSet = repository.getAllQosNotificationData(qos.getPlmnID(), qos.getSnssais(), qos.getQosFlowRetain(), qosType);
                }


                if (dataSet != null && !dataSet.isEmpty()) {
                    if (qosType == QosType.RAN_UE_THROUGHPUT) {
                        for (QosNotificationData notifyData : dataSet) {
                            initiate_QosSustainabilityNotifyData(qosNotifyData, notifyData.getSubscriptionID() ,repository.getNotificationURI(notifyData.getSubscriptionID()), qos.getPlmnID() ,qos.getSnssais(), notifyData.getTac() ,qos.getRanUeThroughput(), QosType.RAN_UE_THROUGHPUT);
                        }
                    } else {
                        for (QosNotificationData notifyData : dataSet) {
                            initiate_QosSustainabilityNotifyData(qosNotifyData, notifyData.getSubscriptionID() ,repository.getNotificationURI(notifyData.getSubscriptionID()), qos.getPlmnID() ,qos.getSnssais(), notifyData.getTac() ,qos.getQosFlowRetain(), QosType.QOS_FLOW_RETAIN);
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



    public void initiate_QosSustainabilityNotifyData(QosSustainabilityNotification qosNotifyData, String subscriptionID, String notificationURI, String plmnID, String snssais, String tac, Integer threshold, QosType thresholdType) throws IOException, JSONException {

        qosNotifyData.setSubscriptionID(subscriptionID);
        qosNotifyData.setNotificationURI(notificationURI);
        qosNotifyData.setPlmnID(plmnID);
        qosNotifyData.setSnssais(snssais);
        qosNotifyData.setTac(tac);
        qosNotifyData.setThreshold(threshold);
        qosNotifyData.setThresholdType(thresholdType);

        sendQosSustainabilityNotification(qosNotifyData);
    }


    public void initiate_SliceLoadLevelNotifyData(LoadLevelInformationNotification ldLevelNotifyData, String subscriptionID, String notificationURI, String snssais, Integer currentLoadLevel) throws IOException, JSONException {

        ldLevelNotifyData.setSubscriptionID(subscriptionID);
        ldLevelNotifyData.setNotificationURI(notificationURI);
        ldLevelNotifyData.setSnssais(snssais);
        ldLevelNotifyData.setCurrentLoadLevel(currentLoadLevel);

        sendLoadLevelInformationNotification(ldLevelNotifyData);
    }





    /**************************UE_MOBILITY code*********************************************************************/


    protected Object check_For_data_for_UE_Mobility(NnwdafEventsSubscription nnwdafEventsSubscription, boolean getAnalytics) throws IOException, JSONException {


        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (getAnalytics) {

            List<EventConnectionForUEMobility> supiDataList = repository.checkForUEMobilityData(nnwdafEventsSubscription.getSupi());

            // if data is not found -> calling collector function to collect data
            if (supiDataList == null || supiDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                // Object obj = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics);

                //  if (obj instanceof ResponseEntity) {
                //      return obj;
                //  }

                // Calling collector function
                Object obj = nwdaf_data_collector_For_UE_Mobility(nnwdafEventsSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }


                // Adding snssais into database
                //  repository.add_data_into_load_level_table(nnwdafEventsSubscription.getSnssais());
                UEMobilitySubscriptionModel ueMobilitySubscriptionModel = new UEMobilitySubscriptionModel();
                ueMobilitySubscriptionModel.setSupi(nnwdafEventsSubscription.getSupi());

                repository.add_data_into_nwdafUEmobility(ueMobilitySubscriptionModel);

                EventConnectionForUEMobility eventConnection = new EventConnectionForUEMobility();
                eventConnection.setSupi(nnwdafEventsSubscription.getSupi());
                eventConnection.setMessage("Data not Found for " + nnwdafEventsSubscription.getSupi());

                eventConnection.setDataStatus(false);


                out.println("supi------->" + ueMobilitySubscriptionModel.getSupi());


                return eventConnection;

            } else {
                // flag to check if getAnalytics ref count will be updated or not
                //flag = 1;
                Object obj = nwdaf_data_collector_For_UE_Mobility(nnwdafEventsSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // if Data found returning JSON
                return supiDataList;
            }

        } else {

            // subscription Data Collector
            Object obj = nwdaf_data_collector_For_UE_Mobility(nnwdafEventsSubscription, getAnalytics);

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


    private Object nwdaf_data_collector_For_UE_Mobility(NnwdafEventsSubscription nnwdafEventsSubscription, boolean getAnalytics) {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        Object snssais_object = repository.findby_supi(nnwdafEventsSubscription.getSupi());

        if (snssais_object == null) {

            // Generating CorrelationID
            UUID correlationID = FrameWorkFunction.getUniqueID();

            // if Event Id is UE-Mobility nwdaf will subscribe to AMF;

          /* if (nnwdafEventsSubscription.getEventID().toString() == "UE_MOBILITY") {
                subscribeAMFfromNWDAF(nnwdafEventsSubscription, correlationID, getAnalytics);
            }*/

            AMFModel amfModel = new AMFModel();

            // Adding correlationID to object and send to SIMULATOR
            amfModel.setCorrelationId(String.valueOf(correlationID));
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

                response_handler_for_UEMobility(nnwdafEventsSubscription, responseCode, correlationID, con, getAnalytics);

                if (con != null) {
                    con.disconnect();
                }

            } catch (Exception ex) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }

            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return correlationID;
        } else {
            repository.increment_ref_count_ofSupi(nnwdafEventsSubscription.getSupi());
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

        eventNotificationObject.put("NwdafEvent", EventID.UE_MOBILITY.toString());
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

        EventCounter[EventID.UE_MOBILITY.ordinal()].incrementSubscriptionNotificationsSent();
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
            UUID correlationID = FrameWorkFunction.getUniqueID();


            //  Nnrf_Model nnrfModel = new Nnrf_Model();

            AMFModel amfModel = new AMFModel();
            // Adding correlationID to object and send to SIMULATOR
            amfModel.setCorrelationId(String.valueOf(correlationID));
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

                response_handlerUEmobility(nnwdafEventsSubscriptionUE, responseCode, correlationID, con, getAnalytics);

                if (con != null) {
                    con.disconnect();
                }

            } catch (Exception ex) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }

            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return correlationID;
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


    private void response_handler_for_UEMobility(NnwdafEventsSubscription nnwdafEventsSubscription, int responseCode, UUID correlationID, HttpURLConnection con, boolean getAnalytics) throws IOException {


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

                uEmobilitySubscriptionTable.setCorrelationID(correlationID);
                uEmobilitySubscriptionTable.setSubscriptionID(UUID.fromString(response.toString()));
                uEmobilitySubscriptionTable.setSupi(nnwdafEventsSubscription.getSupi());

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
                                              int responseCode, UUID correlationID,
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
                UEobj.setCorrelationID(String.valueOf(correlationID));
                UEobj.setSubscriptionID(response.toString());
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


    /***************************************QOS_SUSTAINABILITY************************************************************************/



    public Object checkForData_QosSustainability(NnwdafEventsSubscription subscription, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        String plmnID = subscription.getPlmnID();
        String snssais = subscription.getSnssais();

        if(getAnalytics)
        {
            List<QosSustainabilityInfo> qosInfo = repository.getQosSustainabilityInfo(plmnID, snssais);
            collectDataForQosSustainability(subscription, true);

            repository.setNwdafQosSustainabilityInformation(plmnID, snssais);

            if(qosInfo == null || qosInfo.isEmpty())
            { return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND); }

            else
            { return new ResponseEntity(qosInfo, HttpStatus.OK); }
        }

        else
        { collectDataForQosSustainability(subscription, false); }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }



    public void collectDataForQosSustainability(NnwdafEventsSubscription subscription, boolean getAnalytics) throws IOException, JSONException {
        String plmnID = subscription.getPlmnID();
        String snssais = subscription.getSnssais();

        if(repository.plmnIdSnssaisExists_QosSustainability(plmnID, snssais, QosSustainability.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.increaseRefCountQosSustainability(plmnID, snssais); }
        }

        else
        {
            String correlationID = FrameWorkFunction.getUniqueID().toString();
            String unSubCorrelationID = subscribeRightSide(POST_OAM_URL, correlationID, EventID.QOS_SUSTAINABILITY);

            responseHandlerQosSustainability(plmnID, snssais, correlationID, unSubCorrelationID, getAnalytics);
        }
    }




    public void responseHandlerQosSustainability(String plmnID, String snssais, String correlationID, String unSubCorrelationID, boolean getAnalytics)
    {
        QosSustainabilitySubscriptionTable qos = new QosSustainabilitySubscriptionTable();

        qos.setPlmnID(plmnID);
        qos.setSnssais(snssais);
        qos.setSubscriptionID(unSubCorrelationID);
        qos.setCorrelationID(correlationID);

        repository.addDataQosSustainabilitySubscriptionTable(qos, getAnalytics);
    }









    /***************************************SERVICE_EXPERIENCE************************************************************************/






    public Object checkForData_ServiceExperience(NnwdafEventsSubscription subscription, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        String supi = subscription.getSupi();
        String snssais = subscription.getSnssais();

        Boolean anyUE = subscription.getAnyUE();

        if(getAnalytics)
        {
            List<ServiceExperienceInfo> svcExpInfo = repository.getServiceExperienceInfo(supi, snssais, anyUE);
            collectDataForServiceExperience(subscription, true);

            repository.addServiceExperienceInformation(subscription);

            if(svcExpInfo == null || svcExpInfo.isEmpty())
            { return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND); }

            else
            { return new ResponseEntity(svcExpInfo, HttpStatus.OK); }

        }

        else
        { collectDataForServiceExperience(subscription, false); }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }



    public void collectDataForServiceExperience(NnwdafEventsSubscription subscription, boolean getAnalytics) throws IOException, JSONException {

        String supi = subscription.getSupi();
        String snssais = subscription.getSnssais();

        if(repository.supiSnssaisExist_ServiceExperience(supi, snssais, ServiceExperience.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.updateRefCount_ServiceExperience(supi, snssais); }
        }

        else
        {
            String correlationID = FrameWorkFunction.getUniqueID().toString();
            String unSubCorrelationID = subscribeRightSide(POST_NF_URL, correlationID, EventID.SERVICE_EXPERIENCE);

            responseHandlerServiceExperience(supi, snssais, correlationID, unSubCorrelationID, getAnalytics);
        }

    }



    public void responseHandlerServiceExperience(String supi, String snssais, String correlationID, String unSubCorrelationID, boolean getAnalytics)
    {

        ServiceExperienceSubscriptionTable svcExp = new ServiceExperienceSubscriptionTable();

        svcExp.setSupi(supi);
        svcExp.setSnssais(snssais);
        svcExp.setCorrelationID(correlationID);
        svcExp.setSubscriptionID(unSubCorrelationID);

        repository.addServiceExperienceSubscriptionTable(svcExp, getAnalytics);

    }





    /***************************************NETWORK_PERFORMANCE************************************************************************/


    public Object checkForData_NetworkPerformance(NnwdafEventsSubscription subscription, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        String supi = subscription.getSupi();
        Integer nwPerfType = subscription.getNwPerfType();

        Boolean anyUE = subscription.getAnyUE();

        if(getAnalytics)
        {
            List<Object> nwPerfInfo = repository.getNetworkPerformanceInfo(supi, nwPerfType, anyUE);
            collectDataForNetworkPerformance(subscription, true);

            repository.addNetworkPerformanceInformation(subscription);

            if(nwPerfInfo == null || nwPerfInfo.isEmpty())
            { return new ResponseEntity<String>("Data Not Found", HttpStatus.NOT_FOUND); }

            else
            { return new ResponseEntity(nwPerfInfo, HttpStatus.OK); }

        }

        else
        { collectDataForNetworkPerformance(subscription, false); }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
        return null;
    }



    public void collectDataForNetworkPerformance(NnwdafEventsSubscription subscription, boolean getAnalytics) throws IOException, JSONException {

        String supi = subscription.getSupi();
        Integer nwPerfType = subscription.getNwPerfType();

        if(repository.supi_nwPerfTypeExists(supi, nwPerfType, NetworkPerfTable.SUBSCRIPTION_TABLE))
        {
            if(!getAnalytics)
            { repository.updateRefCount_NetworkPerformance(supi, nwPerfType); }
        }

        else
        {
            String correlationID = FrameWorkFunction.getUniqueID().toString();
            String unSubcorrelationID = subscribeRightSide(POST_OAM_URL, correlationID, EventID.NETWORK_PERFORMANCE);

            responseHandlerNetworkPerformance(supi, nwPerfType, unSubcorrelationID, correlationID, getAnalytics);
        }
    }



    public void responseHandlerNetworkPerformance(String supi, Integer nwPerfType, String subscriptionID, String correlationID, boolean getAnalytics)
    {
        NetworkPerformanceSubscriptionTable nwPerfSubTable = new NetworkPerformanceSubscriptionTable();

        nwPerfSubTable.setSupi(supi);
        nwPerfSubTable.setNwPerfType(nwPerfType);
        nwPerfSubTable.setSubscriptionID(subscriptionID);
        nwPerfSubTable.setCorrelationID(correlationID);

        repository.addNetworkPerformanceSubscriptionTable(nwPerfSubTable, getAnalytics);
    }





    public void sendLoadLevelInformationNotification(LoadLevelInformationNotification ldLevelNotifyData) throws JSONException, IOException
    { sendNotificationToNF(ldLevelNotifyData.getNotificationURI(), NotificationPayload.getLoadLevelInformationPayload(ldLevelNotifyData), EventID.LOAD_LEVEL_INFORMATION); }


    public void sendQosSustainabilityNotification(QosSustainabilityNotification qosNotifyData) throws JSONException, IOException
    { sendNotificationToNF(qosNotifyData.getNotificationURI(), NotificationPayload.getQosSustainabilityPayload(qosNotifyData), EventID.QOS_SUSTAINABILITY); }


    public void sendServiceExperienceNotification(ServiceExperienceNotification svcExpNotifyData) throws JSONException, IOException
    { sendNotificationToNF(svcExpNotifyData.getNotificationURI(), NotificationPayload.getServiceExperiencePayload(svcExpNotifyData), EventID.SERVICE_EXPERIENCE); }


    public void sendNetworkExperienceNotification(NetworkPerformanceNotification nwPerfNotifyData, NetworkPerfThreshold threshold) throws JSONException, IOException
    { sendNotificationToNF(nwPerfNotifyData.getNotificationURI(), NotificationPayload.getNetworkPerformancePayload(nwPerfNotifyData, threshold), EventID.NETWORK_PERFORMANCE); }



    // Sending Notification Data to the Left side
    public void sendNotificationToNF(String notificationURI, JSONObject notificationPayload, EventID eventID) throws JSONException, IOException
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


        EventCounter[eventID.ordinal()].incrementSubscriptionNotificationsSent();
        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }






    public String subscribeRightSide(String subscribeURI, String correlationID, EventID eventID) throws JSONException, IOException
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

        json.put("correlationID", correlationID);
        json.put("notificationTargetAddress", subscribeURI);
        json.put("eventID", eventID.ordinal());

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

            EventCounter[eventID.ordinal()].incrementSubscriptionsSent();
            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);

            // UnSubCorrelationID received from the Right-Side
            return response.toString();
        }

        return null;
    }






    public void unSubscribeRightSide(String unSubscribeURI, String correlationID, String unSubCorrelationID, EventID eventID) throws JSONException, IOException
    {

        URL obj = new URL(unSubscribeURI);


        HttpURLConnection con = (HttpURLConnection) obj.openConnection();


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("unSubCorrelationID", unSubCorrelationID);
        jsonObject.put("correlationID", correlationID);
        jsonObject.put("eventID", eventID.ordinal());

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

        EventCounter[eventID.ordinal()].incrementUnSubscriptionsSent();
    }


}
