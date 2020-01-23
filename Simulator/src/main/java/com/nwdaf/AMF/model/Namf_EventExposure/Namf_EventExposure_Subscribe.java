package com.nwdaf.AMF.model.Namf_EventExposure;


import com.nwdaf.AMF.model.Namf_EventExposure.nestedModel.TargetEventReporting;


public class Namf_EventExposure_Subscribe {


    private int nfId;
    private int eventId;
    private TargetEventReporting targetEventReporting;
    private String notificationTargetAddress;
    private int CorrelationId;
    private int unSubCorrelationId;

    public Namf_EventExposure_Subscribe() {
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

    public TargetEventReporting getTargetEventReporting() {
        return targetEventReporting;
    }

    public void setTargetEventReporting(TargetEventReporting targetEventReporting) {
        this.targetEventReporting = targetEventReporting;
    }

    public String getNotificationTargetAddress() {
        return notificationTargetAddress;
    }

    public void setNotificationTargetAddress(String notificationTargetAddress) {
        this.notificationTargetAddress = notificationTargetAddress;
    }

    public int getCorrelationId() {
        return CorrelationId;
    }

    public void setCorrelationId(int correlationId) {
        CorrelationId = correlationId;
    }

    public int getUnSubCorrelationId() {
        return unSubCorrelationId;
    }

    public void setUnSubCorrelationId(int unSubCorrelationId) {
        this.unSubCorrelationId = unSubCorrelationId;
    }

    public Namf_EventExposure_Subscribe(int nfId, int eventId, TargetEventReporting targetEventReporting, String notificationTargetAddress, int correlationId, int unSubCorrelationId) {
        this.nfId = nfId;
        this.eventId = eventId;
        this.targetEventReporting = targetEventReporting;
        this.notificationTargetAddress = notificationTargetAddress;
        CorrelationId = correlationId;
        this.unSubCorrelationId = unSubCorrelationId;
    }
}
