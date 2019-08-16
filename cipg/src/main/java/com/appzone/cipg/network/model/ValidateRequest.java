package com.appzone.cipg.network.model;

import com.google.gson.annotations.SerializedName;

public class ValidateRequest {

    @SerializedName("Amount")
    private String amount="";

    @SerializedName("MerchantId")
    private String merchantId = "";

    @SerializedName("CurrencyCode")
    private String currencyCode = "";

    @SerializedName("OrderId")
    private String orderId = "";

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
