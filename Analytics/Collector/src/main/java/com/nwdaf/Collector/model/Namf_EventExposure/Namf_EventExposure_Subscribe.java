package com.nwdaf.Collector.model.Namf_EventExposure;


import com.nwdaf.Collector.model.nestedModel.TargetEventReporting;

public class Namf_EventExposure_Subscribe {


    private int nfId;
    private TargetEventReporting targetEventReporting;
    private String notificationTargetAddress;


    public Namf_EventExposure_Subscribe() {
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

    public Namf_EventExposure_Subscribe(int nfId, TargetEventReporting targetEventReporting, String notificationTargetAddress) {
        this.nfId = nfId;
        this.targetEventReporting = targetEventReporting;
        this.notificationTargetAddress = notificationTargetAddress;
    }
}
