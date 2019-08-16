package com.appzone.cipg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.appzone.cipg.global.AppState;
import com.appzone.cipg.model.Charge;
import com.appzone.cipg.ui.WebViewActivity;



public class CipgSdk {
    public static CipgCallback cipgCallback;
    public static void pay(Activity activity,@NonNull Charge charge, CipgCallback callback) {
        if (BuildConfig.DEBUG && (activity == null))
            throw new AssertionError("activity must not be null");

        if (charge.getOrderId().equalsIgnoreCase(""))
            throw new AssertionError("OrderId must not be empty");


        if (charge.getMerchantId().equalsIgnoreCase(""))
            throw new AssertionError("MerchantId must not be empty");

        if (charge.getAmount().equalsIgnoreCase(""))
            throw new AssertionError("Amount must not be null");

        try{
            Long amount = Long.valueOf(charge.getAmount());
        }catch (Exception e){
            throw new AssertionError("Invalid amount passed");
        }

        if (charge.getCustomerEmail().equalsIgnoreCase(""))
            throw new AssertionError("Customer email must not be empty");

        if (charge.getProductName().equalsIgnoreCase(""))
            throw new AssertionError("Product name must not be empty");


        AppState.appContext = activity;
        AppState.amount = charge.getAmount();
        AppState.merchantId = charge.getMerchantId();
        AppState.amount = charge.getAmount();
        String currencyCode = charge.getCurrencyCode();
        AppState.currencyCode= currencyCode.equalsIgnoreCase("")?AppState.currencyCode : currencyCode;
        AppState.email = charge.getCustomerEmail();
        AppState.prod = charge.getProductName();
        AppState.orderId = charge.getOrderId();

        cipgCallback = callback;
        openWebView(activity);
    }

    private static void openWebView(Context context){
        context.startActivity(new Intent(context,WebViewActivity.class));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        cipgCallback = null;
    }
}





