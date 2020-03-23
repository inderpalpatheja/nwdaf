package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Model.AMFModel.AMFModel;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.Nnrf.Nnrf_Model;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.NotificationData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SubscriptionTable;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;

import com.nwdaf.Analytics.Model.TableType.UEMobility.UserLocation;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionData;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobility;
import com.nwdaf.Analytics.Model.TableType.UEMobility.UEMobilitySubscriptionTable;

import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.*;
import java.util.*;

import static java.lang.System.out;
import static java.lang.System.setOut;
import static org.springframework.http.HttpHeaders.USER_AGENT;

public class BusinessLogic extends ResourceValues {


    @Autowired
    Nnwdaf_Repository repository;
    //checking

//    JSONObject jsonForUEMobility = new JSONObject();
//    JSONObject jsonForSliceLoadLevel = new JSONObject();
//    JSONArray jsonArrayFinalNotification = new JSONArray();
//    JSONObject jsonObjectFinalNotification = new JSONObject();

    private static final Logger logger = LoggerFactory.getLogger(BusinessLogic.class);

    Set<String> subID_SET = new HashSet<String>();
    List<UserLocation> userLocations = new ArrayList<>();


    /**
     * @param nnwdafEventsSubscription
     * @throws IOException
     * @throws JSONException
     * @desc function to check snssais data
     */
    protected Object check_For_data(NnwdafEventsSubscription nnwdafEventsSubscription, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (getAnalytics) {
            List<EventConnection> snssaisDataList = repository.checkForData(nnwdafEventsSubscription.getSnssais(),
                    nnwdafEventsSubscription.getAnySlice(), nnwdafEventsSubscription.getEventID());

            // if data is not found -> calling collector function to collect data
            if (snssaisDataList == null || snssaisDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                Object obj = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics);

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
                return eventConnection;

            } else {
                // flag to check if getAnalytics ref count will be updated or not
                //flag = 1;
                Object obj = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // if Data found returning JSON
                return snssaisDataList;
            }

        } else {

            Object obj = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics);

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
    protected Object nwdaf_data_collector(NnwdafEventsSubscription nnwdafEventsSubscription, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if (!snsExists(nnwdafEventsSubscription.getEventID(), nnwdafEventsSubscription.getSnssais())) {

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
                    throw new Exception();
                }

                response_handler(nnwdafEventsSubscription, responseCode, correlationID, con, getAnalytics);

                if (con != null) {
                    con.disconnect();
                }

            } catch (Exception ex) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }

            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return correlationID;
        } else {

            if (!getAnalytics) {
                increaseRefCount(nnwdafEventsSubscription.getEventID(), nnwdafEventsSubscription.getSnssais());
            }
        }
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

            response_handler(nnwdafEventsSubscription, responseCode, correlationID, con, getAnalytics);

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
        json.put("notificationTargetAddress", notificationTargetUrl);

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
    protected void response_handler(NnwdafEventsSubscription nnwdafEventsSubscription,
                                    int responseCode, UUID correlationID,
                                    HttpURLConnection con, boolean getAnalytics) throws IOException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            // incrementing Counter
            Counters.incrementCollectorSubscriptions();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            try {

                int eventID = nnwdafEventsSubscription.getEventID();

                if (eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal()) {
                    SliceLoadLevelSubscriptionTable slice = new SliceLoadLevelSubscriptionTable();
                    slice.setCorrelationID(String.valueOf(correlationID));
                    slice.setSubscriptionID(response.toString());
                    slice.setSnssais(nnwdafEventsSubscription.getSnssais());

                    if (getAnalytics) {
                        if (!repository.snsExistsLoadLevelSubscriptionTable(slice.getSnssais())) {
                            repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(slice, true);
                        }
                    } else {
                        repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(slice, false);
                    }
                } else if (eventID == EventID.QOS_SUSTAINABILITY.ordinal()) {
                    QosSustainabilitySubscriptionTable qos = new QosSustainabilitySubscriptionTable();

                    qos.setCorrelationID(String.valueOf(correlationID));
                    qos.setSubscriptionID(response.toString());
                    qos.setSnssais(nnwdafEventsSubscription.getSnssais());

                    if (getAnalytics) {
                        if (!repository.snsExistsQosSubscriptionTable(qos.getSnssais())) {
                            repository.addDataQosSustainabilitySubscriptionTable(qos, true);
                        }
                    } else {
                        repository.addDataQosSustainabilitySubscriptionTable(qos, false);
                    }
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


        // notificationTargetUrl = Namf_EventExposure_Notify
        json.put("notificationTargetAddress", notificationTargetUrl);

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

    /**
     * @param notificationURI
     * @throws IOException
     * @desc this function will send notification to network function
     */
    protected void send_notificaiton_to_NF(String notificationURI,
                                           String eventID,
                                           String snssais,
                                           List<UserLocation> userLocations,
                                           String supi,
                                           int currentLoadLevel,
                                           String subscriptionID) throws IOException, JSONException {

        JSONObject jsonForUEMobility = new JSONObject();
        JSONObject jsonForSliceLoadLevel = new JSONObject();
        JSONArray jsonArrayFinalNotification = new JSONArray();
        JSONObject jsonObjectFinalNotification = new JSONObject();

        jsonObjectFinalNotification.put("subscriptionID", subscriptionID);

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        Counters.incrementSubscriptionNotifications();


        out.println("eventID - " + eventID);


        String subscriptionURI = "http://localhost:8081/nnwdaf-eventssubscription/v1/subscriptions";

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


        if (eventID.equals("UE_MOBILITY")) {

            // out.println("sheetal_UE");
            // out.println(eventID);

            jsonForUEMobility.put("eventID", eventID);
            jsonForUEMobility.put("supi", supi);


            for (int i = 0; i < userLocations.size(); i++) {

                String key = "userLocation-" + i;
                String taiValue = userLocations.get(i).getTai();
                String cellIDValue = userLocations.get(i).getCellID();

                // String finalLocationValue = taiValue + "||" + cellIDValue;
                out.println("\n\n\ntaiValue" + taiValue + "\n\n\n");
                //String[] splitedString = splitLocationString.split(",");
                String[] taiSplitValue = taiValue.split(":");
                String plmnValues = taiSplitValue[0];
                String TaiValue = taiSplitValue[1];

                out.println("\n\nplmnValues" + plmnValues);
                out.println("\n\nTacValue" + TaiValue);

                String[] plmnSplittedValues = plmnValues.split(",");
                String MCC = plmnSplittedValues[0];
                String MNC = plmnSplittedValues[1];
                String finalLocationValue = " MCC - " + MCC + " MNC - " + MNC + " Tai Value - " + taiValue + " Cell-ID - " + cellIDValue;

                jsonForUEMobility.put(key, finalLocationValue);
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
        //jsonArrayFinalNotification.put(jsonForUEMobility);


        // For POST only - START
        // con.setDoOutput(true);
        //  out.println("final-notification-test-1" + jsonArrayFinalNotification.toString());
        // For POST only - START
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {

            out.println("final-notification-test-2" + jsonArrayFinalNotification.toString());
            byte[] input = jsonArrayFinalNotification.toString().getBytes("utf-8");


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

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }

    // @RequestMapping(method = RequestMethod.DELETE, value = "/Nnrf_NFManagement_NFStatusUnSubscribe")
    // @RequestMapping("/t")
    protected void unsubscribeFromNWDAF(String snssais, Integer eventID) throws Exception {

        // here subscriptionID = unsubCorrealtionID

        // String subscriptionID =  repository.getUnSubCorrelationID(snssais);

        URL obj = new URL(DELETE_NRF_URL);
        //  out.println(DELETE_NRF_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        String mCorrelationID = "";
        String correlationID = "";

        if (eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal()) {
            mCorrelationID = repository.getUnSubCorrelationID_LoadLevelInformation(snssais);
            //   String mCorrelationID = repository.getUnSubCorrelationID(snssais);

            correlationID = repository.getCorrelationID_LoadLevelInformation(mCorrelationID);
        } else if (eventID == EventID.QOS_SUSTAINABILITY.ordinal()) {
            mCorrelationID = repository.getUnSubCorrelationID_QosSustainability(snssais);
            correlationID = repository.getCorrelationID_QosSustainability(mCorrelationID);
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mSubcorrelationID", mCorrelationID);
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

        } catch (Exception ex) {
            return false;
        }

        return true;
    }


    // Updated nwdaf_notification_manager 3rd March, 2020
    protected void nwdaf_notification_manager() throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        // Fetching all snssais list
        List<SliceLoadLevelInformation> list = repository.getALLsnssais();

        if (repository.getALLsnssais() == null) {
            logger.warn("no snssais Found [ Null object ]");
        }

        for (SliceLoadLevelInformation slice : list) {
            List<NotificationData> dataSet = repository.getAllNotificationData(slice.getSnssais(), slice.getCurrentLoadLevel());

            if (dataSet != null) {
                for (NotificationData notifyData : dataSet) {
                    if (!subID_SET.contains(notifyData.getSubscriptionID())) {
                        subID_SET.add(notifyData.getSubscriptionID());

                        // SubscriptionTable subscriptionData = repository.findById_subscriptionID(notifyData.getSubscriptionID());
                        // send_notificaiton_to_NF(subscriptionData.getNotificationURI(), EventID.values()[subscriptionData.getEventID()].toString(), slice.getSnssais(), slice.getCurrentLoadLevel(), subscriptionData.getSubscriptionID());

                        SubscriptionTable subscriptionData = repository.findById_subscriptionID(notifyData.getSubscriptionID());
                        send_notificaiton_to_NF(subscriptionData.getNotificationURI(), EventID.values()[subscriptionData.getEventID()].toString(),
                                slice.getSnssais(),
                                userLocations,
                                "null",
                                slice.getCurrentLoadLevel(),
                                subscriptionData.getSubscriptionID());
                    }


                }
            }
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }


    protected Object check_For_data_for_UE_Mobility(NnwdafEventsSubscription nnwdafEventsSubscription, boolean getAnalytics) throws IOException, JSONException {


        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (getAnalytics) {

            List<UEMobility> supiDataList = repository.checkForUEMobilityData(nnwdafEventsSubscription.getSupi());

            // if data is not found -> calling collector function to collect data
            if (supiDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                // Object obj = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics);

                //  if (obj instanceof ResponseEntity) {
                //      return obj;
                //  }

                // Adding snssais into database
                //  repository.add_data_into_load_level_table(nnwdafEventsSubscription.getSnssais());

                EventConnection eventConnection = new EventConnection();
                eventConnection.setMessage("Data not Found for " + nnwdafEventsSubscription.getSnssais());
                //  eventConnection.setCurrentLoadLevelInfo(0);
                // eventConnection.setSnssais(nnwdafEventsSubscription.getSnssais());
                eventConnection.setDataStatus(false);

                //  logger.debug("snssais: " + nnwdafEventsSubscription.getSnssais() + "\n" +
                //        " CurrentLoadLevelInfo: " + eventConnection.getCurrentLoadLevelInfo() + "\n" +
                //      "DataStatus:  " + eventConnection.getDataStatus());

                //throw new NullPointerException("Data not found!");
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

    private int send_data_receieve_response_For_UEMobility(AMFModel amfModel, HttpURLConnection con) throws JSONException, IOException {

        JSONObject json = new JSONObject();

        json.put("correlationID", amfModel.getCorrelationId());
        json.put("unSubCorrelationId", amfModel.getUnSubCorrelationId());


        // notificationTargetUrl = Namf_EventExposure_Notify
        json.put("notificationTargetAddress", notificationTargetUrl);

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
            // incrementing Counter
            Counters.incrementCollectorSubscriptions();

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
                    if (!repository.supiExists(uEmobilitySubscriptionTable.getSupi())) {
                        repository.addCorrealationIDAndUnSubCorrelationIDIntonwdafUEmobilitySubscriptionTable(uEmobilitySubscriptionTable, true);
                    }
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


    public void add_value_to_userLocationTable(String taiValue, String cellID, Integer timeDuration) {

        repository.addValuesToUserLocationTable(taiValue, cellID, timeDuration);
    }


    public void updateUEMobilityTable(String correlationID) {

        //Fetch all IDs from UserLcoation table
        List<Integer> IDs = repository.getAllIDFromUserLocationTable();

        // finding the supi value by correlationID
        String supi = repository.getSupiValueByCorrelationID(correlationID);

        //Updating the location of supi;
        repository.updateLocatoinValueToUEMobilityTable(IDs, supi);
    }


    public boolean snsExists(Integer eventID, String snssais) {
        if (eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal()) {
            return repository.snsExistsLoadLevelSubscriptionTable(snssais);
        } else if (eventID == EventID.QOS_SUSTAINABILITY.ordinal()) {
            return repository.snsExistsQosSubscriptionTable(snssais);
        }

        return true;
    }


    public void increaseRefCount(Integer eventID, String snssais) {
        if (eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal()) {
            repository.increaseRefCount_LoadLevelSubscriptionTable(snssais);
        } else if (eventID == EventID.QOS_SUSTAINABILITY.ordinal()) {
            repository.increaseRefCount_QosSubscriptionTable(snssais);
        }
    }


    protected void nwdaf_notification_manager_ForUEMobility() throws IOException, JSONException {

        List<String> subcriptionIDSet = new ArrayList<>();

        // List<String> subscriptionIDList = new ArrayList<>();


        UserLocation userLocation = new UserLocation();


        List<UEMobility> supiList = repository.getAllSupi();

        if (repository.getALLsnssais() == null) {
            logger.warn("no snssais Found [ Null object ]");
        }

        for (int i = 0; i < supiList.size(); i++) {
            // Generic Function to get all location via supi value
            String location = repository.getAllNotificationDataForUEMobility(supiList.get(i).getSupi());

            // Removing [ ] from location String
            String splitLocationString = location.substring(1, location.length() - 1);

            // Splitting location via ,
            String[] splitedString = splitLocationString.split(",");

            // iterating through split string and finding User Location from particular ID
            for (int j = 0; j < splitedString.length; j++) {

                /* now it's time to find the UserLocation by particular ID */

                // Type Casting String ID value to Integer Value;
                Integer ID = Integer.valueOf(splitedString[j].trim());

                //Finding UseLocation Via ID.
                userLocation = repository.getUserLocationFromID(ID);

                userLocations.add(userLocation);
                // out.println("user-location : Tai-value " + userLocation.getTai());
            }

            out.println("supi value = " + supiList.get(i).getSupi());

            List<String> subscriptionDataList = repository.findById_supi(supiList.get(i).getSupi());

            out.println("\n\nSubscriptionListsize" + subscriptionDataList.size());


            for (int k = 0; k < subscriptionDataList.size(); k++) {
                out.println("\nsubscriptionID_VIA_supi " + subscriptionDataList.get(k));
                SubscriptionTable subscriptionData = repository.findById_subscriptionID(subscriptionDataList.get(k));

                //out.println("\nEventID " + subscriptionData.getEventID() +
                //  "\nSubscriptionID - "+ subscriptionData.getSubscriptionID());

                out.println("\nnotificaitonURI - " + subscriptionData.getNotificationURI() +
                        "\neventID - " + EventID.values()[subscriptionData.getEventID()].toString() +
                        "\nsupi - " + supiList.get(i).getSupi() +
                        "\nTai value - " + userLocation.getTai() +
                        "\nSubscriptionID - " + subscriptionData.getSubscriptionID());


                if (!subID_SET.contains(subscriptionDataList.get(k))) {
                    subID_SET.add(subscriptionDataList.get(k));

                    // send_notificaiton_to_NF_forUEMobility(subscriptionData.getNotificationURI(),
                    //       EventID.values()[subscriptionData.getEventID()].toString());


                    send_notificaiton_to_NF(subscriptionData.getNotificationURI(),
                            EventID.values()[subscriptionData.getEventID()].toString(),
                            "null",
                            userLocations,
                            supiList.get(i).getSupi(),
                            0, subscriptionData.getSubscriptionID());
                }

            }

        }
    }
}
