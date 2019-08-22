package com.appzone.cipg;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appzone.cipg.global.AppState;
import com.appzone.cipg.global.Constants;
import com.appzone.cipg.model.Error;
import com.appzone.cipg.ui.SdkCallback;

public class CipgWebViewClient extends WebViewClient {
    private String TAG = CipgWebViewClient.class.getSimpleName();
    private SdkCallback completedCallback;
    public CipgWebViewClient(SdkCallback completedCallback) {
        this.completedCallback = completedCallback;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        Log.d(TAG, "Loading resources");
        Log.d(TAG, url);
        if(url.equalsIgnoreCase(AppState.baseUrl+Constants.PAYMENT_URL)){
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
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.d(TAG, "onReceiveError event intercepted");
        Log.d(TAG, "Request + " + failingUrl);
        Error netError = new Error();
        netError.setReason(description);

        if (!description.equalsIgnoreCase("Not Found")) {
            CipgSdk.cipgCallback.onError(netError);
            handleCompletedCallback();
        }
    }

    private void handleCompletedCallback() {
        if (completedCallback != null) {
            completedCallback.close();
        }
    }
}
