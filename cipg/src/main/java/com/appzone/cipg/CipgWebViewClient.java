package com.appzone.cipg;

import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appzone.cipg.global.Constants;
import com.appzone.cipg.model.Error;
import com.appzone.cipg.ui.SdkCallback;

public class CipgWebViewClient extends WebViewClient {
    private String TAG = CipgWebViewClient.class.getSimpleName();
    private SdkCallback completedCallback;
//    private String successString = "http://40.76.66.169/FidelityCIPG/MerchantServices/SDKPaymentOutcome.aspx?TransactionReference=20190816111411ktT&OrderID=119163052";
//    private String someString = " http://40.76.66.169/FidelityCIPG/MerchantServices/SDKPaymentOutcome.aspx?TransactionReference=&OrderID=&ErrorMessage=DuplicateOrderID";

    public CipgWebViewClient(SdkCallback completedCallback) {
        this.completedCallback = completedCallback;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        Log.d(TAG, "Loading resources");
        Log.d(TAG, url);
        if(url.equalsIgnoreCase(Constants.START_URL)){
            completedCallback.loading();
        }
    }



    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d(TAG, "onPageFinished event intercepted");
        Log.d(TAG, "URL + " + url);

        if (url.contains(Constants.OUTCOME_URL)) {
            Uri uri = Uri.parse(url);
            String errorMessage = uri.getQueryParameter(Constants.ERROR_MESSAGE_KEY);
            String transRef = uri.getQueryParameter(Constants.TRANSACTION_REF_KEY);
            String orderId = uri.getQueryParameter(Constants.ORDER_ID_KEY);

            Error error = new Error();

            //Payment was successful
            if(transRef != null && !transRef.equalsIgnoreCase("")
                    && orderId != null && !orderId.equalsIgnoreCase("")
            ){
                completedCallback.startTransRefValidation(transRef, orderId);
            }
            //Payment failed
            else if(errorMessage != null){
                error.setReason(errorMessage);
                CipgSdk.cipgCallback.onError(error);
                handleCompletedCallback();

            }
            //Something else happened
            else{
                error.setReason("Unknown error occurred");
                handleCompletedCallback();
            }


        } else {
            completedCallback.loadingCompleted();
        }
    }


    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.d(TAG, "onReceiveError event intercepted");
        Log.d(TAG, "Request + " + request.getUrl().toString());
        Error netError = new Error();
        netError.setReason(error.toString());

        if (!error.toString().trim().equalsIgnoreCase("not found")
                || !request.getUrl().toString().toLowerCase().contains(".png")
                || !request.getUrl().toString().toLowerCase().contains(".css")
                || !request.getUrl().toString().contains("WebResourceError")
        ) {
            CipgSdk.cipgCallback.onError(netError);
            handleCompletedCallback();
        }
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        Log.d(TAG, "onReceivedHttpError event intercepted");
        Log.d(TAG, "Request + " + request.getUrl().toString());
        Log.d(TAG, "Request Error + " + errorResponse.getReasonPhrase());
        Error error = new Error();
        error.setReason(errorResponse.getReasonPhrase());

        if (!errorResponse.getReasonPhrase().equalsIgnoreCase("Not Found")) {
            CipgSdk.cipgCallback.onError(error);
            handleCompletedCallback();
        }
    }

//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        //Todo: this should be the url to match when cancel button is pressed.
//        String successRedirectUrl = "http://40.76.66.169/FidelityCIPG/MerchantServices/MerchantMobile/PaymentOutcome.aspx";
//        String url = request.getUrl().toString();
//        Log.d(TAG, "shouldOverrideUrlLoading event intercepted");
//        Log.d(TAG, "url: " + request.getUrl().toString());
//        if (url.toLowerCase().contains(successRedirectUrl.toLowerCase())) {
//            String token = "someToken"; //Todo: Get the token from the redirect url here.
//            Response response = new Response();
//            response.setTransRef(token);
//            CipgSdk.cipgCallback.onSuccess(response);
//            handleCompletedCallback();
//            return true;
//        } else {
//            view.loadUrl(url);
//            return false;
//        }
//    }

    private void handleCompletedCallback() {
        if (completedCallback != null) {
            completedCallback.close();
        }
    }
}
