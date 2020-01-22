package com.example.Collector.model.Nsmf_EventExposure;


import com.nwdaf.Collector.model.nestedModel.EventReportingInformationInformation;
import com.nwdaf.Collector.model.nestedModel.TargetEventReporting;

public class Nsmf_EventExposure_Subscribe {

    private int nfId;
    private TargetEventReporting targetEventReporting;
    private String notificationTargetAddress;
    private EventReportingInformationInformation eventReportingInformationInformation;

    public Nsmf_EventExposure_Subscribe() {
    }

    public int getNfId() {
        return nfId;
    }

    public void setNfId(int nfId) {
        this.nfId = nfId;
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

    public EventReportingInformationInformation getEventReportingInformationInformation() {
        return eventReportingInformationInformation;
    }

    public void setEventReportingInformationInformation(EventReportingInformationInformation eventReportingInformationInformation) {
        this.eventReportingInformationInformation = eventReportingInformationInformation;
    }

    public Nsmf_EventExposure_Subscribe(int nfId, TargetEventReporting targetEventReporting, String notificationTargetAddress, EventReportingInformationInformation eventReportingInformationInformation) {
        this.nfId = nfId;
        this.targetEventReporting = targetEventReporting;
        this.notificationTargetAddress = notificationTargetAddress;
        this.eventReportingInformationInformation = eventReportingInformationInformation;
    }
}
