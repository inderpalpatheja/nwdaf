package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnectionUE;
import com.nwdaf.Analytics.Model.AMFModel.AMFModel;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.Nnrf.Nnrf_Model;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscriptionUEmobility;
import com.nwdaf.Analytics.Model.NotificationData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Model.TableType.LoadLevelInformation.SubscriptionTable;

import com.nwdaf.Analytics.Model.TableType.QosSustainability.QosSustainabilitySubscriptionTable;

import com.nwdaf.Analytics.Model.TableType.UEmobility.UEmobilitySubscriptionTable;
import com.nwdaf.Analytics.Model.UserLocation;

import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
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

import static java.lang.System.out;
import static org.springframework.http.HttpHeaders.USER_AGENT;

public class BusinessLogic extends ResourceValues {


    @Autowired
    Nnwdaf_Repository repository;
    //checking

    private static final Logger logger = LoggerFactory.getLogger(BusinessLogic.class);

    Set<String> subID_SET = new HashSet<String>();
    public int subThValue = 0;
    public int currentLoadLevel = 0;


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



    /****UEmobility******/

    protected Object check_For_dataUEmobility(NnwdafEventsSubscriptionUEmobility nnwdafEventsSubscriptionUE, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        if (getAnalytics) {

            //List<EventConnectionUE> supiDataList = repository.checkForDataUE(nnwdafEventsSubscriptionUE.getSupi());
            List<UserLocation> supiDataList = getUElocation(nnwdafEventsSubscriptionUE.getSupi());

            // if data is not found -> calling collector function to collect data
            if (supiDataList == null || supiDataList.isEmpty()) {
                logger.warn("Data not found ");

                // Calling collector function
                Object obj = nwdaf_data_collectorUEmobility(nnwdafEventsSubscriptionUE, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // Adding supi into database
                repository.add_data_into_nwdafUEmobility_table(nnwdafEventsSubscriptionUE.getSupi());

                EventConnectionUE eventConnectionUE = new EventConnectionUE();
                eventConnectionUE.setMessage("Data not Found for " + nnwdafEventsSubscriptionUE.getSupi());
                eventConnectionUE.setSupi(nnwdafEventsSubscriptionUE.getSupi());
                eventConnectionUE.setDataStatus(false);

                logger.debug("supi: " + nnwdafEventsSubscriptionUE.getSupi() + "\n" +
                             "DataStatus:  " + eventConnectionUE.getDataStatus());

                //throw new NullPointerException("Data not found!");
                return eventConnectionUE;

            } else {
                // flag to check if getAnalytics ref count will be updated or not
                //flag = 1;
                Object obj = nwdaf_data_collectorUEmobility(nnwdafEventsSubscriptionUE, getAnalytics);

                if (obj instanceof ResponseEntity) {
                    return obj;
                }

                // if Data found returning JSON
                return supiDataList;
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

    /****UEmobility******/
    /****UEmobility******/
    public List<UserLocation> getUElocation(String supi){

        try {

                Boolean supiCheck = repository.supiExists(supi);

                String location = repository.getAllNotificationDataForUEMobility(supi);

                // Splitting location via ,
                String[] splitedString = location.split(",");


                // iterating through split string and finding User Location from particular ID
                List<UserLocation> userLocations = new ArrayList<>();
                for (int j = 0; j < splitedString.length; j++) {

                    /* now it's time to find the UserLocaiton by particular ID*/
                    // Type Casting String ID value to Integer Value;

                    Integer ID = Integer.valueOf(splitedString[j].trim());
                    UserLocation userLocation;
                    userLocation = repository.getUserLocationFromID(ID);
                    userLocations.add(userLocation);

                }
                return userLocations;


        }catch (EmptyResultDataAccessException e) {
            return null;
        }
        catch (Exception e){
            return null;
        }

    }



    /****UEmobility******/














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
         }  else {

            if(!getAnalytics)
            { increaseRefCount(nnwdafEventsSubscription.getEventID(), nnwdafEventsSubscription.getSnssais()); }
        }
        return null;
    }




    /********UEmobility*************/

    protected Object nwdaf_data_collectorUEmobility(NnwdafEventsSubscriptionUEmobility nnwdafEventsSubscriptionUE, boolean getAnalytics) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);


        if (!repository.supiExists(nnwdafEventsSubscriptionUE.getSupi())) {


            // Generating CorrelationID
            UUID correlationID = FrameWorkFunction.getUniqueID();



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

                response_handlerUEmobility(nnwdafEventsSubscriptionUE, responseCode, correlationID, con, getAnalytics);

                if (con != null) {
                    con.disconnect();
                }

            } catch (Exception ex) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }

            logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
            return correlationID;
        }  else {

            if(!getAnalytics)
           { repository.increment_ref_count(nnwdafEventsSubscriptionUE.getSupi()); }
        }
        return null;
    }


    /********UEmobility*************/





















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

                if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
                {
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
                }

                else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
                {
                    QosSustainabilitySubscriptionTable qos = new QosSustainabilitySubscriptionTable();

                    qos.setCorrelationID(String.valueOf(correlationID));
                    qos.setSubscriptionID(response.toString());
                    qos.setSnssais(nnwdafEventsSubscription.getSnssais());

                    if(getAnalytics)
                    {
                        if(!repository.snsExistsQosSubscriptionTable(qos.getSnssais()))
                        { repository.addDataQosSustainabilitySubscriptionTable(qos, true); }
                    }

                    else
                    { repository.addDataQosSustainabilitySubscriptionTable(qos, false); }
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


    /*********UEmobility*************/


    protected void response_handlerUEmobility(NnwdafEventsSubscriptionUEmobility nnwdafEventsSubscriptionUE,
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


    /*********UEmobility*************/







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
                                           int currentLoadLevel,
                                           String subscriptionID) throws IOException, JSONException {

        final String FUNCTION_NAME = Thread.currentThread().getStackTrace()[1].getMethodName() + "()";
        logger.debug(FrameWorkFunction.ENTER + FUNCTION_NAME);

        Counters.incrementSubscriptionNotifications();



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

        //String jsonInputString = "Hey I am Notification";

        JSONObject json = new JSONObject();

        json.put("eventID", eventID);
        json.put("snssais", snssais);


        // This value will be fetched from nwdafSliceLoadLevelSubscriptionData send by NF
        json.put("notificaionURI", "http://localhost:8081/nnwdaf-eventssubscription/v1/subscriptions");
        json.put("subscriptionID", subscriptionID);
        json.put("currentLoadLevel", currentLoadLevel);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");

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
        }

        finally
        { con.disconnect(); }

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

        if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        {
            mCorrelationID = repository.getUnSubCorrelationID_LoadLevelInformation(snssais);
            //   String mCorrelationID = repository.getUnSubCorrelationID(snssais);

            correlationID = repository.getCorrelationID_LoadLevelInformation(mCorrelationID);
        }

        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        {
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
        }

        finally
        { con.disconnect(); }

    }





    protected boolean testConnection() {

        try {
            URL url = new URL(testConnect_URI);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            if (con.getResponseCode() != HttpStatus.ACCEPTED.value()) {

                if(con != null)
                { con.disconnect(); }

                return false;
            }

            if(con != null)
            { con.disconnect(); }

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

        for(SliceLoadLevelInformation slice: list)
        {
            List<NotificationData> dataSet = repository.getAllNotificationData(slice.getSnssais(), slice.getCurrentLoadLevel());

            if(dataSet != null)
            {
                for(NotificationData notifyData: dataSet)
                {
                    /*if(!subID_SET.contains(notifyData.getSubscriptionID()))
                    {
                        subID_SET.add(notifyData.getSubscriptionID());

                        SubscriptionTable subscriptionData = repository.findById_subscriptionID(notifyData.getSubscriptionID());
                        send_notificaiton_to_NF(subscriptionData.getNotificationURI(), EventID.values()[subscriptionData.getEventID()].toString(), slice.getSnssais(), slice.getCurrentLoadLevel(), subscriptionData.getSubscriptionID());
                    } */

                    SubscriptionTable subscriptionData = repository.findById_subscriptionID(notifyData.getSubscriptionID());
                    send_notificaiton_to_NF(subscriptionData.getNotificationURI(), EventID.values()[subscriptionData.getEventID()].toString(), slice.getSnssais(), slice.getCurrentLoadLevel(), subscriptionData.getSubscriptionID());
                }
            }
        }

        logger.debug(FrameWorkFunction.EXIT + FUNCTION_NAME);
    }



    public boolean snsExists(Integer eventID, String snssais)
    {
        if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        { return repository.snsExistsLoadLevelSubscriptionTable(snssais); }

        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        { return repository.snsExistsQosSubscriptionTable(snssais); }

        return true;
    }


    public void increaseRefCount(Integer eventID, String snssais)
    {
        if(eventID == EventID.LOAD_LEVEL_INFORMATION.ordinal())
        { repository.increaseRefCount_LoadLevelSubscriptionTable(snssais); }

        else if(eventID == EventID.QOS_SUSTAINABILITY.ordinal())
        { repository.increaseRefCount_QosSubscriptionTable(snssais); }
    }

}
