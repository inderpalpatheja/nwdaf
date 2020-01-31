package com.nwdaf.AMF.model.Namf_EventExposure.nestedModel;

public class EventReportingInformationInformation {

    // Mdoe of reporting -eq reporting up to a max number of report, periodic reporing along with
    //periordicity, reporting up to a maximum duration;

    private int modeOfReport;
    private int maximumNumberOfReport;
    private int maximumDurationOfReport;
    private int immediateReportingFlag;
    private int sampingRatio;
    private int groupReportingGuardTime;

    public EventReportingInformationInformation() {
    }

    public int getModeOfReport() {
        return modeOfReport;
    }

    public void setModeOfReport(int modeOfReport) {
        this.modeOfReport = modeOfReport;
    }

    public int getMaximumNumberOfReport() {
        return maximumNumberOfReport;
    }

    public void setMaximumNumberOfReport(int maximumNumberOfReport) {
        this.maximumNumberOfReport = maximumNumberOfReport;
    }

    public int getMaximumDurationOfReport() {
        return maximumDurationOfReport;
    }

    public void setMaximumDurationOfReport(int maximumDurationOfReport) {
        this.maximumDurationOfReport = maximumDurationOfReport;
    }

    public int getImmediateReportingFlag() {
        return immediateReportingFlag;
    }

    public void setImmediateReportingFlag(int immediateReportingFlag) {
        this.immediateReportingFlag = immediateReportingFlag;
    }

    public int getSampingRatio() {
        return sampingRatio;
    }

    public void setSampingRatio(int sampingRatio) {
        this.sampingRatio = sampingRatio;
    }

    public int getGroupReportingGuardTime() {
        return groupReportingGuardTime;
    }

    public void setGroupReportingGuardTime(int groupReportingGuardTime) {
        this.groupReportingGuardTime = groupReportingGuardTime;
    }

    public EventReportingInformationInformation(int modeOfReport, int maximumNumberOfReport, int maximumDurationOfReport, int immediateReportingFlag, int sampingRatio, int groupReportingGuardTime) {
        this.modeOfReport = modeOfReport;
        this.maximumNumberOfReport = maximumNumberOfReport;
        this.maximumDurationOfReport = maximumDurationOfReport;
        this.immediateReportingFlag = immediateReportingFlag;
        this.sampingRatio = sampingRatio;
        this.groupReportingGuardTime = groupReportingGuardTime;
    }
}
