package com.appzone.cipg.network.model;

import com.google.gson.annotations.SerializedName;

public class ValidationResponse {

    /*{"OrderId":"119163051","MerchantId":"00037","Amount":"2000.00","OriginalAmount":"2000.00","CurrencyCode":"566",
    "OriginalCurrencyCode":"566","Status":"NotProcessed","PaymentRef":null,"TransactionRef":"20190816110126orT","PaymentGateway":"",
    "ResponseCode":null,"StatusCode":"04",
    "ResponseDescription":"Not completed by customer","Date":"16-08-2019 11:04","AmountIntegrityCode":"00","EncryptedKey":null}*/

    @SerializedName("OrderId")
    private String orderId;

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("StatusCode")
    private String statusCode;

    @SerializedName("ResponseDescription")
    private String responseDescription;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }
}
