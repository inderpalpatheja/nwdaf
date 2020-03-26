package com.nwdaf.Analytics.Service.Validator.ErrorReport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import org.springframework.http.HttpStatus;

public class QosSustainabilityError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = EventID.QOS_SUSTAINABILITY.toString() + " error";

    @JsonProperty("5Qi")
    Object _5Qi;

    Object snssais;
    Object mcc;
    Object mnc;
    Object tac;
    Object ranUeThroughputThreshold;
    Object qosFlowRetainThreshold;


    public QosSustainabilityError(SubscriptionRawData rawData)
    {
        this.snssais = rawData.getSnssais();
        this._5Qi = rawData.get_5Qi();
        this.mcc = rawData.getMcc();
        this.mnc = rawData.getMnc();
        this.tac = rawData.getTac();
        this.ranUeThroughputThreshold = rawData.getRanUeThroughputThreshold();
        this.qosFlowRetainThreshold = rawData.getQosFlowRetainThreshold();
    }


    public Object getSnssais() {
        return snssais;
    }

    public void setSnssais(Object snssais) {
        this.snssais = snssais;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object get_5Qi() {
        return _5Qi;
    }

    public void set_5Qi(Object _5Qi) {
        this._5Qi = _5Qi;
    }

    public Object getMcc() {
        return mcc;
    }

    public void setMcc(Object mcc) {
        this.mcc = mcc;
    }

    public Object getMnc() {
        return mnc;
    }

    public void setMnc(Object mnc) {
        this.mnc = mnc;
    }

    public Object getTac() {
        return tac;
    }

    public void setTac(Object tac) {
        this.tac = tac;
    }

    public Object getRanUeThroughputThreshold() {
        return ranUeThroughputThreshold;
    }

    public void setRanUeThroughputThreshold(Object ranUeThroughputThreshold) {
        this.ranUeThroughputThreshold = ranUeThroughputThreshold;
    }

    public Object getQosFlowRetainThreshold() {
        return qosFlowRetainThreshold;
    }

    public void setQosFlowRetainThreshold(Object qosFlowRetainThreshold) {
        this.qosFlowRetainThreshold = qosFlowRetainThreshold;
    }
}
