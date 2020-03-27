package com.nwdaf.Analytics.Controller.ConnectionCheck;

import org.springframework.http.HttpStatus;

public class QosAnalyticsInfo {

    String snssais;
    Integer ranUeThroughput;
    Integer qosFlowRetain;
    String message;
    Boolean dataStatus;


    public QosAnalyticsInfo() { }


    public QosAnalyticsInfo(String snssais, HttpStatus status)
    {
        if(status == HttpStatus.NOT_FOUND)
        {
            this.message = "Data Not Found for " + snssais;
            this.ranUeThroughput = 0;
            this.qosFlowRetain = 0;
            this.snssais = snssais;
            this.dataStatus = Boolean.FALSE;
        }
    }



    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public Integer getRanUeThroughput() {
        return ranUeThroughput;
    }

    public void setRanUeThroughput(Integer ranUeThroughput) {
        this.ranUeThroughput = ranUeThroughput;
    }

    public Integer getQosFlowRetain() {
        return qosFlowRetain;
    }

    public void setQosFlowRetain(Integer qosFlowRetain) {
        this.qosFlowRetain = qosFlowRetain;
    }

    public Boolean getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Boolean dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
