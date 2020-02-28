package com.nwdaf.Analytics.Service;

import com.nwdaf.Analytics.Controller.ConnectionCheck.ConnectionStatus;
import com.nwdaf.Analytics.Controller.ConnectionCheck.EventConnection;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.MetaData.Counters;
import com.nwdaf.Analytics.Model.Nnrf.Nnrf_Model;
import com.nwdaf.Analytics.Model.NnwdafEventsSubscription;
import com.nwdaf.Analytics.Model.TableType.SliceLoadLevelInformation;
import com.nwdaf.Analytics.Model.TableType.SliceLoadLevelSubscriptionData;
import com.nwdaf.Analytics.Model.TableType.SliceLoadLevelSubscriptionTable;
import com.nwdaf.Analytics.Repository.Nnwdaf_Repository;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.lang.System.out;
import static org.springframework.http.HttpHeaders.USER_AGENT;

public class FrameWorkFunctions extends ResourceValues {


    @Autowired
    Nnwdaf_Repository repository;


    private static final Logger logger = LoggerFactory.getLogger(FrameWorkFunctions.class);

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

        logger.debug("Enter check_For_data()");

        if (getAnalytics) {

            List<EventConnection> snssaisDataList = repository.checkForData(nnwdafEventsSubscription.getSnssais(),
                    nnwdafEventsSubscription.isAnySlice());

            // if data is not found -> calling collector function to collect data
            if (snssaisDataList.isEmpty()) {
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

            /*
            if (!repository.snsExists(nnwdafEventsSubscription.getSnssais())) {
                getAnalytics = true;
                UUID correlationID = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics);
            } else {
                repository.increment_ref_count(nnwdafEventsSubscription.getSnssais());
            } */

            Object obj = nwdaf_data_collector(nnwdafEventsSubscription, getAnalytics);

            if (obj instanceof ResponseEntity) {
                return obj;
            }
        }

        logger.debug("Exit check_For_data()");
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

        logger.debug("Entered Collector Function");

        Object snssais_object = repository.findby_snssais(nnwdafEventsSubscription.getSnssais());

        if (snssais_object == null) {

            // Generating CorrelationID
            UUID correlationID = UUID.randomUUID();

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

                if(con != null)
                { con.disconnect(); }

            } catch (Exception ex) {
                return new ResponseEntity<ConnectionStatus>(new ConnectionStatus(), HttpStatus.NOT_FOUND);
            }

            logger.debug("Exit Collector Function");
            return correlationID;
        } else {
            repository.increment_ref_count(nnwdafEventsSubscription.getSnssais());
        }
        return null;
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

        logger.debug("Enter response_handler()");

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            // incrementing Counter
            Counters.incrementCollectorSubscriptions();
            showCounters();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            try {

               /* if (!repository.snsExists(nnwdafEventsSubscription.getSnssais())) {

                    NwdafSliceLoadLevelSubscriptionTableModel slice = new NwdafSliceLoadLevelSubscriptionTableModel();

                    slice.setCorrelationID(String.valueOf(correlationID));
                    slice.setSubscriptionID(response.toString());
                    slice.setSnssais(nnwdafEventsSubscription.getSnssais());

                    repository.addCorrealationIDAndUnSubCorrelationIDIntoNwdafIDTable(slice);

                } else {
                    if (!repository.isGetAnalytics(nnwdafEventsSubscription.getSnssais())) {
                        repository.setGetAnalytics(nnwdafEventsSubscription.getSnssais(), true);
                        repository.increment_ref_count(nnwdafEventsSubscription.getSnssais());
                    } */

                SliceLoadLevelSubscriptionTable slice = new SliceLoadLevelSubscriptionTable();

                slice.setCorrelationID(String.valueOf(correlationID));
                slice.setSubscriptionID(response.toString());
                slice.setSnssais(nnwdafEventsSubscription.getSnssais());

                if (getAnalytics) {
                    if (!repository.snsExists(slice.getSnssais())) {
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
    }






    /**
     * @param subscriptionID
     * @param snssais
     * @param loadLevelThreshold
     * @desc function to add values in subscription data table
     */
    protected void add_values_into_subscriptionData(UUID subscriptionID, String snssais, int loadLevelThreshold) {


        logger.debug("enter add_values_into_subscriptionData");
        SliceLoadLevelSubscriptionData nwdafSliceLoadLevelSubscriptionDataModel = new
                SliceLoadLevelSubscriptionData();

        // Setting values in NwdafSliceLoadLevelSubscriptionData object
        nwdafSliceLoadLevelSubscriptionDataModel.setSubscriptionID(String.valueOf(subscriptionID));
        nwdafSliceLoadLevelSubscriptionDataModel.setSnssais(snssais);
        nwdafSliceLoadLevelSubscriptionDataModel.setLoadLevelThreshold(loadLevelThreshold);

        // Adding nwdafSliceLoadLevelSubscriptionDatamodel into NwdafSliceLoadLevelSubscriptionData Table [ Database ]
        repository.addDataIntoNwdafSliceLoadLevelSubscriptionDataTable(nwdafSliceLoadLevelSubscriptionDataModel);
        logger.debug("enter add_values_into_subscriptionData");

    }

    /**
     * @param subscriptionID
     * @return
     * @throws URISyntaxException
     * @desc function to send response header to NF consumers
     */
    protected HttpHeaders send_response_header_to_NF(UUID subscriptionID) throws URISyntaxException {

        logger.debug("enter send_response_header_to_NF ");

        URI location = new URI(URI + "subscriptions/" + String.valueOf(subscriptionID));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);


        logger.debug("Exit enter send_response_header_to_NF  ");
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
     * @throws IOException
     * @desc this function manages notifications for subscribers
     */
    protected void nwdaf_notification_manager() throws IOException, JSONException {

        logger.debug("Entered nwdaf_notification_manager()");

        // Fetching all snssais list
        List<SliceLoadLevelInformation> list = repository.getALLsnssais();

        if (repository.getALLsnssais() == null) {
            logger.warn("no snssais Found [ Null object ]");
        }


        // Iterating throw list and getting all subscriptionID related to that snssais
        // i = indexOfsnssaisList || j = indexOfSubscriptionList

        for (int indexOfSnssaisList = 0; indexOfSnssaisList < list.size(); indexOfSnssaisList++) {

            // checking current Threshold value
            currentLoadLevel = repository.currentLoadLevel(list.get(indexOfSnssaisList).getSnssais());

            if (repository.getALLsnssais() == null) {
                logger.warn("Null object!");
            }
            //    if (currentLoadLevel == 0)
            //      continue;

            // Fetching all subscription ID of a particular snssais
            List<SliceLoadLevelSubscriptionData> subscriptionList = fetch_all_subscitpionID(list.get(indexOfSnssaisList).getSnssais());

            if (subscriptionList == null) {
                logger.warn("No subscription List found");
            }

            // Iterating throw subscriptionIDs to check whose TH value has been reached
            for (int indexOfSubscriptionList = 0; indexOfSubscriptionList < subscriptionList.size(); indexOfSubscriptionList++) {

                // Checking currentLoadLevel Of that snsssais if  current Load level greater than required TH Value
                // then send notification
                subThValue = repository.getLoadLevelThreshold(String.valueOf(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID()));

                if (currentLoadLevel > subThValue) {

                        /* Checking Hash SET if notification has been sent on that particular subscriptionID/
                       Then we do not have to send the notification again.
                        */

                    if (!(subID_SET.contains(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID()))) {
                        subID_SET.add(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID());

                        if (subscriptionList.get(indexOfSubscriptionList).getSubscriptionID() == null) {
                            logger.warn("Null Object Found! [ No subscription ID is present ]");
                        } else {

                            // Fetching notification URL on which NWDAF will send notification
                            String NotificationURI = repository.getNotificationURI(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID());

                            if (repository.getNotificationURI(subscriptionList.get(indexOfSubscriptionList).getSubscriptionID()) == null) {
                                logger.warn("No subscription ID is present -> can't get Notification URI ");
                            }

                            // Calling sendNotification Function
                            String subscriptionID = subscriptionList.get(indexOfSubscriptionList).getSubscriptionID();

                            String eventID = EventID.values()[repository.findById_subscriptionID(subscriptionID).getEventID()].toString();
                            String snssais = repository.getSnssais(subscriptionID);
                            int currentLoadLevel = repository.getCurrentLoadLevel(snssais);

                            send_notificaiton_to_NF(NotificationURI, eventID, snssais, currentLoadLevel, subscriptionID);

                        }

                    }

                }

            }

        }

    }


    /**
     * @param snssais
     * @return
     * @desc this function will fetch all subscription IDs of a particular snssais
     */
    //Fetching all subscriptionID of particular snssais
    protected List<SliceLoadLevelSubscriptionData> fetch_all_subscitpionID(String snssais) {

        logger.debug("Enter fetch_all_subscitpionID");

        List<SliceLoadLevelSubscriptionData> subscriptionIDList = repository.getAllSubIdsbysnssais(snssais);

        if (subscriptionIDList == null) {
            logger.warn("No subscriber found");
        }

        logger.debug("Exit fetch_all_subscitpionID");
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

        logger.debug("Entered send_notificaiton_to_NF()");

        Counters.incrementSubscriptionNotifications();
        showCounters();

        URL url = null;
        try {
            url = new URL(notificationURI);
        } catch (MalformedURLException e) {
            logger.warn("http connect exception found");
            e.printStackTrace();
        }

        // Opening connection;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }







    /**
     * @desc this function holds details of counters
     */
    protected void showCounters() {

        logger.debug("Enter showCounters()");

        logger.info("> # Event_Subscriptions: " + Counters.getSubscriptions());
        logger.info("> # Event_UnSubscriptions: " + Counters.getUnSubscriptions());
        logger.info("> # Event_SubscriptionUpdates: " + Counters.getSubscriptionUpdates());
        logger.info("> # Event_SubscriptionNotifications: " + Counters.getSubscriptionNotifications());

        logger.info("> # Collector_Subscriptions: " + Counters.getCollectorSubscriptions());
        logger.info("> # Collector_SubscriptionNotifications: " + Counters.getCollectorSubscriptionNotifications());
        logger.info("> # Collector_AnalyticsSubscriptions: " + Counters.getAnalyticsSubscriptions());
        logger.info("> # Collector_AnalyticsNotifications: " + Counters.getAnalyticsNotifications());

        logger.debug("Exit showCounters()");
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




    // @RequestMapping(method = RequestMethod.DELETE, value = "/Nnrf_NFManagement_NFStatusUnSubscribe")
    // @RequestMapping("/t")
    protected void unsubscribeFromNWDAF(String snssais) throws Exception {

        // here subscriptionID = unsubCorrealtionID

        // String subscriptionID =  repository.getUnSubCorrelationID(snssais);

        URL obj = new URL(DELETE_NRF_URL);
        //  out.println(DELETE_NRF_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        String mCorrelationID = repository.getUnSubCorrelationID(snssais);
        //   String mCorrelationID = repository.getUnSubCorrelationID(snssais);

        String correlationID = repository.getCorrelationID(mCorrelationID);

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

}
