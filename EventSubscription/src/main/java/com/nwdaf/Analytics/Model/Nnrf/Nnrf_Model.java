package com.nwdaf.Analytics.Model.Nnrf;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter @Setter
public class Nnrf_Model {


    private int nfId;
    private int eventId;
    private String notificationTargetAddress;
    private String correlationId;
    private String subscriptionId;
    private String unSubCorrelationId;


    public Nnrf_Model(String correlationId, String subscriptionId, String unSubCorrelationId) {
        this.correlationId = correlationId;
        this.subscriptionId = subscriptionId;
        this.unSubCorrelationId = unSubCorrelationId;
    }


    public Nnrf_Model(int nfId, int eventId, String notificationTargetAddress, String correlationId) {
        this.nfId = nfId;
        this.eventId = eventId;
        this.notificationTargetAddress = notificationTargetAddress;
        this.correlationId = correlationId;
    }
}
