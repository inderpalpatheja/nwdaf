package com.nwdaf.Analytics.Model.AMFModel;
/*
 * @created 11/03/2020 - 3:47 PM
 * @project EventSubscription
 * @author  sheetalkumar
 */

public class AMFModel {


    private int nfId;
    private int eventId;
    private String notificationTargetAddress;
    private String correlationId;
    private String subscriptionId;
    private String unSubCorrelationId;

    public AMFModel() {
    }

    public int getNfId() {
        return nfId;
    }

    public void setNfId(int nfId) {
        this.nfId = nfId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getNotificationTargetAddress() {
        return notificationTargetAddress;
    }

    public void setNotificationTargetAddress(String notificationTargetAddress) {
        this.notificationTargetAddress = notificationTargetAddress;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getUnSubCorrelationId() {
        return unSubCorrelationId;
    }

    public void setUnSubCorrelationId(String unSubCorrelationId) {
        this.unSubCorrelationId = unSubCorrelationId;
    }

    public AMFModel(int nfId, int eventId, String notificationTargetAddress, String correlationId, String subscriptionId, String unSubCorrelationId) {
        this.nfId = nfId;
        this.eventId = eventId;
        this.notificationTargetAddress = notificationTargetAddress;
        this.correlationId = correlationId;
        this.subscriptionId = subscriptionId;
        this.unSubCorrelationId = unSubCorrelationId;
    }
}
